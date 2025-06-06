package com.seonier.dto.request;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The register request
 *
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@ToString
@JsonSerialize
public class  RegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -2233445566778899001L;

    @NotEmpty(message = "이름은 필수입니다.")
    @JsonProperty("name")
    private String name;

//    @NotEmpty(message = "생년월일은 필수입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("birthdate")
    private LocalDate birthdate;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @JsonProperty("email")
    private String email; // userId로 사용

    @NotEmpty(message = "비밀번호는 필수입니다.")
//    @Size(min = 6, max = 12, message = "비밀번호는 6자 이상 12자 이하로 입력해주세요.")
    @JsonProperty("password")
    private String password;

    @NotEmpty(message = "비밀번호 확인은 필수입니다.")
    @JsonProperty("confirm_password")
    private String confirmPassword;

    @JsonProperty("mobileNumber")
    private String mobileNumber;

//    @NotEmpty(message = "주소는 필수입니다.")
    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("address")
    private String address;

    @JsonProperty("detailAddress")
    private String detailAddress;

    @JsonProperty("extraAddress")
    private String extraAddress;

    @NotEmpty(message = "성별을 선택해주세요.")
    @JsonProperty("gender")
    private String gender;

    @JsonProperty("interests")
    private String interests; // 여러 개 선택 가능성 있음 (구분자 ',' 사용)

//    @NotEmpty(message = "직업은 필수입니다.")
    @JsonProperty("occupation")
    private String occupation;

    @JsonProperty("phone_number")
    private String phoneNumber;   // 사용자 핸드폰 번호

    @JsonProperty("group_id")
    private String groupId;       // 그룹 ID (없으면 CUSTOMER)

    // === 여기가 핵심 수정 ===
    @JsonProperty("selectedDiseases")
    private String selectedDiseases;

//    @JsonProperty("health2")
//    private String health2;
//
//    @JsonProperty("health3")
//    private String health3;
//
//    @JsonProperty("health4")
//    private String health4;
//
//    @JsonProperty("health5")
//    private String health5;
//
//    @JsonProperty("health6")
//    private String health6;
//
//    @JsonProperty("health7")
//    private String health7;
//
//    @JsonProperty("health8")
//    private String health8;

    @JsonProperty("customDisease")
    private String customDisease;

}
