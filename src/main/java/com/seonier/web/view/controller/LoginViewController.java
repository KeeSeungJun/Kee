package com.seonier.web.view.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginViewController extends AbstractViewController {

	@GetMapping(path = "login")
	public String login() {
		log.debug("User login page.");
		return "view/login";
	}

}
