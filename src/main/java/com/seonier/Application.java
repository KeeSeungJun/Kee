//package com.seonier;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
///**
// * The spring boot start application
// *
// * @version 1.0.1
// * @see SpringBootServletInitializer
// */
//@SpringBootApplication
//public class Application extends SpringBootServletInitializer {
//
//	public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);
//	}
//
//	/**
//	 * Configure the application. Normally all you would need to do is to add sources
//	 * (e.g. config classes) because other settings have sensible defaults. You might
//	 * choose (for instance) to add default command line arguments, or set an active
//	 * Spring profile.
//	 *
//	 * @param application a builder for the application context
//	 * @return the spring application builder
//	 * @see SpringApplicationBuilder
//	 */
//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(Application.class);
//	}
//
//	/**
//	 * BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호 인코딩 메서드.
//	 *
//	 * @return the password encoder.
//	 */
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//	}
//
//}
package com.seonier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The spring boot start application
 *
 * @version 1.0.1
 * @see SpringBootServletInitializer
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.seonier.persistence.mapper")  // ← 여기에 매퍼 패키지 경로를 넣어주세요
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Configure the application. Normally all you would need to do is to add sources
	 * (e.g. config classes) because other settings have sensible defaults. You might
	 * choose (for instance) to add default command line arguments, or set an active
	 * Spring profile.
	 *
	 * @param application a builder for the application context
	 * @return the spring application builder
	 * @see SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

	/**
	 * BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호 인코딩 메서드.
	 *
	 * @return the password encoder.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
