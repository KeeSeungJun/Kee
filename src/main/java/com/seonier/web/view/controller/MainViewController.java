package com.seonier.web.view.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import com.seonier.persistence.model.User;
import com.seonier.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;


//@Slf4j
//@RequiredArgsConstructor
//@Controller
//public class MainViewController extends AbstractViewController {
//
//	private final UserService userService;
//
//	/**
//	 * Reqeust Mapping이 정의 되지 않은 페이지 처리.
//	 *
//	 * @param model model attributes.
//	 * @return view page.
//	 */
//	@RequestMapping(path = { "/", "main" })
//	public String main(Model model, @RequestParam(required = false, name = "user_id") String userId) {
//		log.debug("Access the main page.");
//		User user = userService.getUserByUserId(StringUtils.isBlank(userId) ? "admin" : userId);
//
//		// DB 쿼리 방법을 보여주기 위해서 TMP로 다시 조회하는 로직 추가.
//		user = userService.getUserByUserNo(user.getUserNo());
//
//		model.addAttribute("message", user.getUserName() + "님 환영합니다.");
//		return "view/main";
//	}
//
//}
@Slf4j
@RequiredArgsConstructor
@Controller
public class MainViewController extends AbstractViewController {

	private final UserService userService;

	/**
	 * Reqeust Mapping이 정의 되지 않은 페이지 처리.
	 */
	@RequestMapping(path = { "/", "main" })
	public String main(
			Model model,
			@RequestParam(required = false, name = "user_id") String userId
	) {
		log.debug("Access the main page.");
		User user = userService.getUserByUserId(
				StringUtils.isBlank(userId) ? "admin" : userId
		);
		// TMP: 다시 조회 예시
		user = userService.getUserByUserNo(user.getUserNo());

		// --- 1) userName 속성 추가 ---
		model.addAttribute("userName", user.getUserName());

		// 2) 만약 ADMIN 이면 admin 전용 페이지로
		if ("ADMIN".equalsIgnoreCase(user.getUserGroupId())) {
			model.addAttribute("message", user.getUserName() + "님, 관리자 페이지에 오신 것을 환영합니다.");
			return "view/adminmain";
		}
		// 그 외 사용자
		model.addAttribute("message", user.getUserName() + "님 환영합니다.");
		return "view/main";
	}

	/**
	 * 자주 묻는 질문 페이지
	 */
	@GetMapping("/faq")
	public String faqPage() {
		log.debug("Access the FAQ page.");
		// 필요한 모델이 있으면 model.addAttribute(...) 해 주세요
		return "view/faq";
	}

	/**
	 * 일자리 추천 페이지
	 */
	@GetMapping("/job")
	public String jobRecommendPage() {
		log.debug("Access the Job Recommend page.");
		// 필요한 모델이 있으면 model.addAttribute(...) 해 주세요
		return "view/job";
	}

	/** 자주 묻는 질문 (FAQ) 관리 페이지 */
	@GetMapping("/faqmanage")
	public String faqManagePage(Model model) {
		log.debug("Access the FAQ Manage page.");
		// TODO: service 호출하여 model.addAttribute("faqs", faqService.findAll());
		return "view/faqmanage";
	}

	/** 일자리 등록 페이지 */
	@GetMapping("/jobadd")
	public String jobAddPage(Model model) {
		log.debug("Access the Job Add page.");
		// TODO: 사전 데이터(카테고리 목록 등) model 에 실어주기
		return "view/jobadd";
	}

	/** Q&A 관리 페이지 */
	@GetMapping("/qnamanage")
	public String qnaManagePage(Model model) {
		log.debug("Access the QnA Manage page.");
		// TODO: service 호출하여 model.addAttribute("questions", qnaService.findAll());
		return "view/qnamanage";
	}
	@GetMapping("/map/test")
	public String mapTestPage(HttpServletRequest request, Model model) {
		log.debug("Access the Map Manage page.");
		// 1. 서비스에서 일자리 리스트를 가져와서 View에 전달.
		// 2. GPT를 통한 Rerank를 하여 list를 View에 전달.
		// 3. View에서 일자리 x, y 정보를 통하여 맵에 마커 표시.
		// 내 주소를 조회해서 카카오 로컬 API 를 이용해 조회된 위/경도

		model.addAttribute("x", 127.074163955752);
		model.addAttribute("y", 37.4910941147932);
		// TODO: service 호출하여 model.addAttribute("questions", qnaService.findAll());
//		return "view/map_test_kee";
		return "view/map";
	}
	@GetMapping("/profile")
	public String profile(Model model) {
		log.debug("Access the Profile Manage page.");
		return "view/profile";
	}
	@GetMapping("/profileEdit")
	public String profileEdit(Model model) {
		log.debug("Access the ProfileEdit Manage page.");
		return "view/profileEdit";
	}
	@GetMapping("/profileEditAdmin")
	public String profileEditAdmin(Model model) {
		log.debug("Access the ProfileEditAdmin Manage page.");
		return "view/profileEditAdmin";
	}
	@GetMapping("/accDel")
	public String accDelete(Model model) {
		log.debug("Access the Accdelete page.");
		return "view/accDel";
	}
	@GetMapping("/addDelAdmin")
	public String addDeleteAdmin(Model model) {
		log.debug("Access the addDeleteAdmin page.");
		return "view/addDelAdmin";
	}
}