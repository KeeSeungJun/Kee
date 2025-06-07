package com.seonier.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.seonier.dto.response.DefaultResponse;
import com.seonier.persistence.model.User;
import com.seonier.util.JsonUtils;
import com.seonier.web.lang.RequestException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExampleService {

	private final OpenAiChatModel openAiChatModel;

	private final UserService userService;

	@Value("${prompt}")
	private String prompt;

	public DefaultResponse getJobList(String userId) {
		User user = userService.findByUserId(userId);
		if (user == null || StringUtils.isEmpty(user.getUserId())) {
			throw new RequestException(401, "로그인 후 다시 이용해주세요.");
		}

		// TODO 샘플로 사용자 리스트 조회, 일자리 정보를 이런식으로 가져온다.
		List<User> users = userService.findAll();
		log.debug("User list: {}", JsonUtils.toJsonLogIndent(users));

		// DB에서 조회된사용한 정보를 이용해서 프롬프트를 완성한다.
		String content = StringUtils.replace(this.prompt, "{{USER_ID}}", user.getUserId());
		content = StringUtils.replace(content, "{{JOBS}}", JsonUtils.toJsonLogIndent(users));
		log.debug("Prompt content: {}", content);

		OpenAiChatOptions.Builder builder = OpenAiChatOptions.builder()
				.model(OpenAiApi.ChatModel.GPT_4_O_MINI) // TODO OpenAI Model 설정
				.temperature(0.0) // 무작위성" 또는 "창의성"을 조절하는 파라미터
				.responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.TEXT).build()); // 응답을 어떻게 받을지 정의

		// TODO 실제 OpenAI 호출하는 부분
		// ChatResponse chatResponse = this.openAiChatModel.call(
		// 		new Prompt(UserMessage.builder().text(content).build(), builder.build())
		// );
		// String result = chatResponse.getResult().getOutput().getText();
		// log.debug("Open AI Text: {}", result);

		// 응답받은 값을 정의한다.
		// { name: "청소 업무", salary: "220만원", location: "대전 서구", company: "클린업", contact: "042-123-4567" },
		// { name: "배달 보조", salary: "200만원", location: "대전 중구", company: "퀵익스프레스", contact: "010-2222-3333" },
		// { name: "식당 서빙", salary: "230만원", location: "대전 유성구", company: "맛집식당", contact: "042-987-6543" },

		// TODO 이곳에 실제 데이터가 들어가야한다. 이 부분은 임의적으로 만든 것임.
		List<Map<String, String>> list = List.of(
				Map.of("name", "청소 업무", "salary", "220만원", "location", "대전 서구", "company", "클린업", "contact", "042-123-4567"),
				Map.of("name", "배달 보조", "salary", "200만원", "location", "대전 중구", "company", "퀵익스프레스", "contact", "010-2222-3333"),
				Map.of("name", "식당 서빙", "salary", "230만원", "location", "대전 유성구", "company", "맛집식당", "contact", "042-987-6543")
		);

		return DefaultResponse.builder()
				.put("list", list)
				.build();
	}
}
