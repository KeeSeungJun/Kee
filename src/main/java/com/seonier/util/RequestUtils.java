package com.seonier.util;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * The http request utils
 *
 * @version 1.0.1
 */
public class RequestUtils {

	private RequestUtils() throws IllegalAccessException {
		throw new IllegalAccessException("Access to class not allowed.");
	}

	/**
	 * Check if the browser is Internet Explorer.
	 *
	 * @param request the http servlet request
	 * @return true / false
	 */
	public static boolean isIE(final HttpServletRequest request) {
		return isIE(request.getHeader("User-Agent"));
	}

	/**
	 * Check if the browser is Internet Explorer.
	 *
	 * @param userAgent the http user-agent
	 * @return true / false
	 */
	public static boolean isIE(final String userAgent) {
		return !StringUtils.isBlank(userAgent) && StringUtils.containsAnyIgnoreCase(userAgent, "MSIE", "Trident");
	}

	/**
	 * Determine if authorization type is application json
	 *
	 * @param request the http servlet request
	 * @return true / false
	 */
	public static boolean isAcceptAsJson(final HttpServletRequest request) {
		return isAcceptAsJson(request.getHeader(HttpHeaders.ACCEPT));
	}

	/**
	 * Determine if authorization type is application json
	 *
	 * @param accept the http accept
	 * @return true / false
	 */
	public static boolean isAcceptAsJson(final String accept) {
		return StringUtils.startsWithIgnoreCase(accept, MediaType.APPLICATION_JSON_VALUE);
	}

}
