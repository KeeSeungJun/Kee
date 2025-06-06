package com.seonier.util;

import org.apache.commons.lang3.StringUtils;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;

/**
 * Message source utils.
 *
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class MessageUtils {

	private static final String MESSAGE_KEY_PREFIX = "message.response";

	private MessageUtils() throws IllegalAccessException {
		throw new IllegalAccessException("access to class not allowed.");
	}

	/**
	 * Http Status OK Message Key.
	 *
	 * @return the response message key.
	 */
	public static String responseCode() {
		return StringUtils.join(MESSAGE_KEY_PREFIX, ".", HttpStatus.OK.value());
	}

	/**
	 * Http Status Message Key.
	 *
	 * @param status the integer status.
	 * @return the response message key.
	 */
	public static String responseCode(int status) {
		return StringUtils.join(MESSAGE_KEY_PREFIX, ".", status);
	}

	/**
	 * Http Status Message Key.
	 *
	 * @param status the string status.
	 * @return the response message key.
	 */
	public static String responseCode(String status) {
		return StringUtils.join(MESSAGE_KEY_PREFIX, ".", status);
	}

	/**
	 * Http Status Message Key.
	 *
	 * @param status the http status.
	 * @return the response message key.
	 */
	public static String responseCode(HttpStatus status) {
		return StringUtils.join(MESSAGE_KEY_PREFIX, ".", status.value());
	}

	/**
	 * Response message.
	 *
	 * @param messageSource the message source accessor.
	 * @param code the message source code.
	 * @return the message.
	 */
	public static String response(MessageSourceAccessor messageSource, int code) {
		return messageSource.getMessage(responseCode(code));
	}

	/**
	 * Response message.
	 *
	 * @param messageSource the message source accessor.
	 * @param code the message source code.
	 * @return the message.
	 */
	public static String response(MessageSourceAccessor messageSource, String code) {
		return messageSource.getMessage(responseCode(code));
	}

	/**
	 * Response message.
	 *
	 * @param messageSource the message source accessor.
	 * @param code the message source code.
	 * @param args arguments for the message, or {@code null} if none.
	 * @return the message.
	 */
	public static String response(MessageSourceAccessor messageSource, int code, Object... args) {
		return messageSource.getMessage(responseCode(code), args);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 *
	 * @param messageSource the message source accessor.
	 * @param code the message source code.
	 * @return the message.
	 */
	public static String message(MessageSourceAccessor messageSource, String code) {
		return messageSource.getMessage(code);
	}

	/**
	 * Retrieve the message for the given code and the default Locale.
	 *
	 * @param messageSource the message source accessor.
	 * @param code the message source code.
	 * @param args arguments for the message, or {@code null} if none.
	 * @return the message.
	 */
	public static String message(MessageSourceAccessor messageSource, String code, Object... args) {
		return messageSource.getMessage(code, args);
	}

}
