package com.seonier.web.lang;

import org.apache.commons.lang3.StringUtils;

import com.seonier.dto.response.ResultError;
import com.seonier.lang.AbstractObject;
import com.seonier.util.MessageUtils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Spring MVC abstract controller
 *
 * @version 1.0.1
 */
@Slf4j
@Component
public abstract class AbstractController extends AbstractObject {

	/**
	 * 항상 사용하는 기능이어서 공통으로 변경
	 */
	protected static String getUserIdFromCookies(HttpServletRequest request) {
		if (request.getCookies() == null || request.getCookies().length == 0) {
			return "admin";
		}
		for (Cookie cookie : request.getCookies()) {
		    if ("USER_ID".equals(cookie.getName())) {
		        log.debug("user_id: {}", cookie.getValue());
		        return cookie.getValue();
		    }
		}
//		return "admin";
		 throw new RequestException(401, "로그인 후 다시 이용해주세요.");
	}

	/**
	 * Return parameter error.
	 *
	 * @param messageSource the message source.
	 * @param result the binding result
	 */
	protected final void checkForErrors(final MessageSourceAccessor messageSource, final BindingResult result) {
		checkForErrors(messageSource, null, result, null);
	}

	/**
	 * Return parameter error.
	 *
	 * @param messageSource the message source.
	 * @param clazz the request class
	 * @param result the binding result
	 */
	protected final void checkForErrors(final MessageSourceAccessor messageSource, final Class<?> clazz,
			final BindingResult result) {
		checkForErrors(messageSource, clazz, result, null);
	}

	/**
	 * Return parameter error.
	 *
	 * @param messageSource the message source.
	 * @param clazz the request class
	 * @param result the binding result
	 * @param message the error message
	 */
	protected final void checkForErrors(final MessageSourceAccessor messageSource, final Class<?> clazz,
			final BindingResult result, final String message) {
		if (result == null || !result.hasErrors()) {
			return;
		}
		ResultError res = ResultError.builder().code(HttpStatus.PRECONDITION_FAILED.value()).message(message).build();
		if (StringUtils.isBlank(message)) {
			res.setMessage(messageSource.getMessage(MessageUtils.responseCode(HttpStatus.PRECONDITION_FAILED)));
		}
		for (FieldError error : result.getFieldErrors()) {
			log.debug("Field name : {}, Error message : {}", error.getField(), error.getDefaultMessage());
			if (clazz != null) {
				res.add(clazz, error.getField(), error.getDefaultMessage());
			} else {
				res.add(error.getField(), error.getDefaultMessage());
			}
		}
		throw new RequestException(res);
	}

}
