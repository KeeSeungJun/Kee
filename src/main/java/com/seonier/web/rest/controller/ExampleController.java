package com.seonier.web.rest.controller;

import java.util.List;

import com.seonier.dto.request.ExampleRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.web.lang.AbstractController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The spring mvc example controller
 *
 * @version 1.0.1
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class ExampleController extends AbstractController {

	private final MessageSourceAccessor messageSource;

	@GetMapping(path = "test/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DefaultResponse> getExample(HttpServletRequest ignoredRequest,
			HttpServletResponse ignoredResponse, @Valid ExampleRequest params, BindingResult errors) {
		checkForErrors(messageSource, params.getClass(), errors);
		log.debug("Example request : {}", params);
		// @formatter:off
		return ResponseEntity.ok().body(
				DefaultResponse.builder()
						.code(200)
						.message(message(messageSource, 200)).build()
		);
		// @formatter:on
	}

	@GetMapping(path = "test/get2", produces = MediaType.APPLICATION_JSON_VALUE)
	public DefaultResponse getExample2() {
		// @formatter:off
		log.debug("Test list : {}", List.of(
				"010-1234-5678",
				"01012345678",
				"02-1234-5678",
				"0212345678",
				"1980-11-11",
				"19801111"
		));
		return DefaultResponse.builder()
				.code(200)
				.message(message(messageSource, 200))
				.build();
		// @formatter:on
	}

}
