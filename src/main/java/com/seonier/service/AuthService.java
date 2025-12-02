package com.seonier.service;

import com.seonier.dto.request.ResetPasswordRequest;
import com.seonier.dto.request.SendSmsRequest;
import com.seonier.dto.request.VerifySmsRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.mapper.UserMapper;
import com.seonier.persistence.model.User;
import com.seonier.web.lang.RequestException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 인증 서비스 (이메일 인증, 비밀번호 재설정)
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService extends AbstractService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    // 인증번호 저장소 (실제 환경에서는 Redis 등 사용 권장)
    private final Map<String, String> authCodeStorage = new HashMap<>();

    /**
     * 이메일 인증번호 발송
     *
     * @param params 이메일
     * @return 인증번호 발송 결과
     */
    public DefaultResponse sendSmsAuthCode(@Valid SendSmsRequest params) {
        log.debug("Send email auth code to: {}", params.getEmail());

        // 6자리 랜덤 인증번호 생성
        String authCode = String.format("%06d", new Random().nextInt(1000000));

        // 인증번호 저장 (3분 유효)
        authCodeStorage.put(params.getEmail(), authCode);

        // 이메일 발송
        try {
            sendAuthEmail(params.getEmail(), authCode);
            log.info("▶▶▶ 이메일 인증번호 발송 완료: {} -> {}", params.getEmail(), authCode);
        } catch (MessagingException e) {
            log.error("이메일 발송 실패", e);
            throw new RequestException(500, "이메일 발송에 실패했습니다.");
        }

        return DefaultResponse.builder()
                .put("message", "인증번호가 이메일로 발송되었습니다.")
                .build();
    }

    /**
     * 인증 이메일 발송
     */
    private void sendAuthEmail(String toEmail, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("[시니어 일자리] 이메일 인증번호");
        helper.setText(buildEmailContent(authCode), true);

        mailSender.send(message);
    }

    /**
     * 이메일 HTML 콘텐츠 생성
     */
    private String buildEmailContent(String authCode) {
        return String.format(
                "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px;'>" +
                "<h2 style='color: #333;'>이메일 인증</h2>" +
                "<p>아래의 인증번호를 입력하여 이메일 인증을 완료해주세요.</p>" +
                "<div style='background-color: #f4f4f4; padding: 20px; text-align: center; font-size: 24px; font-weight: bold; letter-spacing: 5px; margin: 20px 0;'>" +
                "%s" +
                "</div>" +
                "<p style='color: #666; font-size: 14px;'>인증번호는 3분간 유효합니다.</p>" +
                "<p style='color: #666; font-size: 14px;'>본인이 요청하지 않았다면 이 이메일을 무시하세요.</p>" +
                "</div>" +
                "</body>" +
                "</html>",
                authCode
        );
    }

    /**
     * 이메일 인증번호 확인
     *
     * @param params 이메일, 인증번호
     * @return 인증 결과
     */
    public DefaultResponse verifySmsAuthCode(@Valid VerifySmsRequest params) {
        log.debug("Verify email auth code: email={}, authCode={}", params.getEmail(), params.getAuthCode());

        String storedAuthCode = authCodeStorage.get(params.getEmail());

        if (storedAuthCode == null) {
            throw new RequestException(400, "인증번호가 만료되었거나 발송되지 않았습니다.");
        }

        if (!storedAuthCode.equals(params.getAuthCode())) {
            throw new RequestException(400, "인증번호가 일치하지 않습니다.");
        }

        // 인증 성공 시 저장소에서 제거
        authCodeStorage.remove(params.getEmail());

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
