package com.seonier.web.view.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminViewController {

    @GetMapping("/adminmain")
    public String adminMain(HttpServletRequest request, Model model) {
        return "view/adminmain";
    }
}
