package com.seonier.lang;

import org.apache.commons.lang3.StringUtils;

import com.seonier.dto.response.ResultError;
import com.seonier.util.MessageUtils;
import com.seonier.util.RequestUtils;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
@SuppressWarnings("unused")
public abstract class AbstractObject {

	private static final String badRequestPage = "error/400";

	private static final String unauthorizedPage = "error/401";

	private static final String paymentRequiredViewPage = "error/402";

	private static final String forbiddenPage = "error/403";

	private static final String notFoundPage = "error/404";

	private static final String methodNotAllowedPage = "error/405";

	private static final String defaultErrorPage = "error/error";

	/**
	 * Response message.
	 *
	 * @param messageSource the message source.
	 * @param code the message source code.
	 * @return the message.
	 */
	protected final String message(final MessageSourceAccessor messageSource, final int code) {
		return MessageUtils.response(messageSource, code);
	}

	/**
	 * Response message.
	 *
	 * @param messageSource the message source.
	 * @param code the message source code.
	 * @param args arguments for the message, or {@code null} if none.
	 * @return the message.
	 */
	protected final String message(final MessageSourceAccessor messageSource, final int code, Object... args) {
		return MessageUtils.response(messageSource, code, args);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request) {
		return result(request, HttpStatus.OK, null, null, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status) {
		return result(request, status, null, null, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param obj the result object
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, Object obj) {
		return result(request, HttpStatus.OK, obj, null, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param obj the result object
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, Object obj) {
		return result(request, status, obj, null, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param code the result code
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final int code) {
		return result(request, HttpStatus.OK, null, code, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param obj the result object
	 * @param code the result code
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, Object obj, final int code) {
		return result(request, HttpStatus.OK, obj, code, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param code the result code
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, final int code) {
		return result(request, status, null, code, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param obj the result object
	 * @param code the result code
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, Object obj,
			final int code) {
		return result(request, status, obj, code, null, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param code the result code
	 * @param message the result message
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final Integer code, final String message) {
		return result(request, HttpStatus.OK, null, code, message, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param obj the result object
	 * @param code the result code
	 * @param message the result message
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, Object obj, final Integer code,
			final String message) {
		return result(request, HttpStatus.OK, obj, code, message, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param code the result code
	 * @param message the result message
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, final Integer code,
			final String message) {
		return result(request, status, null, code, message, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param obj the result object
	 * @param code the result code
	 * @param message the result message
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, Object obj,
			final Integer code, final String message) {
		return result(request, status, obj, code, message, null);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param view the model and view
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final String view) {
		return result(request, HttpStatus.OK, null, null, null, view);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param view the model and view
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, final String view) {
		return result(request, status, null, null, null, view);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param code the result code
	 * @param message the result message
	 * @param view the model and view
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final Integer code, final String message,
			final String view) {
		return result(request, HttpStatus.OK, null, code, message, view);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param code the result code
	 * @param message the result message
	 * @param view the model and view
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, final Integer code,
			final String message, final String view) {
		return result(request, status, null, code, message, view);
	}

	/**
	 * Result model and view or restful
	 *
	 * @param request the http servlet request
	 * @param status the http status
	 * @param obj the result object
	 * @param code the result code
	 * @param message the result message
	 * @param view the model and view
	 * @return error page or restful
	 */
	protected final Object result(final HttpServletRequest request, final HttpStatus status, final Object obj,
			final Integer code, final String message, final String view) {
		if (RequestUtils.isAcceptAsJson(request)) {
			HttpHeaders headers = new HttpHeaders();
			if (RequestUtils.isIE(request)) {
				headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
			} else {
				headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			}
			if (obj != null) {
				return ResponseEntity.status(status).headers(headers).body(obj);
			}
			ResultError result = ResultError.builder().code(status.value()).message(status.getReasonPhrase()).build();
			if (code != null) {
				result.setCode(code);
			}
			if (StringUtils.isNotBlank(message)) {
				result.setMessage(message);
			}
			return ResponseEntity.status(status).headers(headers).body(result);
		}
		if (StringUtils.isNotBlank(view)) {
			return new ModelAndView(view);
		} else if (code != null) {
			return new ModelAndView(getView(code), status);
		}
		return new ModelAndView(getView(status.value()), status);
	}

	/**
	 * Getting error view.
	 *
	 * @param statusCode the http status code.
	 * @return the error view.
	 */
	private String getView(int statusCode) {
		try {
			return switch (HttpStatus.valueOf(statusCode)) {
				case BAD_REQUEST -> badRequestPage;
				case UNAUTHORIZED -> unauthorizedPage;
				case PAYMENT_REQUIRED -> paymentRequiredViewPage;
				case FORBIDDEN -> forbiddenPage;
				case NOT_FOUND -> notFoundPage;
				case METHOD_NOT_ALLOWED -> methodNotAllowedPage;
				default -> defaultErrorPage;
			};
		} catch (Exception e) {
			return defaultErrorPage;
		}
	}

}
