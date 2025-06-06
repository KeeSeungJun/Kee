package com.seonier.dto.request;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The example request
 *
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ToString
@JsonSerialize
public class LoginRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = -7278111829925276379L;

	@NotEmpty(message = "사용자 아이디는 필수입니다.")
	@JsonProperty("user_id")
	private String userId;

	@NotEmpty(message = "비밀번호는 필수입니다.")
	@Size(min = 6, max = 12, message = "비밀번호는 6 이상 12 이하 값만 입력 가능합니다.")
	@JsonProperty("passwd")
	private String passwd;

}
