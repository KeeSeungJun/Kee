package com.seonier.service;

import com.seonier.persistence.mapper.JobMapper;
import com.seonier.persistence.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 고용24 사이트 스크래핑 서비스
 * 매일 오전 9시에 대전지역 채용공고를 스크래핑하여 DB에 저장
 * 마감일이 지난 채용공고는 자동 삭제
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Employment24ScraperService {

    private final JobMapper jobMapper;
    
    // 고용24 대전지역 채용공고 목록 페이지 URL
    private static final String EMPLOYMENT24_LIST_URL = 
        "https://www.work24.go.kr/wk/a/b/1200/retriveDtlEmpSrchList.do?region=30000&sortField=DATE&sortOrderBy=DESC&resultCnt=50&currentPageNo=";
    
    // 병렬 처리를 위한 스레드 풀 크기 (동시에 처리할 작업 수)
    // 서버 부하를 고려하여 3~5개 정도가 적당
    private static final int THREAD_POOL_SIZE = 3;
    
    /**
     * 애플리케이션 시작 시 즉시 실행 (테스트용)
     * 테스트 완료 후에는 주석 처리하여 비활성화
     */
    // @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void scrapeOnStartup() {
        log.info("애플리케이션 시작 - 즉시 스크래핑 실행");
        scrapeAndSaveJobs();
    }
    
    /**
     * 매일 오전 9시에 실행되는 스케줄러
     * cron 표현식: 초 분 시 일 월 요일
     * "0 0 9 * * *" = 매일 오전 9시 0분 0초
     */
    @Scheduled(cron = "0 0 9 * * *")
    @Transactional
    public void scrapeAndSaveJobs() {
        log.info("=====================================================");
        log.info("고용24 스크래핑 시작: {}", LocalDateTime.now());
        log.info("=====================================================");
        
        try {
            // 1. 마감일이 지난 채용공고 삭제
            deleteExpiredJobs();
            
            // 2. 새로운 채용공고 스크래핑
            List<Job> scrapedJobs = scrapeEmployment24();
            
            if (scrapedJobs.isEmpty()) {
                log.warn("스크래핑된 채용공고가 없습니다.");
                return;
            }
            
            // 3. DB에 저장 (중복 체크)
            int savedCount = 0;
            int duplicateCount = 0;
            
            for (Job job : scrapedJobs) {
                try {
                    // 중복 체크: 기업명 + 채용공고 제목
                    if (job.getJobCompanyName() != null && job.getJobTitle() != null) {
                        int count = jobMapper.countByCompanyAndTitle(job.getJobCompanyName(), job.getJobTitle());
                        if (count > 0) {
                            duplicateCount++;
                            log.debug("중복 채용공고 스킵: {} - {}", job.getJobCompanyName(), job.getJobTitle());
                            continue;
                        }
                    }
                    
                    jobMapper.insertJob(job);
                    savedCount++;
                } catch (Exception e) {
                    log.error("채용공고 저장 실패: {}", job.getJobTitle(), e);
                }
            }
            
            log.info("=====================================================");
            log.info("고용24 스크래핑 완료");
            log.info("- 총 조회: {}건", scrapedJobs.size());
            log.info("- 신규 저장: {}건", savedCount);
            log.info("- 중복 스킵: {}건", duplicateCount);
            log.info("=====================================================");
            
        } catch (Exception e) {
            log.error("고용24 스크래핑 중 오류 발생", e);
        }
    }
    
    /**
     * 마감일이 지난 채용공고 삭제
     */
    private void deleteExpiredJobs() {
        try {
            LocalDate today = LocalDate.now();
            int deletedCount = jobMapper.deleteExpiredJobs(today);
            log.info("마감일이 지난 채용공고 {}건 삭제 완료 (기준일: {})", deletedCount, today);
        } catch (Exception e) {
            log.error("만료 채용공고 삭제 실패", e);
        }
    }
    
    /**
     * 고용24 사이트에서 채용공고 스크래핑 (페이지네이션 + 병렬 처리)
     */
    private List<Job> scrapeEmployment24() {
        List<Job> jobs = new CopyOnWriteArrayList<>();  // 스레드 안전한 리스트
        int currentPage = 1;
        
        // 병렬 처리를 위한 ExecutorService 생성
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        try {
            while (true) {
                log.info("{}페이지 스크래핑 시작", currentPage);
                
                String pageUrl = EMPLOYMENT24_LIST_URL + currentPage;
                
                // 목록 페이지 로드
                Document listDoc = Jsoup.connect(pageUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(30000)
                        .get();
                
                // 병렬 처리를 위한 Future 리스트
                List<Future<Job>> futures = new ArrayList<>();
                
                // 1부터 50까지의 채용공고를 병렬로 처리
                for (int i = 1; i <= 50; i++) {
                    final int index = i;
                    Future<Job> future = executorService.submit(() -> {
                        try {
                            return scrapeJobFromList(listDoc, index);
                        } catch (Exception e) {
                            log.error("채용공고 #{} 파싱 중 오류", index, e);
                            return null;
                        }
                    });
                    futures.add(future);
                }
                
                // 모든 작업 완료 대기 및 결과 수집
                int jobsFoundOnPage = 0;
                for (Future<Job> future : futures) {
                    try {
                        Job job = future.get(60, TimeUnit.SECONDS);  // 최대 60초 대기
                        if (job != null) {
                            jobs.add(job);
                            jobsFoundOnPage++;
                        }
                    } catch (TimeoutException e) {
                        log.error("채용공고 처리 시간 초과", e);
                        future.cancel(true);
                    } catch (Exception e) {
                        log.error("채용공고 결과 수집 중 오류", e);
                    }
                }
                
                log.info("{}페이지에서 {}건 수집 (누적: {}건)", currentPage, jobsFoundOnPage, jobs.size());
                
                // 이 페이지에서 채용공고를 하나도 찾지 못했으면 종료
                if (jobsFoundOnPage == 0) {
                    log.info("더 이상 채용공고가 없습니다. 스크래핑 종료");
                    break;
                }
                
                currentPage++;
                
                // 안전장치: 최대 100페이지까지만
                if (currentPage > 100) {
                    log.warn("최대 페이지 수 도달. 스크래핑 종료");
                    break;
                }
                
                // 서버 부하 방지를 위한 딜레이 (2초로 축소)
                Thread.sleep(2000);
            }
            
        } catch (InterruptedException e) {
            log.error("스크래핑 중 인터럽트 발생", e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("고용24 페이지 로드 실패", e);
        } finally {
            // ExecutorService 종료
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        
        log.info("전체 스크래핑 완료: 총 {}건 수집", jobs.size());
        return jobs;
    }
    
    /**
     * 목록 페이지에서 채용공고 기본 정보 추출 및 상세 페이지 크롤링
     * @param listDoc 목록 페이지 Document
     * @param index 리스트 인덱스 (1~50)
     */
    private Job scrapeJobFromList(Document listDoc, int index) {
        try {
            // 기본 정보 추출 (목록 페이지)
            String listSelector = "#list" + index;
            Element listItem = listDoc.selectFirst(listSelector);
            
            if (listItem == null) {
                return null;
            }
            
            // 채용공고 제목 및 상세 페이지 URL
            String titleSelector = listSelector + " > td.al_left.pd24 > div > div:nth-child(2) > a";
            Element titleLink = listDoc.selectFirst(titleSelector);
            if (titleLink == null) {
                return null;
            }
            
            String jobTitle = titleLink.text().trim();
            String detailUrl = titleLink.attr("abs:href");
            
            if (jobTitle.isEmpty() || detailUrl.isEmpty()) {
                return null;
            }
            
            // 기업 정보
            String companySelector = listSelector + " > td.al_left.pd24 > div > div:nth-child(1) > div > label > span > a";
            String companyName = extractTextSafely(listDoc, companySelector);
            
            // D-Day
            String ddaySelector = "#dDayInfo" + (index - 1);  // dDayInfo0, dDayInfo1, ...
            String dday = extractTextSafely(listDoc, ddaySelector);
            
            // 마감일
            String deadlineSelector = listSelector + " > td:nth-child(3) > p:nth-child(3)";
            String deadlineText = extractTextSafely(listDoc, deadlineSelector);
            LocalDate deadline = parseDeadline(deadlineText);
            
            // 급여 미리보기
            String salaryPreviewSelector = listSelector + " > td.link.pd24 > div > ul > li.dollar > p > span";
            String salaryPreview = extractTextSafely(listDoc, salaryPreviewSelector);
            
            log.debug("발견된 채용공고: {} ({}) - 급여: {}", jobTitle, companyName, salaryPreview);
            
            // 상세 페이지 크롤링 (1초 딜레이 - 서버 부하 고려)
            Thread.sleep(1000);
            Job job = scrapeDetailPage(detailUrl, jobTitle, companyName, dday, deadline, salaryPreview);
            
            return job;
            
        } catch (Exception e) {
            log.error("목록 페이지 파싱 중 오류 (index: {})", index, e);
            return null;
        }
    }
    
    /**
     * 상세 페이지에서 추가 정보 크롤링
     */
    private Job scrapeDetailPage(String detailUrl, String jobTitle, String companyName, String dday, LocalDate deadline, String salaryPreview) {
        try {
            Document detailDoc = Jsoup.connect(detailUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(30000)
                    .get();
            
            // 상세 정보 추출
            String baseSelector = "#contents > div > div.cont_wrap_area > div.tab_wrap.line_type.mt40";
            
            // 모집 조건 섹션 (div:nth-child(2))
            String recruitSectionSelector = baseSelector + " > div:nth-child(2) > div.box_table_wrap.write.mt16 > table > tbody";
            String position = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(1) > td:nth-child(2)");
            String recruitCount = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(1) > td:nth-child(4)");
            String experience = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(2) > td:nth-child(2)");
            String education = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(2) > td:nth-child(4)");
            String employmentType = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(3) > td:nth-child(2)");
            String workLocation = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(3) > td:nth-child(4)");
            String salary = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(4) > td:nth-child(2)");
            String rank = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(4) > td:nth-child(4)");
            String preference = extractTextSafely(detailDoc, recruitSectionSelector + " > tr:nth-child(5) > td");
            
            // 근무 조건 섹션 (div:nth-child(3))
            String workSectionSelector = baseSelector + " > div:nth-child(3) > div > table > tbody";
            String workType = extractTextSafely(detailDoc, workSectionSelector + " > tr:nth-child(1) > td:nth-child(2)");
            String workHours = extractTextSafely(detailDoc, workSectionSelector + " > tr:nth-child(1) > td:nth-child(4)");
            String nearbyStation = extractTextSafely(detailDoc, workSectionSelector + " > tr:nth-child(3) > td");
            
            // JOB_POSITION에 숫자가 포함되어 있으면 JOB_RECRUIT_COUNT로 이동
            if (position != null && position.matches(".*\\d+.*")) {
                // position에 숫자가 있으면 recruitCount로 이동
                if (recruitCount == null || recruitCount.isEmpty() || recruitCount.equals("-")) {
                    recruitCount = position;
                }
                position = null; // position은 비움
            }
            
            // Job 객체 생성
            return Job.builder()
                    .usrId("EMPLOYMENT24")
                    .jobTitle(jobTitle)
                    .jobCompanyName(companyName)
                    .jobDetailUrl(detailUrl)
                    .jobDday(dday)
                    .jobDeadline(deadline)
                    .jobSalaryPreview(salaryPreview)
                    .jobPosition(position)
                    .jobRecruitCount(recruitCount)
                    .jobExperience(experience)
                    .jobEducation(education)
                    .jobEmploymentType(employmentType)
                    .jobWorkLocation(workLocation)
                    .jobSalary(salary)
                    .jobRank(rank)
                    .jobPreference(preference)
                    .jobWorkType(workType)
                    .jobWorkHours(workHours)
                    .jobNearbyStation(nearbyStation)
                    .build();
            
        } catch (Exception e) {
            log.error("상세 페이지 크롤링 실패: {}", detailUrl, e);
            return null;
        }
    }
    
    /**
     * 마감일 텍스트 파싱 ("마감일 : 2025-12-01" 형식)
     */
    private LocalDate parseDeadline(String deadlineText) {
        try {
            if (deadlineText == null || deadlineText.isEmpty()) {
                return null;
            }
            
            // "마감일 : 2025-12-01" 형식에서 날짜 부분만 추출
            String dateStr = deadlineText.replace("마감일", "").replace(":", "").trim();
            
            if (dateStr.isEmpty() || dateStr.equals("-")) {
                return null;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateStr, formatter);
            
        } catch (Exception e) {
            log.warn("마감일 파싱 실패: {}", deadlineText);
            return null;
        }
    }
    
    /**
     * 안전하게 텍스트 추출
     */
    private String extractTextSafely(Document doc, String selector) {
        try {
            Element element = doc.selectFirst(selector);
            return element != null ? element.text().trim() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
