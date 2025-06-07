//package com.seonier.service;
//
//import com.seonier.dto.request.LoginRequest;
//import com.seonier.dto.request.RegisterRequest;
//import com.seonier.dto.response.DefaultResponse;
//import com.seonier.persistence.mapper.UserMapper;
//import com.seonier.persistence.model.User;
//import com.seonier.web.lang.RequestException;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class UserService extends AbstractService {
//
//	private final PasswordEncoder passwordEncoder;
//	private final UserMapper userMapper;
//
//	public User getUserByUserNo(long userNo) {
//		log.debug("User no: {}", userNo);
//		return userMapper.findByUserNo(userNo);
//	}
//
//	public User getUserByUserId(String userId) {
//		log.debug("User id: {}", userId);
//		return userMapper.findByUserId(userId);
//	}
//
//	public DefaultResponse getLoginCheck(@Valid LoginRequest params) {
//		User user = userMapper.findByUserId(params.getUserId());
//		if (user == null || user.getUserNo() == null) {
//			throw new RequestException(401, "아이디 또는 비밀번호를 다시 확인해주세요.");
//		}
//
//		if (!this.passwordEncoder.matches(params.getPasswd(), user.getPasswd())) {
//			throw new RequestException(401, "아이디 또는 비밀번호를 다시 확인해주세요.");
//		}
//
//		return DefaultResponse.builder()
//				.put("user_id", user.getUserId())
//				.build();
//	}
//
//
//
//	public DefaultResponse registerUser(@Valid RegisterRequest params) {
//		log.debug("Register user: {}", params);
//
//		log.debug("▶▶▶ Incoming health flags: health1={}, health2={}, health3={}, health4={}, health5={}, health6={}, health7={}, health8={}",
//				params.getHealth1(),
//				params.getHealth2(),
//				params.getHealth3(),
//				params.getHealth4(),
//				params.getHealth5(),
//				params.getHealth6(),
//				params.getHealth7(),
//				params.getHealth8()
//		);
//
//		// 아이디 중복 체크
//		User existingUser = userMapper.findByUserId(params.getEmail());
//		if (existingUser != null) {
//			throw new RequestException(409, "이미 사용 중인 아이디입니다.");
//		}
//
//		// 비밀번호 암호화
//		String encryptedPassword = passwordEncoder.encode(params.getPassword());
//
//		// User 엔티티 생성 및 데이터 매핑
//		User user = new User();
//		user.setUserId(params.getEmail());                     // RegisterRequest.email -> userId
//		user.setPasswd(encryptedPassword);                     // 비밀번호 암호화 후 저장
//		user.setUserName(params.getName());                    // 이름
//		user.setMobileNumber(params.getPhoneNumber());         // 핸드폰 번호
//		user.setUserGroupId(params.getGroupId() != null ? params.getGroupId() : "CUSTOMER"); // 그룹 ID (없으면 기본)
//		user.setUseAT("YES");                                  // 기본값
//		user.setCreateId("system");                            // 생성자 ID
//		user.setUpdateId("system");                            // 수정자 ID
//
//		String raw = params.getGender(); // "male" or "female"
//		String code = raw.equalsIgnoreCase("male") ? "M" : "F";
//		user.setUsrGender(code);
//
//	//	user.setGender(params.getGender());
//
//		// 건강 상태 (int 그대로 매핑)
////		user.setUserHealth1(StringUtils.equalsIgnoreCase(params.getHealth1(), "checked") ? "Y" : "N");
////		user.setUserHealth2(StringUtils.equalsIgnoreCase(params.getHealth2(), "checked") ? "Y" : "N");
////		user.setUserHealth3(StringUtils.equalsIgnoreCase(params.getHealth3(), "checked") ? "Y" : "N");
////		user.setUserHealth4(StringUtils.equalsIgnoreCase(params.getHealth4(), "checked") ? "Y" : "N");
////		user.setUserHealth5(StringUtils.equalsIgnoreCase(params.getHealth5(), "checked") ? "Y" : "N");
////		user.setUserHealth6(StringUtils.equalsIgnoreCase(params.getHealth6(), "checked") ? "Y" : "N");
////		user.setUserHealth7(StringUtils.equalsIgnoreCase(params.getHealth7(), "checked") ? "Y" : "N");
////		user.setUserHealth8(StringUtils.equalsIgnoreCase(params.getHealth8(), "checked") ? "Y" : "N");
//
//		//user.setUserHealth1("Y".equalsIgnoreCase(params.getHealth1()) ? "Y" : "N");
//		String h1 = "Y".equalsIgnoreCase(params.getHealth1()) ? "Y" : "N";
//		log.debug("Mapping health1: raw='{}' → mapped='{}'", params.getHealth1(), h1);
//		user.setUsrHealth1(h1);
//
//		user.setUsrHealth2("Y".equalsIgnoreCase(params.getHealth2()) ? "Y" : "N");
//		user.setUsrHealth3("Y".equalsIgnoreCase(params.getHealth3()) ? "Y" : "N");
//		user.setUsrHealth4("Y".equalsIgnoreCase(params.getHealth4()) ? "Y" : "N");
//		user.setUsrHealth5("Y".equalsIgnoreCase(params.getHealth5()) ? "Y" : "N");
//		user.setUsrHealth6("Y".equalsIgnoreCase(params.getHealth6()) ? "Y" : "N");
//		user.setUsrHealth7("Y".equalsIgnoreCase(params.getHealth7()) ? "Y" : "N");
//		user.setUsrHealth8("Y".equalsIgnoreCase(params.getHealth8()) ? "Y" : "N");
//
//
//
//		// DB 저장
//		userMapper.insertUser(user);
//		log.debug(">>> User no: {}", user.getUserNo());
//
//		return DefaultResponse.builder()
//				.put("user_id", user.getUserId())
//				.build();
//	}
//}
package com.seonier.service;

