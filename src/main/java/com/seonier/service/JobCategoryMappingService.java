package com.seonier.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seonier.dto.response.JobCategoryResponse;
import com.seonier.persistence.mapper.JobCategoryMapper;
import com.seonier.persistence.mapper.JobMapper;
import com.seonier.persistence.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 직업 분류 매핑 서비스
 * - 1단계: 유사 매칭 (로컬)
 * - 2단계: OpenAI 매핑 (실패한 것만)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobCategoryMappingService {

    private final JobMapper jobMapper;
    private final JobCategoryMapper jobCategoryMapper;
    private final JobCategoryService jobCategoryService;
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    /**
     * 전체 프로세스 실행
     */
    public void mapAllJobCategories() {
        log.info("=== 직업 분류 매핑 시작 ===");
        
        // 1단계: 유사 매칭
        int fuzzyMatchCount = fuzzyMatchJobs();
        log.info("1단계 완료: 유사 매칭 {}건", fuzzyMatchCount);
        
        // 2단계: OpenAI 매핑 (실패한 것만)
        int aiMatchCount = aiMatchUnmatchedJobs();
        log.info("2단계 완료: OpenAI 매핑 {}건", aiMatchCount);
        
        // 최종 통계
        printFinalStatistics();
        
        log.info("=== 직업 분류 매핑 완료 ===");
    }

    /**
     * 1단계: 유사 매칭으로 직업 분류 업데이트 (건너뛰기)
     */
    private int fuzzyMatchJobs() {
        log.info("1단계: 유사 매칭 건너뛰기 - 전부 OpenAI로 처리");
        return 0;
    }

    /**
     * 2단계: OpenAI로 매칭 실패한 직종 처리
     */
    private int aiMatchUnmatchedJobs() {
        log.info("2단계: OpenAI 매핑 시작");
        
        // 여전히 매칭 안된 직종 조회
        List<Job> unmatchedJobs = jobMapper.findAll().stream()
                .filter(job -> job.getJobCategory1() == null)
                .filter(job -> job.getJobPosition() != null && !job.getJobPosition().trim().isEmpty())
                .filter(job -> !job.getJobPosition().equals("-"))
                .collect(Collectors.toList());
        
        if (unmatchedJobs.isEmpty()) {
            log.info("매칭 실패한 직종 없음");
            return 0;
        }
        
        log.info("OpenAI 매핑 대상: {}건", unmatchedJobs.size());
        
        // 배치 처리 (100개씩)
        int batchSize = 100;
        int totalMatched = 0;
        
        for (int i = 0; i < unmatchedJobs.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, unmatchedJobs.size());
            List<Job> batch = unmatchedJobs.subList(i, endIndex);
            
            int batchNum = (i / batchSize) + 1;
            int totalBatches = (unmatchedJobs.size() + batchSize - 1) / batchSize;
            
            log.info("\n========== 배치 {}/{} 시작 ({}~{}번째) ==========", 
                    batchNum, totalBatches, i + 1, endIndex);
            
            int matched = processBatchWithOpenAI(batch);
            totalMatched += matched;
            
            log.info("========== 배치 {}/{} 완료 | 누적 매핑: {}건 ==========", 
                    batchNum, totalBatches, totalMatched);
            
            // API 레이트 리밋 방지
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        return totalMatched;
    }

    /**
     * OpenAI로 배치 처리
     */
    private int processBatchWithOpenAI(List<Job> jobs) {
        try {
            // 직종명 또는 채용공고 제목으로 매핑할 텍스트 추출
            Map<String, String> jobTextMap = new HashMap<>();
            for (Job job : jobs) {
                String key;
                if (job.getJobPosition() != null && !job.getJobPosition().trim().isEmpty()) {
                    key = job.getJobPosition();
                } else if (job.getJobTitle() != null && !job.getJobTitle().trim().isEmpty()) {
                    key = job.getJobTitle();
                } else {
                    continue; // 둘 다 없으면 스킵
                }
                jobTextMap.put(key, key);
            }
            
            Set<String> uniqueTexts = jobTextMap.keySet();
            
            log.info("OpenAI 처리 시작: {} 개 고유 텍스트 (직종명 또는 제목)", uniqueTexts.size());
            
            // OpenAI 프롬프트 생성
            String prompt = buildOpenAIPrompt(uniqueTexts);
            log.debug("프롬프트 길이: {} 자", prompt.length());
            
            // OpenAI API 호출 (OpenAiService를 통해)
            log.info("OpenAI API 호출 중...");
            ChatResponse response = openAiService.callOpenAI(
                    prompt,
                    OpenAiApi.ChatModel.GPT_4_O_MINI.getValue(),
                    0.1
            );
            log.info("OpenAI API 응답 받음");
            
            String responseText = response.getResult().getOutput().getText();
            log.debug("OpenAI 응답: {}", responseText);
            
            // JSON 파싱
            Map<String, JobCategoryMapping> mappings = parseOpenAIResponse(responseText);
            log.info("파싱 완료: {} 개 매핑 결과 받음", mappings.size());
            
            // DB 업데이트
            int matchCount = 0;
            for (Job job : jobs) {
                // 직종명 또는 제목으로 키 생성
                String key = null;
                if (job.getJobPosition() != null && !job.getJobPosition().trim().isEmpty()) {
                    key = job.getJobPosition();
                } else if (job.getJobTitle() != null && !job.getJobTitle().trim().isEmpty()) {
                    key = job.getJobTitle();
                }
                
                if (key == null) continue;
                
                JobCategoryMapping mapping = mappings.get(key);
                if (mapping != null && mapping.getMiddleName() != null) {
                    // JOB_CATEGORY 테이블에서 중분류로 조회 (아무 직업이나 하나 가져옴)
                    JobCategoryResponse category = jobCategoryMapper.findByMiddleName(mapping.getMiddleName());
                    if (category != null) {
                        updateJobCategory(job, category);
                        matchCount++;
                        log.debug("✅ OpenAI 매핑 성공: '{}' → '{}'", 
                                key, category.getMiddleName());
                    } else {
                        log.warn("⚠️ 중분류 '{}' DB 조회 실패 (원본: '{}')", 
                                mapping.getMiddleName(), key);
                    }
                } else {
                    log.warn("⚠️ 매핑 결과 없음: '{}'", key);
                }
            }
            
            log.info("✅ 이번 배치 매핑 성공: {}/{}건", matchCount, jobs.size());
            return matchCount;
            
        } catch (Exception e) {
            log.error("❌ OpenAI 배치 처리 실패: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * OpenAI 프롬프트 생성
     */
    private String buildOpenAIPrompt(Set<String> texts) {
        StringBuilder sb = new StringBuilder();
        sb.append("다음 직종명 또는 채용공고 제목들을 KECO 2018 중분류 중 하나로 매핑해주세요.\n\n");
        
        sb.append("=== KECO 2018 중분류 목록 (34개) ===\n");
        sb.append("01: 관리직(입법·부서장)\n");
        sb.append("02: 경영·행정·사무직\n");
        sb.append("03: 금융·보험직\n");
        sb.append("11: 인문·사회과학 연구직\n");
        sb.append("12: 자연·생명과학 연구직\n");
        sb.append("13: 정보통신 연구개발직 및 공학기술직\n");
        sb.append("14: 건설·채굴 연구개발직 및 공학기술직\n");
        sb.append("15: 제조 연구개발직 및 공학기술직\n");
        sb.append("21: 교육직\n");
        sb.append("22: 법률직\n");
        sb.append("23: 사회복지·종교직\n");
        sb.append("24: 경찰·소방·교도직\n");
        sb.append("25: 군인\n");
        sb.append("30: 보건·의료직\n");
        sb.append("41: 예술·디자인·방송직\n");
        sb.append("42: 스포츠·레크리에이션직\n");
        sb.append("51: 미용·예식 서비스직\n");
        sb.append("52: 여행·숙박·오락 서비스직\n");
        sb.append("53: 음식 서비스직\n");
        sb.append("54: 경호·경비직\n");
        sb.append("55: 돌봄 서비스직(간병·육아)\n");
        sb.append("56: 청소 및 기타 개인서비스직\n");
        sb.append("61: 영업·판매직\n");
        sb.append("62: 운전·운송직\n");
        sb.append("70: 건설·채굴직\n");
        sb.append("81: 기계 설치·정비·생산직\n");
        sb.append("82: 금속·재료 설치·정비·생산직(판금·단조·주조·용접·도장 등)\n");
        sb.append("83: 전기·전자 설치·정비·생산직\n");
        sb.append("84: 정보통신 설치·정비직\n");
        sb.append("85: 화학·환경 설치·정비·생산직\n");
        sb.append("86: 섬유·의복 생산직\n");
        sb.append("87: 식품 가공·생산직\n");
        sb.append("88: 인쇄·목재·공예 및 기타 설치·정비·생산직\n");
        sb.append("90: 농림어업직\n\n");
        
        sb.append("=== 매핑할 텍스트 목록 (직종명 또는 채용공고 제목) ===\n");
        texts.forEach(text -> sb.append("- ").append(text).append("\n"));
        sb.append("\n");
        
        sb.append("=== 출력 형식 ===\n");
        sb.append("반드시 다음 JSON 형식으로만 출력하세요:\n");
        sb.append("{\n");
        sb.append("  \"mappings\": [\n");
        sb.append("    {\"original\": \"의료\", \"middleName\": \"보건·의료직\"},\n");
        sb.append("    {\"original\": \"생산\", \"middleName\": \"기계 설치·정비·생산직\"},\n");
        sb.append("    {\"original\": \"IT개발\", \"middleName\": \"정보통신 연구개발직 및 공학기술직\"}\n");
        sb.append("  ]\n");
        sb.append("}\n\n");
        
        sb.append("=== 중요 규칙 ===\n");
        sb.append("1. **모든 텍스트는 반드시 위의 34개 중분류 중 하나로 분류해야 합니다**\n");
        sb.append("2. 채용공고 제목에서 업무 내용을 추론하여 가장 적합한 중분류를 선택하세요\n");
        sb.append("3. middleName은 정확히 일치하는 중분류명을 사용하세요 (띄어쓰기, 특수문자 포함)\n");
        sb.append("4. 애매하거나 불명확한 경우도 **가장 유사한 중분류를 선택**하세요\n");
        sb.append("5. middleName을 null로 설정하지 마세요 - 무조건 34개 중 하나를 선택하세요\n");
        sb.append("6. 여러 분야가 혼합된 경우 **가장 주된 업무**를 기준으로 선택하세요\n");
        sb.append("7. JSON 형식만 출력하고 다른 설명은 추가하지 마세요\n");
        sb.append("\n예시: '건설현장 안전관리자 모집' → '건설·채굴직'\n");
        sb.append("예시: '카페 직원 구함' → '음식 서비스직'\n");
        sb.append("예시: '간호조무사 급구' → '보건·의료직'\n");
        
        return sb.toString();
    }

    /**
     * OpenAI 응답 파싱
     */
    private Map<String, JobCategoryMapping> parseOpenAIResponse(String response) {
        try {
            // JSON 추출 (```json ... ``` 제거)
            String json = response;
            if (response.contains("```json")) {
                json = response.substring(response.indexOf("```json") + 7, response.lastIndexOf("```"));
            } else if (response.contains("```")) {
                json = response.substring(response.indexOf("```") + 3, response.lastIndexOf("```"));
            }
            json = json.trim();
            
            // 파싱
            Map<String, Object> result = objectMapper.readValue(json, new TypeReference<>() {});
            List<Map<String, String>> mappingsList = (List<Map<String, String>>) result.get("mappings");
            
            Map<String, JobCategoryMapping> mappings = new HashMap<>();
            for (Map<String, String> item : mappingsList) {
                JobCategoryMapping mapping = new JobCategoryMapping();
                mapping.setOriginal(item.get("original"));
                mapping.setMiddleName(item.get("middleName"));
                mappings.put(mapping.getOriginal(), mapping);
            }
            
            return mappings;
            
        } catch (Exception e) {
            log.error("OpenAI 응답 파싱 실패: {}", response, e);
            return new HashMap<>();
        }
    }

    /**
     * Job 테이블 업데이트 (직접 매퍼 호출)
     */
    public void updateJobCategory(Job job, JobCategoryResponse category) {
        job.setJobCategory1(category.getMajorName());
        job.setJobCategory2(category.getMiddleName());
        job.setJobCategory3(category.getJobName());
        job.setJobCode(category.getJobCode());
        
        // 직접 업데이트 (트랜잭션 없이)
        jobMapper.updateJob(job);
        log.debug("DB 업데이트 완료: JOB_NO={}, 중분류={}", job.getJobNo(), category.getMiddleName());
    }

    /**
     * 최종 통계 출력
     */
    private void printFinalStatistics() {
        List<Job> allJobs = jobMapper.findAll();
        long total = allJobs.size();
        long matched = allJobs.stream().filter(j -> j.getJobCategory1() != null).count();
        double percentage = (matched * 100.0) / total;
        
        log.info("=== 최종 통계 ===");
        log.info("전체 채용공고: {}건", total);
        log.info("매칭 성공: {}건 ({:.2f}%)", matched, percentage);
        log.info("매칭 실패: {}건", total - matched);
    }

    /**
     * OpenAI 매핑 결과 DTO
     */
    @lombok.Data
    private static class JobCategoryMapping {
        private String original;     // 원본 직종명
        private String middleName;   // KECO 중분류명
    }
}
