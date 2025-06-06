package com.seonier.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.LiteWebJarsResourceResolver;

/**
 * The web mvc configuration.
 *
 * @version 1.0.2
 * @see WebMvcConfigurer
 */
@Slf4j
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	// @formatter:off
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
			"classpath:/META-INF/resources/",
			"classpath:/public/",
			"classpath:/resources/",
			"classpath:/static/"
	};
	// @formatter:on

	private final LocaleChangeInterceptor localeChangeInterceptor;

	public WebMvcConfiguration(LocaleChangeInterceptor localeChangeInterceptor) {
		this.localeChangeInterceptor = localeChangeInterceptor;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
	}

	/**
	 * Add Spring MVC lifecycle interceptors for pre- and post-processing of
	 * controller method invocations and resource handler requests.
	 *
	 * @param registry the interceptor registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		log.debug("Add Spring MVC lifecycle interceptors.");
		registry.addInterceptor(this.localeChangeInterceptor);
	}

	/**
	 * Add handlers to serve static resources.
	 *
	 * @param registry the resource handler registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.debug("Add handlers to serve static resources");

		// @formatter:off
		registry.addResourceHandler("/**")
				.addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
				// .setCachePeriod(31_536_000); // Cache 기간 설정

		// Register Webjars Resource
		// Webjars registration sample : ex. /webjars/jquery/jquery.min.js
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/")
				// .setCachePeriod(31_536_000)
				.resourceChain(true)
				.addResolver(new LiteWebJarsResourceResolver());
		// @formatter:on
	}

}
