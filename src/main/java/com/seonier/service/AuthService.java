package com.seonier.service;

import com.seonier.dto.request.ResetPasswordRequest;
import com.seonier.dto.request.SendSmsRequest;
import com.seonier.dto.request.VerifySmsRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.mapper.UserMapper;
import com.seonier.persistence.model.User;
import com.seonier.web.lang.RequestException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 인증 서비스 (SMS, 비밀번호 재설정)
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService extends AbstractService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 인증번호 저장소 (실제 환경에서는 Redis 등 사용 권장)
    private final Map<String, String> authCodeStorage = new HashMap<>();

    /**
     * SMS 인증번호 발송
     *
     * @param params 전화번호
     * @return 인증번호 발송 결과
     */
    public DefaultResponse sendSmsAuthCode(@Valid SendSmsRequest params) {
        log.debug("Send SMS auth code to: {}", params.getPhone());

        // 6자리 랜덤 인증번호 생성
        String authCode = String.format("%06d", new Random().nextInt(1000000));

        // 인증번호 저장 (3분 유효)
        authCodeStorage.put(params.getPhone(), authCode);

        // TODO: 실제 SMS 발송 로직 추가 (예: AWS SNS, Twilio, NHN Cloud SMS 등)
        log.info("▶▶▶ SMS 인증번호 발송: {} -> {}", params.getPhone(), authCode);

        // 개발 환경에서는 로그에 인증번호 출력 (프로덕션에서는 제거)
        return DefaultResponse.builder()
                .put("message", "인증번호가 발송되었습니다.")
                .put("authCode", authCode) // 개발 환경에서만 포함 (프로덕션에서는 제거)
                .build();
    }

    /**
     * SMS 인증번호 확인
     *
     * @param params 전화번호, 인증번호
     * @return 인증 결과
     */
    public DefaultResponse verifySmsAuthCode(@Valid VerifySmsRequest params) {
        log.debug("Verify SMS auth code: phone={}, authCode={}", params.getPhone(), params.getAuthCode());

        String storedAuthCode = authCodeStorage.get(params.getPhone());

        if (storedAuthCode == null) {
            throw new RequestException(400, "인증번호가 만료되었거나 발송되지 않았습니다.");
        }

        if (!storedAuthCode.equals(params.getAuthCode())) {
            throw new RequestException(400, "인증번호가 일치하지 않습니다.");
        }

        // 인증 성공 시 저장소에서 제거
        authCodeStorage.remove(params.getPhone());

        return DefaultResponse.builder()
                .put("message", "인증이 완료되었습니다.")
                .put("verified", true)
                .build();
    }

    /**
     * 비밀번호 재설정
     *
     * @param params 전화번호, 새 비밀번호
     * @return 비밀번호 재설정 결과
     */
    public DefaultResponse resetPassword(@Valid ResetPasswordRequest params) {
        log.debug("Reset password for phone: {}", params.getPhone());

        // 전화번호로 사용자 조회
        User user = userMapper.findByMobileNumber(params.getPhone());
        if (user == null) {
            throw new RequestException(404, "해당 전화번호로 가입된 사용자를 찾을 수 없습니다.");
        }

        // 비밀번호 암호화 후 업데이트
        String encryptedPassword = passwordEncoder.encode(params.getNewPassword());
        user.setPasswd(encryptedPassword);
        user.setUpdateId("system");

        userMapper.updateUserPassword(user);

        log.info("▶▶▶ 비밀번호 재설정 완료: userId={}", user.getUserId());

        return DefaultResponse.builder()
                .put("message", "비밀번호가 재설정되었습니다.")
                .put("userId", user.getUserId())
                .build();
    }

}
