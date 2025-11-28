package com.seonier.web.view.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RegisterViewController extends AbstractViewController {

    // 일반 회원가입 페이지
    @GetMapping(path = "/register")
    public String register() {
        log.debug("User register page.");
        return "view/register";
    }

    // 업체(관리자) 회원가입 페이지
    @GetMapping(path = "/registeradmin")
    public String registerAdmin() {
        log.debug("Admin register page.");
        return "view/registeradmin";
    }
}