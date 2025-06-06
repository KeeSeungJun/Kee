package com.seonier.web.lang;

import java.io.Serial;

import com.seonier.dto.response.ResultError;

import lombok.Getter;

import org.springframework.http.HttpStatus;

/**
 * The response exception
 *
 * @version 1.0.1
 * @see RuntimeException
 */
@Getter
public class RequestException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 4410699395373348759L;

	/**
	 * HTTP status.
	 * -- GETTER --
	 * Getting http status.
	 */
	private final HttpStatus status;

	/**
	 * Result code.
	 * -- GETTER --
	 * Getting result code.
	 */
	private final int code;

	/**
	 * Result error
	 * -- GETTER --
	 * Getting result error
	 */
	private final ResultError result;

	/**
	 * Response exception.
	 */
	public RequestException() {
		super();
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
		this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status the http status.
	 */
	public RequestException(final HttpStatus status) {
		super();
		this.status = status;
		this.code = status.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param code the exception code.
	 */
	public RequestException(final int code) {
		super();
		this.status = HttpStatus.OK;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status the http status.
	 * @param code the exception code.
	 */
	public RequestException(final HttpStatus status, final int code) {
		super();
		this.status = status;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param message the exception message.
	 */
	public RequestException(final String message) {
		super(message);
		this.status = HttpStatus.OK;
		this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param code the exception code.
	 * @param message the exception message.
	 */
	public RequestException(final int code, final String message) {
		super(message);
		this.status = HttpStatus.OK;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status HttpStatus the http status.
	 * @param message the exception message.
	 */
	public RequestException(final HttpStatus status, final String message) {
		super(message);
		this.status = status;
		this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status the http status.
	 * @param code the exception code.
	 * @param message the exception message.
	 */
	public RequestException(final HttpStatus status, final int code, final String message) {
		super(message);
		this.status = status;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param cause the throwable.
	 */
	public RequestException(final Throwable cause) {
		super(cause);
		this.status = HttpStatus.INTERNAL_SERVER_ERROR;
		this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status the http status.
	 * @param cause the throwable.
	 */
	public RequestException(final HttpStatus status, final Throwable cause) {
		super(cause);
		this.status = status;
		this.code = status.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param code the exception code.
	 * @param cause the throwable.
	 */
	public RequestException(final int code, final Throwable cause) {
		super(cause);
		this.status = HttpStatus.OK;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status the http status.
	 * @param code the exception code.
	 * @param cause the throwable.
	 */
	public RequestException(final HttpStatus status, final int code, final Throwable cause) {
		super(cause);
		this.status = status;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param message the exception message.
	 * @param cause the throwable.
	 */
	public RequestException(final String message, final Throwable cause) {
		super(message, cause);
		this.status = HttpStatus.OK;
		this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param code the exception code.
	 * @param message the exception message.
	 * @param cause the throwable.
	 */
	public RequestException(final int code, final String message, final Throwable cause) {
		super(message, cause);
		this.status = HttpStatus.OK;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status HttpStatus the http status.
	 * @param message the exception message.
	 * @param cause the throwable.
	 */
	public RequestException(final HttpStatus status, final String message, final Throwable cause) {
		super(message, cause);
		this.status = status;
		this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param status the http status.
	 * @param code the exception code.
	 * @param message the exception message.
	 * @param cause the throwable.
	 */
	public RequestException(final HttpStatus status, final int code, final String message, final Throwable cause) {
		super(message, cause);
		this.status = status;
		this.code = code;
		this.result = null;
	}

	/**
	 * Response exception.
	 *
	 * @param result the object.
	 */
	public RequestException(final ResultError result) {
		super(result.getMessage());
		this.status = HttpStatus.valueOf(result.getCode());
		this.code = result.getCode();
		this.result = result;
	}

	/**
	 * Response exception.
	 *
	 * @param result the result error.
	 * @param cause Throwable.
	 */
	public RequestException(final ResultError result, final Throwable cause) {
		super(result.getMessage(), cause);
		this.status = HttpStatus.valueOf(result.getCode());
		this.code = result.getCode();
		this.result = result;
	}

}
