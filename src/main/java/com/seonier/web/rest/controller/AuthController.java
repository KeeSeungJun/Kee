package com.seonier.web.rest.controller;

import com.seonier.dto.request.ResetPasswordRequest;
import com.seonier.dto.request.SendSmsRequest;
import com.seonier.dto.request.VerifySmsRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.service.AuthService;
import com.seonier.web.lang.AbstractController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 API Controller
 *
 * @version 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController extends AbstractController {

    private final AuthService authService;
    private final MessageSourceAccessor messageSource;

    /**
     * SMS 인증번호 발송
     *
     * @param params 전화번호
     * @return 인증번호 발송 결과
     */
    @PostMapping(path = "/send-sms", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse sendSms(@Valid @RequestBody SendSmsRequest params, BindingResult errors) {
        checkForErrors(this.messageSource, params.getClass(), errors);
        log.debug("Send SMS: {}", params);
        return authService.sendSmsAuthCode(params);
    }

    /**
     * SMS 인증번호 확인
     *
     * @param params 전화번호, 인증번호
     * @return 인증 결과
     */
    @PostMapping(path = "/verify-sms", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse verifySms(@Valid @RequestBody VerifySmsRequest params, BindingResult errors) {
        checkForErrors(this.messageSource, params.getClass(), errors);
        log.debug("Verify SMS: {}", params);
        return authService.verifySmsAuthCode(params);
    }

    /**
     * 비밀번호 재설정
     *
     * @param params 전화번호, 새 비밀번호
     * @return 비밀번호 재설정 결과
     */
    @PostMapping(path = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse resetPassword(@Valid @RequestBody ResetPasswordRequest params, BindingResult errors) {
        checkForErrors(this.messageSource, params.getClass(), errors);
        log.debug("Reset password: {}", params);
        return authService.resetPassword(params);
    }

}
