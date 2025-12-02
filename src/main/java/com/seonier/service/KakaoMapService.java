package com.seonier.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * KakaoMap API 서비스
 * 주소를 좌표(위도/경도)로 변환
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoMapService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.api.key:}")
    private String kakaoApiKey;

    private static final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String KAKAO_KEYWORD_API_URL = "https://dapi.kakao.com/v2/local/search/keyword.json";

    /**
     * 키워드(회사명 등)로 장소 검색하여 주소 반환
     *
     * @param keyword 검색 키워드 (예: "회사명")
     * @param region 지역 필터 (예: "대전") - null 가능
     * @return 주소 문자열 또는 null (검색 실패 시)
     */
    public String searchPlaceAddress(String keyword, String region) {
        if (keyword == null || keyword.trim().isEmpty()) {
            log.warn("검색 키워드가 비어있습니다.");
            return null;
        }

        try {
            // API 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);

            // API 요청 URL 생성
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(KAKAO_KEYWORD_API_URL)
                    .queryParam("query", keyword)
                    .queryParam("size", 5);  // 최대 5개 결과

            if (region != null && !region.trim().isEmpty()) {
                builder.queryParam("query", keyword + " " + region);
            }

            String url = builder.build().toUriString();

            // API 호출
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 응답 파싱
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode documents = root.path("documents");

            if (documents.isArray() && documents.size() > 0) {
                // 가장 관련성 높은 첫 번째 결과 사용
                for (int i = 0; i < documents.size(); i++) {
                    JsonNode place = documents.get(i);
                    String placeName = place.path("place_name").asText();
                    String addressName = place.path("address_name").asText();
                    String roadAddressName = place.path("road_address_name").asText();
                    
                    // 대전 지역 필터링 (region이 "대전"일 경우)
                    if (region != null && region.contains("대전") && 
                        !addressName.contains("대전") && !roadAddressName.contains("대전")) {
                        continue;  // 대전이 아니면 스킵
                    }

                    // 도로명 주소 우선, 없으면 지번 주소
                    String address = !roadAddressName.isEmpty() ? roadAddressName : addressName;
                    
                    if (!address.isEmpty()) {
                        log.info("키워드 '{}' 검색 성공: {} ({})", keyword, placeName, address);
                        return address;
                    }
                }
                
                log.warn("키워드 '{}' 검색 결과 중 {}에 해당하는 장소 없음", keyword, region);
                return null;
            } else {
                log.warn("키워드 '{}' 검색 실패: 결과 없음", keyword);
                return null;
            }

        } catch (Exception e) {
            log.error("키워드 '{}' 검색 중 오류 발생: {}", keyword, e.getMessage());
            return null;
        }
    }

    /**
     * 주소를 좌표로 변환
     *
     * @param address 주소 (예: "대전광역시 서구 유등로 507")
     * @return Map<"latitude", "longitude"> 또는 null (변환 실패 시)
     */
    public Map<String, Double> getCoordinates(String address) {
        if (address == null || address.trim().isEmpty()) {
            log.warn("주소가 비어있습니다.");
            return null;
        }

        try {
            // API 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoApiKey);

            // API 요청 URL 생성
            String url = UriComponentsBuilder.fromUriString(KAKAO_API_URL)
                    .queryParam("query", address)
                    .build()
                    .toUriString();

            // API 호출
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 응답 파싱
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode documents = root.path("documents");

            if (documents.isArray() && documents.size() > 0) {
                JsonNode firstResult = documents.get(0);
                double latitude = firstResult.path("y").asDouble();
                double longitude = firstResult.path("x").asDouble();

                Map<String, Double> coordinates = new HashMap<>();
                coordinates.put("latitude", latitude);
                coordinates.put("longitude", longitude);

                log.debug("주소 '{}' 좌표 변환 성공: ({}, {})", address, latitude, longitude);
                return coordinates;
            } else {
                log.warn("주소 '{}' 좌표 변환 실패: 결과 없음", address);
                return null;
            }

        } catch (Exception e) {
            log.error("주소 '{}' 좌표 변환 중 오류 발생: {}", address, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 두 지점 간의 거리 계산 (Haversine 공식, 단위: km)
     *
     * @param lat1 지점1 위도
     * @param lon1 지점1 경도
     * @param lat2 지점2 위도
     * @param lon2 지점2 경도
     * @return 거리 (km)
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
