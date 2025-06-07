package com.seonier.persistence.model;

import java.io.Serial;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 사용자 정보
 *
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class User extends AbstractModel {

    @Serial
    private static final long serialVersionUID = 3569659936430407817L;

    /**
     * 사용자 번호
     */
    private Long userNo;

    /**
     * 사용자 아이디
     */
    private String userId;

    /**
     * 비밀번호
     */
    @JsonIgnore
    private String passwd;

    /**
     * 사용자 이름
     */
    private String userName;

    /**
     * 사용자 생년월일
     */
	@JsonProperty(index = Integer.MAX_VALUE - 2)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birthdate;

    /**
     * 사용자 휴대폰번호
     */
    private String mobileNumber;

    /**
     * 사용자 그룹 아이디
     */
    private String userGroupId;

    /**
     * 사용 여부 (YES/NO)
     */
    private String useAT;

    /**
     * 생성자 ID
     */
    private String createId;

    /**
     * 수정자 ID
     */
    private String updateId;

    // ============================================
    // 추가된 건강 상태 필드 (질병 여부 - int형)
    // ============================================
    /**
     * 성별
     **/
    private String gender;

    private String userAddr;

    private String postcode;

    private String detailAddress;

    private Double userAddrLat;

    private Double userAddrLon;

    private String userJobScr;


    /**
     * 건강 상태
     */
    private String userHealth;

//    /**
//     * 건강 상태 - 당뇨병
//     */
//    private String userHealth2;
//
//    /**
//     * 건강 상태 - 목디스크
//     */
//    private String userHealth3;
//
//    /**
//     * 건강 상태 - 관절염
//     */
//    private String userHealth4;
//
//    /**
//     * 건강 상태 - 만성요통
//     */
//    private String userHealth5;
//
//    /**
//     * 건강 상태 - 심장질환
//     */
//    private String userHealth6;
//
//    /**
//     * 건강 상태 - 시력저하
//     */
//    private String userHealth7;
//
//    /**
//     * 건강 상태 - 청력저하
//     */
//    private String userHealth8;


    private String customDisease;


    private String occupation;


}
