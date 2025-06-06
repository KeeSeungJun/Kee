package com.seonier.util;

import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import static com.seonier.constants.Regex.BIRTHDAY;
import static com.seonier.constants.Regex.EMAIL;
import static com.seonier.constants.Regex.ENGLISH;
import static com.seonier.constants.Regex.IP4;
import static com.seonier.constants.Regex.MOBILE_PHONE;
import static com.seonier.constants.Regex.NUMBER;
import static com.seonier.constants.Regex.SAME_PATTERN;
import static com.seonier.constants.Regex.TELEPHONE;
import static com.seonier.constants.Regex.URL;

/**
 * 정규 표현식(영어: regular expression, 간단히 regexp 또는 regex) 유틸리티.
 *
 * @version 1.0.0
 */
@Slf4j
@SuppressWarnings("all")
public class RegexUtils {

	private RegexUtils() throws IllegalAccessException {
		throw new IllegalAccessException("access to class not allowed.");
	}

	/**
	 * The email validation.
	 *
	 * @param email the email address, format: zhangsan@zuidaima.com . zhangsan@xxx.com.cn . xxx On behalf of a mail service provider
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isEmail(final String email) {
		return Pattern.compile(EMAIL).matcher(email).matches();
	}

	/**
	 * The number validation.
	 *
	 * @param number the number.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isNumber(final String number) {
		return Pattern.compile(NUMBER).matcher(number).matches();
	}

	/**
	 * The english validation.
	 *
	 * @param english the english.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isEnglish(final String english) {
		return Pattern.compile(ENGLISH).matcher(english).matches();
	}

	/**
	 * 3개 이상 동일한 문자 패턴 체크.
	 *
	 * @param passwd
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isSamePattern(final String passwd) {
		return Pattern.compile(SAME_PATTERN).matcher(passwd).matches();
	}

	/**
	 * 3개 이상 동일한 문자 패턴 체크.
	 *
	 * @param passwd
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isNotSamePattern(final String passwd) {
		return !isSamePattern(passwd);
	}

	/**
	 * The mobile phone validation.
	 *
	 * @param phoneNum the mobile phone number.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isMobilePhone(final String phoneNum) {
		return Pattern.compile(MOBILE_PHONE).matcher(phoneNum).matches();
	}

	/**
	 * The telephone validation.
	 *
	 * @param phoneNum the telephone number.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isTelephone(final String phoneNum) {
		return Pattern.compile(TELEPHONE).matcher(phoneNum).matches();
	}

	/**
	 * The birthday validation (year, month, day)
	 *
	 * @param birthday the birthday, format: 1992-09-03 or 1992.09.03 or 1992/09/03
	 * @return Verify successful return true , returned after validation failure false
	 */
	public static boolean isBirthday(final String birthday) {
		return Pattern.compile(BIRTHDAY).matcher(birthday).matches();
	}

	/**
	 * The IPv4 address validation.
	 *
	 * @param ip the ip address, format: 127.0.0.1
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isIPv4(final String ip) {
		return Pattern.compile(IP4).matcher(ip).matches();
	}

	/**
	 * The URL address validation.
	 *
	 * @param url Format: http://blog.csdn.net:80/xyang81/article/details/7705960?  or  http://www.csdn.net:80
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isURL(final String url) {
		return Pattern.compile(URL).matcher(url).matches();
	}

	/**
	 * The white space character validation.
	 *
	 * @param str the string.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isNotSpace(final String str) {
		return !isSpace(str);
	}

	/**
	 * The white space character validation.
	 *
	 * @param str the string.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isSpace(final String str) {
		return Pattern.compile("\\s").matcher(str).find();
	}

	/**
	 * 문자 최소 / 최대 길이 체크.
	 *
	 * @param str 문자열.
	 * @param min 최소 길이.
	 * @param max 최대 길이.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean isCharacterLength(String str, int min, int max) {
		return matches(String.format(".{%d,%d}$", min, max), str);
	}

	/**
	 * The find regex check.
	 *
	 * @param regex the regex patterns.
	 * @param value the valication check string.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean find(final String regex, final String value) {
		return find(Pattern.compile(regex), value);
	}

	/**
	 * The find regex check.
	 *
	 * @param pattern the pattern.
	 * @param value the valication check string.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean find(final Pattern pattern, final String value) {
		return pattern.matcher(value).find();
	}

	/**
	 * The regex check.
	 *
	 * @param regex the regex patterns.
	 * @param value the valication check string.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean matches(final String regex, final String value) {
		return matches(Pattern.compile(regex), value);
	}

	/**
	 * The regex check.
	 *
	 * @param pattern the pattern.
	 * @param value the valication check string.
	 * @return the verify successful return true, returned after validation failure false
	 */
	public static boolean matches(final Pattern pattern, final String value) {
		return pattern.matcher(value).matches();
	}

}
