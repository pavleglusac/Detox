package com.sbnz.admin.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 600L;
	private final Object principal;
	private Object credentials;

	private Object loginToken;

	private Object secret;

	public CustomAuthenticationToken(Object principal, Object credentials, Object loginToken, Object secret) {
		super((Collection)null);
		this.principal = principal;
		this.credentials = credentials;
		this.loginToken = loginToken;
		this.secret = secret;
		this.setAuthenticated(false);
	}

	public CustomAuthenticationToken(Object principal, Object credentials, Object loginToken, Object secret, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		this.loginToken = loginToken;
		this.secret = secret;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public Object getLoginToken() {
		return loginToken;
	}

	public Object getSecret() {
		return secret;
	}
}
