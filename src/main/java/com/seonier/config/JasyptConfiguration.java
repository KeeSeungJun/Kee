package com.seonier.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션의 Jasypt 암호화 및 복호화 설정을 구성하는 클래스.
 * <p>
 * Jasypt는 민감한 데이터를 암호화 및 복호화하기 위한 라이브러리로,
 * 이 클래스에서는 {@link PooledPBEStringEncryptor}를 기반으로 한
 * 암호화 로직과 설정을 정의합니다.
 * <p>
 * 기본 암호화 알고리즘으로 PBEWITHSHA256AND128BITAES-CBC-BC를 사용하며,
 * 암호화 키, 복호화 키 생성 방식을 포함한 다양한 설정을 제공합니다.
 * <p>
 * 서비스에서 안전한 텍스트 데이터를 관리하기 위해 이 설정을 활용할 수 있습니다.
 *
 * @see StringEncryptor
 * @see PooledPBEStringEncryptor
 * @see SimpleStringPBEConfig
 */
@Slf4j
@Configuration
public class JasyptConfiguration {

	/**
	 * Jasypt 기반 문자열 암호화기를 생성하고 반환하는 메서드.
	 * <p>
	 * PBEWITHSHA256AND128BITAES-CBC-BC 알고리즘을 사용하며, 암호화 키, 반복 횟수,
	 * 풀 크기, Provider 및 난수 생성기 등의 설정을 포함합니다.
	 * <p>
	 * 특히, 민감한 데이터의 암호화와 복호화에 사용할 수 있는 고성능 암호화기를 제공합니다.
	 * 애플리케이션 구동 시 기본 설정값으로 초기화되며, System property를 통해 암호화 비밀번호 변경이 가능합니다.
	 *
	 * @return 문자열 암호화기 반환.
	 * @implNote PooledPBEStringEncryptor를 사용하여 암호화 성능을 높임.
	 * @apiNote 민감한 데이터의 안전한 암호화를 보장하기 위한 보안 설정 기반으로 동작.
	 * @see PooledPBEStringEncryptor
	 * @see SimpleStringPBEConfig
	 * @see BouncyCastleProvider
	 */
	@Bean("jasyptStringEncryptor")
	public StringEncryptor jasyptStringEncryptor() {
		log.debug("Pooled PBE String Encryptor...");
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setProvider(new BouncyCastleProvider());

		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		config.setPassword(System.getProperty("jasypt.encryptor.password", "Kee"));
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setStringOutputType("base64");
		config.setSaltGenerator(new RandomSaltGenerator());
		config.setIvGenerator(new RandomIvGenerator());
		encryptor.setConfig(config);
		return encryptor;
	}

}