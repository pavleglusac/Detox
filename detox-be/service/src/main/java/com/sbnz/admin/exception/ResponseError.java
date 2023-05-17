package com.sbnz.admin.exception;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class ResponseError {
	private final Integer status;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final Map<String, String> errors;

	public ResponseError(Integer status, String message, Map<String, String> errors) {
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public ResponseError(Integer status, String message) {
		this.status = status;
		this.message = message;
		this.errors = null;
	}

	public ResponseError(HttpStatus status, String message, Map<String, String> errors) {
		this.status = status.value();
		this.message = message;
		this.errors = errors;
	}

	public ResponseError(HttpStatus status, String message) {
		this.status = status.value();
		this.message = message;
		this.errors = null;
	}
}
