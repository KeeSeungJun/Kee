package com.seonier.web.rest.controller;

import com.seonier.persistence.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.seonier.service.UserService;
import com.seonier.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(HttpServletRequest request) {
        String userId = CookieUtils.getCookie(request, "USER_ID");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        User user = userService.getUserByUserId(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자 없음");
        }

        return ResponseEntity.ok(user);
    }
}
