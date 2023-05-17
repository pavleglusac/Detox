package com.sbnz.admin.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
	private String accessToken;
	private Long expiresAt;
}
