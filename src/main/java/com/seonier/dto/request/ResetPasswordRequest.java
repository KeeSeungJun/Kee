package com.seonier.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * 비밀번호 재설정 요청 DTO
 *
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ToString(exclude = "newPassword")
@JsonSerialize
public class ResetPasswordRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "전화번호는 필수입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리 숫자만 입력 가능합니다.")
    @JsonProperty("phone")
    private String phone;

    @NotEmpty(message = "새 비밀번호는 필수입니다.")
    @JsonProperty("newPassword")
    private String newPassword;

}
