package com.seonier.constants;

/**
 * 정규 표현식 패턴.
 *
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class Regex {

	/**
	 * Email 주소 패턴
	 */
	public static final String EMAIL = "[a-zA-Z0-9]+[a-zA-Z0-9._-]*@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";

	/**
	 * Number 패턴
	 */
	public static final String NUMBER = "^[\\d]*$";

	/**
	 * English 패턴
	 */
	public static final String ENGLISH = "^[a-zA-Z]*$";

	/**
	 * 핸드폰 패턴
	 */
	public static final String MOBILE_PHONE = "^01(?:0|1|[6-9])[.-]?(\\\\d{3}|\\\\d{4})[.-]?(\\\\d{4})+";

	/**
	 * 전화 패턴
	 */
	public static final String TELEPHONE = "^\\d{2,3}[.-]?\\d{3,4}[.-]?\\d{4}+";

	/**
	 * 생일 패턴
	 */
	public static final String BIRTHDAY = "^(19|20)\\d{2}[.-]?(0[1-9]|1[0-2])[.-]?(0[1-9]|[12][0-9]|3[01])$";

	/**
	 * IPv4 패턴
	 */
	public static final String IP4 = "mask[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";

	/**
	 * URL 패턴
	 */
	public static final String URL = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";

	/**
	 * 비멀번호 패턴
	 * <p>
	 * 문자, 숫자, 특수문자 1개 이상 포함하는 경우의 패턴 : ^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W)
	 * 최소 8자리에 대문자, 소문자, 숫자 각 1개 이상 포함 : ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$
	 * 최소 8자리에 숫자, 문자, 특수문자 각 1개 이상 포함 : ^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$
	 * 최소 8자리에 대문자, 소문자, 숫자, 특수문자 각 1개 이상 포함 : ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[$@$!%*?&])[A-Za-z\d$@$!%*?&]{8,}
	 */
	@SuppressWarnings("unused")
	public static final String PASSWD = "^(?=.*[a-zA-Z])(?=.*\\d)";

	/**
	 * 연속된 문자 3개 이상 금지
	 */
	public static final String SAME_PATTERN = ".*(.)\\1\\1.*";

	private Regex() throws IllegalAccessException {
		throw new IllegalAccessException("access to class not allowed.");
	}

}
