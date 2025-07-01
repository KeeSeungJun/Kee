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

@Slf4j
@Configuration
public class JasyptConfiguration {

	@Bean("jasyptStringEncryptor")
	public StringEncryptor jasyptStringEncryptor() {
		log.debug("Pooled PBE String Encryptor...");
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setProvider(new BouncyCastleProvider());

		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
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