package com.openclassrooms.chatop.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.openclassrooms.chatop.dto.ErrorResponse;

/**
 * Turns business and validation exceptions into JSON error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleValidation(MethodArgumentNotValidException exception) {
		String message = exception.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.collect(Collectors.joining(", "));
		return new ErrorResponse(message);
	}

	@ExceptionHandler(EmailAlreadyUsedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleEmailAlreadyUsed(EmailAlreadyUsedException exception) {
		return new ErrorResponse(exception.getMessage());
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMissingPart(MissingServletRequestPartException exception) {
		return new ErrorResponse(exception.getRequestPartName() + " is required");
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleBadRequest(BadRequestException exception) {
		return new ErrorResponse(exception.getMessage());
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleInvalidCredentials(InvalidCredentialsException exception) {
		return new ErrorResponse(exception.getMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleAccessDenied(AccessDeniedException exception) {
		return new ErrorResponse(exception.getMessage());
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(ResourceNotFoundException exception) {
		return new ErrorResponse(exception.getMessage());
	}

}
