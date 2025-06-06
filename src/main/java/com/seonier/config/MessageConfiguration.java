package com.seonier.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * The web mvc configuration.
 *
 * @version 1.0.0
 */
@Slf4j
@Configuration
public class MessageConfiguration {

	// @formatter:off
	private static final String[] DEFAULT_BASENAMES = {
			"classpath:messages/response"
	};
	// @formatter:on

	/**
	 * Reloadable resource bundle message source
	 *
	 * @return the reloadable resource bundle message source
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		// Whether to always apply the MessageFormat rules, parsing even messages without arguments.
		messageSource.setAlwaysUseMessageFormat(false);

		// Comma-separated list of basenames (essentially a fully-qualified classpath location),
		// each following the ResourceBundle convention with relaxed support for slash based locations.
		messageSource.setBasenames(DEFAULT_BASENAMES);

		// Specify the time interval to detect changes to the property file.
		// messageSource.setCacheSeconds(60);

		// Message bundles encoding.
		messageSource.setDefaultEncoding("UTF-8");

		// If there is no message set in the code, the code is the default message.
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
		log.debug("Message Source Accessor: {}", messageSource);
		return new MessageSourceAccessor(messageSource);
	}

	/**
	 * Local resolver
	 * "Locale change interceptor"에 한번 설정된 Locale 은 Session 또는 Cookie 에 저장된다.
	 *
	 * @return the locale resolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver("locale");
	}

	/**
	 * Locale change interceptor
	 *
	 * @return the local change interceptor
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor locale = new LocaleChangeInterceptor();
		locale.setParamName("locale");
		return locale;
	}

}
