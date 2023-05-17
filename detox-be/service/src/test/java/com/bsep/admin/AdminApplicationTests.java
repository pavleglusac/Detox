package com.sbnz.detox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.SecureRandom;

@SpringBootTest
class AdminApplicationTests {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() throws IOException {
//		System.out.println(generateRandomToken(15));
//		System.out.println(generateRandomToken(15));
//		System.out.println(generateRandomToken(15));
//		System.out.println(generateRandomToken(15));
		String randomToken = generateRandomToken(15);
		System.out.println(randomToken);
		System.out.println(passwordEncoder.encode(randomToken));

//		System.out.println("-----------------");
//		System.out.println(passwordEncoder.encode("Rgx>m--in=Yc%UZ"));
//		ASN1ObjectIdentifier asn1ObjectIdentifier = new ASN1ObjectIdentifier("1.3.6.1.5.5.7.3.5");
//		ASN1Sequence asn1Sequence = ASN1Sequence.getInstance(asn1ObjectIdentifier);
//		KeyPurposeId[] keyPurposeIds = { KeyPurposeId.id_kp_clientAuth, KeyPurposeId.id_kp_serverAuth };
//		new ExtendedKeyUsage(
//				new KeyPurposeId[] {
//						KeyPurposeId.id_kp_serverAuth,
//						KeyPurposeId.id_kp_clientAuth});
//		ExtendedKeyUsage extendedKeyUsage = new ExtendedKeyUsage();
//		Extension extension = new Extension(Extension.extendedKeyUsage, false, asn1ObjectIdentifier.getEncoded());
//		System.out.println(extension.getExtnId());

	}

	public String generateRandomToken(int length) {
		int leftLimit = 35;
		int rightLimit = 126;
		SecureRandom random = new SecureRandom();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		return generatedString;
	}

}
