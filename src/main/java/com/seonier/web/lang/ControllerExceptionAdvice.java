package com.seonier.web.lang;

import org.apache.commons.lang3.StringUtils;

import com.seonier.lang.AbstractObject;
import com.seonier.util.MessageUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * The controller exception advice.
 *
 * @version 1.0.1
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice extends AbstractObject {

	private final MessageSourceAccessor messageSource;

	/**
	 * Controller exception advice
	 *
	 * @param messageSource the message source.
	 */
	private ControllerExceptionAdvice(MessageSourceAccessor messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * HTTP Status 400 에러 처리.
	 *
	 * @param request the http servlet request
	 * @param error the throwable
	 * @return the response entity
	 * @see BindException
	 * @see HttpMessageNotReadableException
	 * @see MethodArgumentNotValidException
	 * @see MissingServletRequestParameterException
	 * @see MissingServletRequestPartException
	 * @see ServletRequestBindingException
	 * @see TypeMismatchException
	 */
	// @formatter:off
	@ExceptionHandler({
			BindException.class,
			HttpMessageNotReadableException.class,
			MethodArgumentNotValidException.class,
			MissingServletRequestParameterException.class,
			MissingServletRequestPartException.class,
			ServletRequestBindingException.class,
			TypeMismatchException.class
	})
	// @formatter:on
	private Object handleBadRequestException(final HttpServletRequest request, final Throwable error) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		log.warn("{} (URL: {}, Status code: {}, Method: {})", error.getMessage(), request.getRequestURI(),
				getCode(status), request.getMethod());
		return result(request, status, getCode(status), message(this.messageSource, status.value()));
	}

	/**
	 * HTTP Status 404 에러 처리.
	 *
	 * @param request the http servlet request
	 * @param error the throwable
	 * @return the response entity
	 * @see NoHandlerFoundException
	 * @see NoResourceFoundException
	 */
	// @formatter:off
	@ExceptionHandler({
			NoHandlerFoundException.class,
			NoResourceFoundException.class
	})
	// @formatter:on
	private Object handleNotFoundException(final HttpServletRequest request, final ServletException error) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		log.warn("{} (URL: {}, Status code: {}, Method: {})", error.getMessage(), request.getRequestURI(),
				getCode(status), request.getMethod());
		return result(request, status, getCode(status), message(this.messageSource, status.value()));
	}

	/**
	 * HTTP Status 405 에러 처리.
	 *
	 * @param request the http servlet request
	 * @param error the throwable
	 * @return the response entity
	 * @see HttpRequestMethodNotSupportedException
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	private Object handleMethodNotAllowedException(final HttpServletRequest request, final ServletException error) {
		HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
		log.warn("{} (URL: {}, Status code: {}, Method: {})", error.getMessage(), request.getRequestURI(),
				getCode(status), request.getMethod());
		return result(request, status, getCode(status), message(this.messageSource, status.value()));
	}

	/**
	 * 지원하지 않는 미디어 타입 에러.
	 *
	 * @param request the http servlet request
	 * @param error the throwable
	 * @return the response entity
	 * @see HttpMediaTypeNotSupportedException
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	private Object handleHttpMediaTypeNotSupportedException(HttpServletRequest request, ServletException error) {
		log.warn(error.getMessage(), error);
		HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
		return result(request, status, getCode(status), message(this.messageSource, status.value()));
	}

	/**
	 * Request Parameter Error
	 *
	 * @param request the http servlet request
	 * @param error the http request method not supported exception
	 * @return the response entity
	 * @see RequestException
	 */
	@ExceptionHandler(RequestException.class)
	private Object handleRequestException(final HttpServletRequest request, final RequestException error) {
		if (error.getResult() != null) {
			log.error("{} (URL: {}, Status code: {}, Method: {})", error.getMessage(), request.getRequestURI(),
					getCode(error.getStatus()), request.getMethod());
			return result(request, error.getStatus(), error.getResult());
		}
		HttpStatus status = error.getStatus() != null ? error.getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
		log.error("{} (URL: {}, Status code: {}, Method: {})", error.getMessage(), request.getRequestURI(),
				getCode(status), request.getMethod());

		String message = StringUtils.isNotBlank(error.getMessage()) ?
				error.getMessage() :
				MessageUtils.response(this.messageSource, error.getCode());
		return result(request, status, error.getCode(), message);
	}

	/**
	 * Runtime Error.
	 *
	 * @param request the http servlet request
	 * @param error the throwable
	 * @return the response entity
	 * @see IllegalArgumentException
	 * @see IllegalStateException
	 * @see NullPointerException
	 */
	// @formatter:off
	@ExceptionHandler({
			IllegalArgumentException.class,
			IllegalStateException.class,
			NullPointerException.class
	})
	// @formatter:on
	private Object handleRuntimeException(final HttpServletRequest request, final RuntimeException error) {
		log.error(error.getMessage(), error);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return result(request, status, getCode(status), MessageUtils.response(this.messageSource, status.value()));
	}

	/**
	 * Internal Server Error
	 *
	 * @param request the http servlet request
	 * @param error the throwable
	 * @return the response entity
	 * @see Throwable
	 */
	@ExceptionHandler(Throwable.class)
	private Object handleException(final HttpServletRequest request, final Throwable error) {
		log.error(error.getMessage(), error);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return result(request, status, getCode(status), MessageUtils.response(this.messageSource, status.value()));
	}

	/**
	 * Http status code.
	 *
	 * @param httpStatus the http status.
	 * @return the http status code.
	 */
	private int getCode(HttpStatus httpStatus) {
		return httpStatus.value();
	}

}
