package com.seonier.dto.request;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * The example request
 *
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ToString
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(NON_NULL)
public class ExampleRequest implements Serializable {

	@Serial
	private static final long serialVersionUID = -7278111829925276379L;

	/**
	 * Example ID
	 */
	@NotEmpty(message = "Example ID 파라미터 값은 필수입니다.")
	private String exampleId;

	/**
	 * Example name
	 */
	@NotEmpty(message = "Example Name 파라미터 값은 필수입니다.")
	@Size(min = 10, max = 32, message = "Example Name 파라미터는 10 이상 32 이하 값만 입력 가능합니다.")
	private String exampleName;

}
