package com.seonier.web.lang;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Error controller implementation.
 *
 * @version 1.0.0
 */
@Slf4j
@Controller
public class CustomErrorController extends AbstractController implements ErrorController {

	private final MessageSourceAccessor messageSource;

	/**
	 * Custom error controller
	 *
	 * @param messageSource the message source.
	 */
	protected CustomErrorController(MessageSourceAccessor messageSource) {
		this.messageSource = messageSource;
	}

	@GetMapping(value = "${server.error.page:error}")
	public Object handleError(HttpServletRequest request) {
		String uri = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		int statusCode = 500;
		if (status != null) {
			statusCode = Integer.parseInt(status.toString());
		}
		log.error("{} (URL: {}, Status code: {}, Method: {})", HttpStatus.valueOf(statusCode).getReasonPhrase(), uri,
				statusCode, request.getMethod());
		return result(request, HttpStatus.valueOf(statusCode), statusCode, message(this.messageSource, statusCode));
	}
}
