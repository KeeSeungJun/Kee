//package com.seonier.web.rest.controller;
//
//import com.seonier.dto.response.DefaultResponse;
//import com.seonier.service.ExampleService;
//import com.seonier.web.lang.AbstractController;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.context.support.MessageSourceAccessor;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * The spring mvc example controller
// *
// * @version 1.0.1
// */
//@Slf4j
//@RequiredArgsConstructor
//@RestController
//public class ExampleController extends AbstractController {
//
//	private final MessageSourceAccessor messageSource;
//
//	private final ExampleService exampleService;
//
//	@GetMapping(path = "api/example/get1", produces = MediaType.APPLICATION_JSON_VALUE)
//	public DefaultResponse getExample(HttpServletRequest request) {
//		// 1. Cookie에서 사용자 정보 조회
//		String userId = getUserIdFromCookies(request);
//		return exampleService.getJobList(userId);
//	}
//
//}
