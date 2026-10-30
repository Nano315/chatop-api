package com.openclassrooms.chatop.exception;

public class BadRequestException extends RuntimeException {

	public BadRequestException(String message) {
		super(message);
	}

}
