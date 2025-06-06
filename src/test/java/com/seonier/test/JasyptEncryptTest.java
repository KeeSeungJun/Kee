package com.seonier.test;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.registry.AlgorithmRegistry;
import org.jasypt.salt.RandomSaltGenerator;
import org.junit.jupiter.api.Test;

public class JasyptEncryptTest {

	@Test
	void encryptTest() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setProvider(new BouncyCastleProvider());

		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		// config.setAlgorithm("PBEWITHSHA256AND128BITAES-CBC-BC");
		config.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
		config.setPassword(System.getProperty("jasypt.encryptor.password", "Kee"));
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setStringOutputType("base64");
		config.setSaltGenerator(new RandomSaltGenerator());
		config.setIvGenerator(new RandomIvGenerator());
		encryptor.setConfig(config);

		String openaiKey = encryptor.encrypt("api-key: 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"); // 암호화 할 내용
		System.out.println("Username encrypt : " + openaiKey + "\ndecrypt :" + encryptor.decrypt(openaiKey));
	}

	@Test
	public void getAllAlgorithms() {
		System.out.println("allDigestAlgorithms : " + AlgorithmRegistry.getAllDigestAlgorithms());
		System.out.println("allPBEAlgorithms : " + AlgorithmRegistry.getAllPBEAlgorithms());
	}

}
