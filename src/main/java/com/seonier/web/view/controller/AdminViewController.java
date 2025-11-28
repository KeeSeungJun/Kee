package com.seonier.web.view.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminViewController {

    @GetMapping("/adminmain")
    public String adminMain(HttpServletRequest request, Model model) {
        return "view/adminmain";
    }

    @GetMapping("/accDelAdmin")
    public String accDeleteAdmin() {
        log.debug("Access the Admin Delete Account page.");
        return "view/accDelAdmin";
    }
    @GetMapping("/resumeAdmin")
    public String resumeAdmin() {
        log.debug("Access the Resume Admin page.");
        return "view/resumeAdmin";
    }
}