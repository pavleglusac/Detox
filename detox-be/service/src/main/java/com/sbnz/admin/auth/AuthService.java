package com.sbnz.admin.auth;

import com.sbnz.admin.auth.dto.LoginRequest;
import com.sbnz.admin.auth.dto.TokenResponse;
import com.sbnz.admin.exception.UserNotFoundException;
import com.sbnz.admin.model.User;
import com.sbnz.admin.repository.RoleRepository;
import com.sbnz.admin.repository.UserRepository;
import com.sbnz.admin.security.CustomAuthenticationToken;
import com.sbnz.admin.security.TokenProvider;
import com.sbnz.admin.service.MailingService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {
		String secret = generateRandomToken(20);

		Authentication authentication = authenticationManager.authenticate(new CustomAuthenticationToken(
				loginRequest.getEmail(),
				loginRequest.getPassword(),
				loginRequest.getLoginToken(),
				secret
		));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String accessToken = tokenProvider.createAccessToken(authentication, secret);
		Long expiresAt = tokenProvider.readClaims(accessToken).getExpiration().getTime();
		// add cookie with secret
		Cookie cookie = new Cookie("token", accessToken);
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);
		((User)authentication.getPrincipal()).setLoginAttempts(0);
		userRepository.save((User)authentication.getPrincipal());
		return new TokenResponse(accessToken, expiresAt);
	}

	public String generateRandomToken(int length) {
		String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'()*+-./:<=>?@[]^_`{|}~";
		SecureRandom random = new SecureRandom();
		StringBuilder buffer = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(allowedChars.length());
			buffer.append(allowedChars.charAt(randomIndex));
		}
		String generatedString = buffer.toString();
		System.out.println(generatedString);
		return generatedString;
	}


	public void verify(String email, String token) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
		if (passwordEncoder.matches(token, user.getEmailVerificationToken())) {
			user.setEmailVerified(true);
			userRepository.save(user);
		} else {
			throw new RuntimeException("Invalid token");
		}
	}

	public void logout(User user, HttpServletRequest request, HttpServletResponse response) {
		// remove cookie with secret
		Cookie cookie = new Cookie("token", "");
		cookie.setPath("/");
		cookie.setSecure(false);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

}
