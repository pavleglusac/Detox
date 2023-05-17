package com.sbnz.admin.exception;

public class InvalidAccessTokenException extends RuntimeException {
	public InvalidAccessTokenException(String message) {
		super(message);
	}
}
