package com.sbnz.detox.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
	private final Auth auth = new Auth();
	@Getter
	@Setter
	public static class Auth {
		private String tokenSecret;
	}


}