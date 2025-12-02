package com.seonier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 이메일 인증번호 발송 요청 DTO
 *
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ToString
@JsonSerialize
public class SendSmsRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @JsonProperty("email")
    private String email;

}
