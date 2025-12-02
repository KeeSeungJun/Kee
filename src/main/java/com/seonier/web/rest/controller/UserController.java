package com.seonier.web.rest.controller;

import com.seonier.persistence.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.seonier.service.UserService;
import com.seonier.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PutMapping("/me")
    public ResponseEntity<?> updateMyInfo(HttpServletRequest request, @RequestBody Map<String, Object> updates) {
        String userId = CookieUtils.getCookie(request, "USER_ID");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        try {
            boolean success = userService.updateUserInfo(userId, updates);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "회원 정보가 수정되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "회원 정보 수정에 실패했습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "서버 오류가 발생했습니다."));
        }
    }
}
