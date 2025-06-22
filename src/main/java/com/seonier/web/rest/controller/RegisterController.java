package com.seonier.web.rest.controller;

import com.seonier.dto.request.RegisterRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.service.UserService;
import com.seonier.web.lang.AbstractController;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.validation.Valid;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RegisterController extends AbstractController {

    private final UserService userService;
    private final MessageSourceAccessor messageSource;

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public DefaultResponse register(HttpServletResponse response, @Valid @RequestBody RegisterRequest params, BindingResult errors) {
        checkForErrors(this.messageSource, params.getClass(), errors);
        log.debug("User register: {}", params);

        return userService.registerUser(response, params);
    }
}
