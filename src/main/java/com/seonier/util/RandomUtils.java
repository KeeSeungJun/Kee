package com.seonier.util;

import java.security.SecureRandom;

public class RandomUtils {

	private RandomUtils() throws IllegalAccessException {
		throw new IllegalAccessException("Access to class not allowed.");
	}

	public static String get() {
		return get(6);
	}

	public static String get(final int length) {
		String randomWord = "";
		SecureRandom random = new SecureRandom();
		do {
			String tmpRandomWord = randomWord + random.nextInt(10);
			if (RegexUtils.isSamePattern(tmpRandomWord)) {
				continue;
			}
			randomWord = tmpRandomWord;
		} while (randomWord.length() < length);
		return randomWord;
	}

}
