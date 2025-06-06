package com.seonier;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by sky01126 on 25. 3. 18..
 *
 * @version 1.0.0
 * @see
 */
public class PasswordEncoderUtil {

	 public static void main(String[] args) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String rawPassword = "qwer1234";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }

}
