package com.sbnz.detox.security;


import com.sbnz.detox.config.AppProperties;
import com.sbnz.detox.exception.InvalidAccessTokenException;
import com.sbnz.detox.exception.InvalidRecoveryTokenException;
import com.sbnz.detox.exception.InvalidTokenTypeException;
import com.sbnz.detox.exception.InvalidVerificationTokenException;
import com.sbnz.detox.model.User;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Service
public class TokenProvider {

	private AppProperties appProperties;

	private final Long ACCESS_TOKEN_EXPIRATION = 60L * 60L * 24L * 7L;

	public TokenProvider(AppProperties appProperties) {
		this.appProperties = appProperties;
	}

	public String createAccessToken(Authentication authentication, String secret) {
		User user = (User) authentication.getPrincipal();

		Instant now = Instant.now();
		Instant expiresAt = now.plusSeconds(ACCESS_TOKEN_EXPIRATION);

		return Jwts.builder()
				   	.setSubject(user.getId().toString())
				   	.setIssuedAt(Date.from(now))
				   	.claim("type", TokenType.ACCESS)
					.claim("secret", secret)
				   	.setExpiration(Date.from(expiresAt))
				   	.signWith(getKey())
				   	.compact();
	}

	public String createEmailVerificationToken(User user) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusSeconds(60 * 60);

		return Jwts.builder()
				   .setSubject(user.getId().toString())
				   .setIssuedAt(Date.from(now))
				   .claim("type", TokenType.VERIFICATION)
				   .setExpiration(Date.from(expiresAt))
				   .signWith(getKey())
				   .compact();
	}

	public String createRecoveryToken(User user) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusSeconds(60 * 60);

		return Jwts.builder()
				   .setSubject(user.getId().toString())
				   .setIssuedAt(Date.from(now))
				   .claim("type", TokenType.RECOVERY)
				   .setExpiration(Date.from(expiresAt))
				   .signWith(getKey())
				   .compact();
	}

	public UUID getUserIdFromToken(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
		Claims claims = parser.parseClaimsJws(token).getBody();

		return UUID.fromString(claims.getSubject());
	}

	public String getSecretFromToken(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
		Claims claims = parser.parseClaimsJws(token).getBody();
		if (claims.get("secret") == null) {
			throw new InvalidAccessTokenException("Invalid token.");
		}
		return claims.get("secret").toString();
	}

	public Claims readClaims(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
		return parser.parseClaimsJws(token).getBody();
	}

	public void validateToken(String token, TokenType tokenType) {
		try {
			Claims claims = readClaims(token);
			if (claims.get("type") == null || !claims.get("type").equals(tokenType.name())) {
				throw new InvalidTokenTypeException("Invalid token type.");
			}
		} catch (ExpiredJwtException e) {
			switch (tokenType) {
				case ACCESS -> throw new InvalidAccessTokenException("Access token has expired.");
				case VERIFICATION -> throw new InvalidVerificationTokenException("Verification link has expired.");
				case RECOVERY -> throw new InvalidRecoveryTokenException("Password reset link has expired.");
			}
		}
	}

	private Key getKey() {
		byte[] keyBytes = appProperties.getAuth().getTokenSecret().getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
