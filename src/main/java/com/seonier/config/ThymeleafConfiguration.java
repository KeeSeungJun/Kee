package com.seonier.config;

import lombok.extern.slf4j.Slf4j;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The  thymeleaf configuration
 *
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class ThymeleafConfiguration {

	/**
	 * The thymeleaf layout dialect bean.
	 *
	 * @return the layout dialect.
	 */
	@Bean
	public LayoutDialect layoutDialect() {
		log.debug("The thymeleaf layout dialect bean.");
		return new LayoutDialect();
	}

}
