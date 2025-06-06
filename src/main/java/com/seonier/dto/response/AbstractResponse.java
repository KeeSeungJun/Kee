package com.seonier.dto.response;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The abstract response
 *
 * @version 1.0.1
 * @see Serializable
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = -4698005157675044667L;

	/**
	 * Result code
	 */
	@Builder.Default
	@JsonProperty("code")
	private int code = 200;

	/**
	 * Result message
	 */
	@Builder.Default
	@JsonProperty("message")
	private String message = "SUCCESS";

}
