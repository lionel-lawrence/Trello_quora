package com.upgrad.quora.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exc,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(AuthorizationFailedException.class)
	public ResponseEntity<ErrorResponse> authorizationfailedException(AuthorizationFailedException exc,
			WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(SignUpRestrictedException.class)
	public ResponseEntity<ErrorResponse> signuprestrictedException(SignUpRestrictedException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(AnswerNotFoundException.class)
	public ResponseEntity<ErrorResponse> answernotfoundException(AnswerNotFoundException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidQuestionException.class)
	public ResponseEntity<ErrorResponse> invalidquestionException(InvalidQuestionException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(SignOutRestrictedException.class)
	public ResponseEntity<ErrorResponse> signoutrestrictedException(SignOutRestrictedException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.UNAUTHORIZED);
	}
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> usernotfoundException(UserNotFoundException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

}