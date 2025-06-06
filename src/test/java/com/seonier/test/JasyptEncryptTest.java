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
		config.setAlgorithm("PBEWITHSHA256AND256BITAES-CBC-BC");
		config.setPassword(System.getProperty("jasypt.encryptor.password", "Kee"));
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setStringOutputType("base64");
		config.setSaltGenerator(new RandomSaltGenerator());
		config.setIvGenerator(new RandomIvGenerator());
		encryptor.setConfig(config);

		String openaiKey = encryptor.encrypt("오픈AI 키를 입력해서 암호화를 한 후 Commit 시에는 삭제한다."); // 암호화 할 내용
		System.out.println("Username encrypt : " + openaiKey + "\ndecrypt :" + encryptor.decrypt(openaiKey));
	}

	@Test
	public void getAllAlgorithms() {
		System.out.println("allDigestAlgorithms : " + AlgorithmRegistry.getAllDigestAlgorithms());
		System.out.println("allPBEAlgorithms : " + AlgorithmRegistry.getAllPBEAlgorithms());
	}

}
