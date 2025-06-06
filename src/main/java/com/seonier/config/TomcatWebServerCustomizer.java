package com.seonier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * Added option to embedded Tomcat
 *
 * @version 1.0.0
 * @see WebServerFactoryCustomizer
 * @see TomcatServletWebServerFactory
 */
@Configuration
public class TomcatWebServerCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

	@Value("${server.error.page:/error}")
	private String errorPage;

	/**
	 * Added option to embedded Tomcat.
	 *
	 * @param factory the tomcat servlet web server factory
	 */
	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		// @formatter:off
		factory.addErrorPages(
				new ErrorPage(HttpStatus.BAD_REQUEST, this.errorPage),
				new ErrorPage(HttpStatus.UNAUTHORIZED, this.errorPage),
				new ErrorPage(HttpStatus.PAYMENT_REQUIRED, this.errorPage),
				new ErrorPage(HttpStatus.FORBIDDEN, this.errorPage),
				new ErrorPage(HttpStatus.NOT_FOUND, this.errorPage),
				new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, this.errorPage),
				new ErrorPage(HttpStatus.NOT_ACCEPTABLE, this.errorPage),

				new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, this.errorPage),
				new ErrorPage(HttpStatus.NOT_IMPLEMENTED, this.errorPage),
				new ErrorPage(HttpStatus.BAD_GATEWAY, this.errorPage),
				new ErrorPage(HttpStatus.SERVICE_UNAVAILABLE, this.errorPage),
				new ErrorPage(HttpStatus.GATEWAY_TIMEOUT, this.errorPage)
		);
		// @formatter:on

		factory.addConnectorCustomizers(connector -> {
			connector.setAllowTrace(true); // Filter 에서 막기 위해서 TRACE 활성화
			connector.setProperty("relaxedQueryChars", "<>[\\]^`{|}");
		});
	}

}
