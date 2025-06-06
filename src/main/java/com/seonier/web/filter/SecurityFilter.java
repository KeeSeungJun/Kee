package com.seonier.web.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * the web security filter
 *
 * @version 1.0.0
 * @see OncePerRequestFilter
 */
@Slf4j
@Component
@Order(1)
public class SecurityFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(final HttpServletRequest request, @NonNull final HttpServletResponse response,
			@NonNull final FilterChain chain) throws ServletException, IOException {
		String methodName = request.getMethod();
		if (StringUtils.equalsAnyIgnoreCase(methodName, HttpMethod.TRACE.name())) {
			log.info("{} method not allowed", methodName);
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			response.setContentType("message/http");
			response.getWriter().println(methodName + " method not allowed");
			response.getWriter().flush();
		} else {
			chain.doFilter(request, response);
		}
	}
}