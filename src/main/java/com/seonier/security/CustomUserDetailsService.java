//package com.seonier.security;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.seonier.persistence.mapper.UserMapper;
//import com.seonier.persistence.model.User;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserMapper userMapper;
//
//    public CustomUserDetailsService(UserMapper userMapper) {
//        this.userMapper = userMapper;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // DB 에서 userId 로 조회
//        User u = userMapper.findByUserId(username);
//        if (u == null || !"YES".equals(u.getUseAT())) {
//            throw new UsernameNotFoundException("사용자 조회 실패: " + username);
//        }
//        // Spring Security 가 이해할 수 있는 UserDetails 객체로 변환
//        return org.springframework.security.core.userdetails.User
//                .withUsername(u.getUserId())
//                .password(u.getPasswd())         // DB 에 암호는 BCrypt 로 저장돼 있어야 함
//                .roles(u.getUserGroupId())       // 예: "ADMIN", "GUEST" 등을 ROLE_XXX 로 사용
//                .build();
//    }
//}
package com.seonier.security;

import com.seonier.persistence.mapper.UserMapper;
import com.seonier.persistence.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userMapper.findByUserId(username);
        if (u == null) {
            throw new UsernameNotFoundException(username + " 사용자를 찾을 수 없습니다.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(u.getUserId())
                .password(u.getPasswd())        // DB에 bcrypt로 저장되어 있어야 합니다
                .roles(u.getUserGroupId())      // “ADMIN”/“CUSTOMER” 같은 값을 ROLE_ 없이 그대로
                .build();
    }
}
