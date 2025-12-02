package com.seonier.service;

import com.seonier.persistence.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 일자리 위치 정보 업데이트 서비스
 * KakaoMap API를 이용하여 주소를 좌표로 변환 후 DB 저장
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JobLocationUpdateService {

    private final JobService jobService;
    private final KakaoMapService kakaoMapService;

    /**
     * 모든 일자리의 위치 정보 업데이트
     * (주소 → 위도/경도 변환)
     */
    @Transactional
    public void updateAllJobLocations() {
        log.info("일자리 위치 정보 업데이트 시작");

        List<Job> jobs = jobService.findAll();
        int successCount = 0;
        int failCount = 0;

        for (Job job : jobs) {
            // 이미 좌표가 있으면 스킵
            if (job.getJobLocationLat() != null && job.getJobLocationLon() != null) {
                log.debug("일자리 {}번: 이미 좌표 존재, 스킵", job.getJobNo());
                continue;
            }

            // 주소가 없으면 스킵
            String address = job.getJobWorkLocation();
            if (address == null || address.trim().isEmpty() || address.equals("-")) {
                log.warn("일자리 {}번: 주소 없음, 스킵", job.getJobNo());
                failCount++;
                continue;
            }

            try {
                // KakaoMap API 호출 (주소 → 좌표)
                Map<String, Double> coordinates = kakaoMapService.getCoordinates(address);

                if (coordinates != null) {
                    job.setJobLocationLat(coordinates.get("latitude"));
                    job.setJobLocationLon(coordinates.get("longitude"));
                    
                    // DB 업데이트
                    jobService.updateJobLocation(job);
                    successCount++;
                    log.info("일자리 {}번 좌표 업데이트 성공: {} → ({}, {})", 
                            job.getJobNo(), address, 
                            coordinates.get("latitude"), coordinates.get("longitude"));
                } else {
                    failCount++;
                    log.warn("일자리 {}번 좌표 변환 실패: {}", job.getJobNo(), address);
                }

                // API 호출 제한 방지 (초당 10건 제한)
                Thread.sleep(100);

            } catch (Exception e) {
                failCount++;
                log.error("일자리 {}번 처리 중 오류: {}", job.getJobNo(), e.getMessage(), e);
            }
        }

        log.info("일자리 위치 정보 업데이트 완료 - 성공: {}, 실패: {}, 전체: {}", 
                successCount, failCount, jobs.size());
    }

    /**
     * 특정 일자리의 위치 정보 업데이트
     *
     * @param jobNo 일자리 번호
     */
    @Transactional
    public boolean updateJobLocation(Integer jobNo) {
        Job job = jobService.findById(jobNo);
        if (job == null) {
            log.warn("일자리 {}번을 찾을 수 없습니다.", jobNo);
            return false;
        }

        String address = job.getJobWorkLocation();
        if (address == null || address.trim().isEmpty()) {
            log.warn("일자리 {}번: 주소 없음", jobNo);
            return false;
        }

        try {
            Map<String, Double> coordinates = kakaoMapService.getCoordinates(address);
            if (coordinates != null) {
                job.setJobLocationLat(coordinates.get("latitude"));
                job.setJobLocationLon(coordinates.get("longitude"));
                jobService.updateJobLocation(job);
                log.info("일자리 {}번 좌표 업데이트 성공", jobNo);
                return true;
            }
        } catch (Exception e) {
            log.error("일자리 {}번 좌표 업데이트 실패: {}", jobNo, e.getMessage(), e);
        }

        return false;
    }
}
