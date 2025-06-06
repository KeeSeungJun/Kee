package com.seonier.web.rest.controller;

import com.seonier.dto.request.LoginRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.service.UserService;
import com.seonier.web.lang.AbstractController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class LoginController extends AbstractController {

	private final MessageSourceAccessor messageSource;

	private final UserService userService;

	@PostMapping(path = "login", produces = MediaType.APPLICATION_JSON_VALUE)
	public DefaultResponse login(HttpServletResponse response, @Valid @RequestBody LoginRequest params, BindingResult errors) {
		checkForErrors(this.messageSource, params.getClass(), errors);
		log.debug("User login: {}", params);

		// 사용자 테이블 조회/
		return userService.getLoginCheck(response, params);
	}

}
