package com.seonier.web.view.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RegisterViewController extends AbstractViewController {

    @GetMapping(path = "register")
    public String register() {
        log.debug("User register page.");
        return "view/register";
    }
}