import java.util.List;

import com.seonier.dto.request.LoginRequest;
import com.seonier.dto.request.RegisterRequest;
import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.mapper.UserMapper;
import com.seonier.persistence.model.User;
import com.seonier.web.lang.RequestException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService extends AbstractService {

	private final PasswordEncoder passwordEncoder;
	private final UserMapper userMapper;

	/**
	 * 1-2: Controller 에서 호출할 메서드 시그니처.
	 * 내부에서는 기존 getUserByUserNo / getUserByUserId 를 재사용합니다.
	 */
	public User findByUserNo(long userNo) {
		return getUserByUserNo(userNo);
	}

	public User findByUserId(String userId) {
		return getUserByUserId(userId);
	}

	public List<User> findAll() {
		return userMapper.findAll();
	}

	/**
	 * 기존에 쓰이던 메서드들 — 그대로 둡니다.
	 */
	public User getUserByUserNo(long userNo) {
		log.debug("User no: {}", userNo);
		return userMapper.findByUserNo(userNo);
	}

	public User getUserByUserId(String userId) {
		log.debug("User id: {}", userId);
		return userMapper.findByUserId(userId);
	}

	public DefaultResponse getLoginCheck(HttpServletResponse response, @Valid LoginRequest params) {
		// (내부적으로는 userMapper.findByUserId 를 호출하던 부분이
		// 이제 getUserByUserId → findByUserId 로 엮여 있습니다.)
		User user = getUserByUserId(params.getUserId());
		if (user == null || user.getUserNo() == null) {
			throw new RequestException(401, "아이디 또는 비밀번호를 다시 확인해주세요.");
		}

		if (!this.passwordEncoder.matches(params.getPasswd(), user.getPasswd())) {
			throw new RequestException(401, "아이디 또는 비밀번호를 다시 확인해주세요.");
		}

		ResponseCookie cookie = ResponseCookie.from("USER_ID", user.getUserId())
				.path("/")
				.maxAge(-1)
				.httpOnly(true)
				.secure(true)
				.build();
		response.setHeader("Set-Cookie", cookie.toString());

		return DefaultResponse.builder()
				.put("user_id", user.getUserId())
				.build();
	}

	public DefaultResponse registerUser(@Valid RegisterRequest params) {
		log.debug("Register user: {}", params);

		// 1) ID 중복 체크
		User existingUser = findByUserId(params.getEmail());
		if (existingUser != null) {
			throw new RequestException(409, "이미 사용 중인 아이디입니다.");
		}

		// 2) 비밀번호 암호화
		String encryptedPassword = passwordEncoder.encode(params.getPassword());

		// 3) User 객체에 값 세팅
		User user = new User();
		user.setUserId(params.getEmail());
		user.setPasswd(encryptedPassword);
		user.setUserName(params.getName());
		user.setBirthdate(params.getBirthdate());
		user.setPostcode(params.getPostcode());
		user.setUserAddr(params.getAddress());
		user.setCustomDisease(params.getCustomDisease());
		user.setGender(params.getGender());
		user.setMobileNumber(params.getPhoneNumber());
		user.setUserGroupId(params.getGroupId() != null ? params.getGroupId() : "CUSTOMER");
		user.setUseAT("YES");
		user.setCreateId("system");
		user.setUpdateId("system");
		user.setOccupation(params.getOccupation());
		user.setUserHealth(params.getSelectedDiseases());

		String raw = params.getGender(); // "male" or "female"
		String code = raw.equalsIgnoreCase("male") ? "M" : "F";
		user.setGender(code);

		// 건강 상태 매핑
//		user.setUserHealth1("Y".equalsIgnoreCase(params.getHealth1()) ? "Y" : "N");
//		user.setUserHealth2("Y".equalsIgnoreCase(params.getHealth2()) ? "Y" : "N");
//		user.setUserHealth3("Y".equalsIgnoreCase(params.getHealth3()) ? "Y" : "N");
//		user.setUserHealth4("Y".equalsIgnoreCase(params.getHealth4()) ? "Y" : "N");
//		user.setUserHealth5("Y".equalsIgnoreCase(params.getHealth5()) ? "Y" : "N");
//		user.setUserHealth6("Y".equalsIgnoreCase(params.getHealth6()) ? "Y" : "N");
//		user.setUserHealth7("Y".equalsIgnoreCase(params.getHealth7()) ? "Y" : "N");
//		user.setUserHealth8("Y".equalsIgnoreCase(params.getHealth8()) ? "Y" : "N");
//		user.setUsrHealthEct(params.getHealthEtc());

		// 4) DB 저장
		userMapper.insertUser(user);
		log.debug(">>> User no: {}", user.getUserNo());

		return DefaultResponse.builder()
				.put("user_id", user.getUserId())
				.build();
	}
}
