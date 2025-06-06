package com.seonier.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

/**
 * The default response
 *
 * @version 1.0.1
 * @see AbstractResponse
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSerialize
@SuppressWarnings("all")
public class DefaultResponse extends AbstractResponse {

	private static final long serialVersionUID = -3139663263648555722L;

	/**
	 * Result
	 */
	@JsonIgnore
	@Singular("put")
	private Map<String, Object> result;

	/**
	 * Getting Result
	 *
	 * @return the result map
	 */
	@JsonAnyGetter
	public Map<String, Object> get() {
		return this.result;
	}

}
