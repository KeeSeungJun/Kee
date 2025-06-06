package com.seonier.dto.response;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * The result error
 *
 * @version 1.0.1
 * @see AbstractResponse
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@SuperBuilder
@NoArgsConstructor
@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResultError extends AbstractResponse {

	@Serial
	private static final long serialVersionUID = -3139663263648555722L;

	/**
	 * Result error messages
	 */
	@JsonProperty("errors")
	private List<FieldError> errors;

	/**
	 * Add Field error
	 *
	 * @param field the field name
	 * @param message the error message
	 */
	public void add(final String field, final String message) {
		if (errors == null) {
			this.errors = new LinkedList<>();
		}
		this.errors.add(new FieldError(field, message));
	}

	/**
	 * Add Field error
	 *
	 * @param clazz the request class
	 * @param field the field name
	 * @param message the error message
	 */
	public void add(final Class<?> clazz, final String field, final String message) {
		if (errors == null) {
			this.errors = new LinkedList<>();
		}
		this.errors.add(new FieldError(clazz, field, message));
	}

	@Getter
	@JsonSerialize
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class FieldError implements Serializable {

		@Serial
		private static final long serialVersionUID = 1L;

		/**
		 * 파라미터명.
		 */
		@JsonProperty("field")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private final String field;

		/**
		 * 에러메시지.
		 */
		@JsonProperty("message")
		@JsonInclude(JsonInclude.Include.NON_NULL)
		private final String message;

		/**
		 * Field error
		 *
		 * @param field the field name
		 * @param message the error message
		 */
		public FieldError(String field, String message) {
			this.field = field;
			this.message = message;
		}

		/**
		 * Field error
		 *
		 * @param clazz the request class
		 * @param field the field name
		 * @param message the error message
		 */
		public FieldError(Class<?> clazz, String field, String message) {
			JsonProperty jsonProperty = null;
			try {
				// create Field object
				Field f = clazz.getDeclaredField(field);

				// apply getAnnotation()
				jsonProperty = f.getAnnotation(JsonProperty.class);
			} catch (NoSuchFieldException | SecurityException e) {
				// ignore..
			}
			if (jsonProperty != null) {
				this.field = jsonProperty.value();
			} else {
				this.field = field;
			}
			this.message = message;
		}

	}

}
