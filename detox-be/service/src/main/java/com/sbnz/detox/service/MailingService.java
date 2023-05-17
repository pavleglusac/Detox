package com.sbnz.detox.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Service
public class MailingService {

	@Autowired
	private JavaMailSender mailSender;

	private final Path templatesLocation;

	@Autowired
	public MailingService() {
		templatesLocation = Paths.get("src", "main", "resources", "templates");
	}

	@Async
	public void sendCertificateMail(String name, File certificate, File publicKey, File privateKey) {
		String content = renderTemplate("distribution.html", Map.of("name", name));
		sendMailWithAttachment("bsepml23@gmail.com", "Certificate", content, certificate,publicKey, privateKey);
	}

	@Async
	public void sendVerificationMail(String name, String email, String emailVerificationToken, String loginToken) {
		String encodedEmailToken = URLEncoder.encode(emailVerificationToken, StandardCharsets.UTF_8);
		String content = renderTemplate("verification.html", Map.of("name", name,
				"email", email,
				"verificationToken", encodedEmailToken,
				"loginToken", loginToken));
		sendMail("bsepml23@gmail.com", "Verification", content);
	}

	private void sendMailWithAttachment(String to, String subject, String body, File cert, File publicKey, File privateKey) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setText(body, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.addAttachment(cert.getName(), cert);
			helper.addAttachment(publicKey.getName(), publicKey);
			helper.addAttachment(privateKey.getName(), privateKey);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private void sendMail(String to, String subject, String body) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setText(body, true);
			helper.setTo(to);
			helper.setSubject(subject);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	private String renderTemplate(String templateName, String... variables) {
		Map<String, String> variableMap = new HashMap<>();

		List<String> keyValueList = Arrays.stream(variables).collect(Collectors.toList());

		if (keyValueList.size() % 2 != 0)
			throw new IllegalArgumentException();

		for (int i = 0; i < keyValueList.size(); i += 2) {
			variableMap.put(keyValueList.get(i), keyValueList.get(i + 1));
		}

		return renderTemplate(templateName, variableMap);
	}

	private String renderTemplate(String templateName, Map<String, String> variables) {
		File file = templatesLocation.resolve(templateName).toFile();
		String message = null;
		try {
			message = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String target, renderedValue;
		for (var entry : variables.entrySet()) {
			target = "\\{\\{ " + entry.getKey() + " \\}\\}";
			renderedValue = entry.getValue();

			message = message.replaceAll(target, Matcher.quoteReplacement(renderedValue));
		}

		return message;
	}


}