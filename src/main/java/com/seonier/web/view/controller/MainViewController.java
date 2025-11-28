package com.seonier.web.view.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import com.seonier.persistence.model.User;
import com.seonier.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainViewController extends AbstractViewController {

	private final UserService userService;

	@GetMapping({"/", "/main"})
	public String main(HttpServletRequest request, Model model) {
		String userId = getUserIdFromCookies(request);
		log.debug("Access the main page. userId={}", userId);

		if (StringUtils.isBlank(userId)) {
			return "redirect:/login";
		}

		User user = userService.getUserByUserId(userId);
		if (user == null) {
			return "redirect:/login";
		}

		model.addAttribute("userName", user.getUserName());

		String role = StringUtils.trimToEmpty(user.getUserGroupId()).toUpperCase();
		if ("ADMIN".equals(role)) {
			model.addAttribute("message", user.getUserName() + "님, 관리자 페이지에 오신 것을 환영합니다.");
			return "view/adminmain";
		}

		model.addAttribute("message", user.getUserName() + "님 환영합니다.");
		return "view/main";
	}


	@GetMapping("/faq")
	public String faqPage() {
		log.debug("Access the FAQ page.");
		return "view/faq";
	}

	@GetMapping("/job")
	public String jobRecommendPage() {
		log.debug("Access the Job Recommend page.");
		return "view/job";
	}

	@GetMapping("/faqmanage")
	public String faqManagePage(Model model) {
		log.debug("Access the FAQ Manage page.");
		return "view/faqmanage";
	}

	@GetMapping("/jobadd")
	public String jobAddPage(Model model) {
		log.debug("Access the Job Add page.");
		return "view/jobadd";
	}

	@GetMapping("/jobnew")
	public String jobNewPage(Model model) {
		log.debug("Access the Job New page.");
		return "view/jobnew";
	}

	@GetMapping("/qnamanage")
	public String qnaManagePage(Model model) {
		log.debug("Access the QnA Manage page.");
		return "view/qnamanage";
	}
	@GetMapping("/qnaList")
	public String qnaListPage(Model model) {
		log.debug("Access the QnA List page.");
		return "view/qnaList";
	}

//  @GetMapping("/main")
//  public String mainPage(
//        HttpServletRequest request,
//        Model model
//  ) {
//     String userId = getUserIdFromCookies(request);
//     User user = userService.getUserByUserId(userId);
//     model.addAttribute("userName", user.getUserName());
//     log.debug("Access the main List page for user {}", userId);
//     return "view/main";
//  }

	@GetMapping("/map")
	public String mapPage(HttpServletRequest request, Model model) {
		log.debug("Access the Map Manage page.");
		return "view/map";
	}
	@GetMapping("/profile")
	public String profile(Model model) {
		log.debug("Access the Profile Manage page.");
		return "view/profile";
	}
	@GetMapping("/myResume")
	public String myResume(Model model) {
		log.debug("Access the myResume Manage page.");
		return "view/myResume";
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
	@GetMapping("/jobmanage")
	public String jobManage(Model model) {
		return "view/jobmanage";
	}
	@GetMapping("/adminprofile")
	public String adminProfile() {
		log.debug("Access the Admin Profile page.");
		return "view/adminprofile";
	}

	@GetMapping("/mapAdmin")
	public String mapAdminPage() {
		log.debug("Access the Admin Map page.");
		return "view/mapAdmin";
	}
}