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
		if (exc.getCode() == "SGR-001") {

			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
		}
	}

	@ExceptionHandler(AuthorizationFailedException.class)
	public ResponseEntity<ErrorResponse> authorizationfailedException(AuthorizationFailedException exc,
			WebRequest request) {
		if (exc.getCode() == "ATHR-001" || exc.getCode() == "ATHR-003" || exc.getCode() == "ATHR-002") {

			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.FORBIDDEN);
		} else {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
		}
	}

	@ExceptionHandler(SignUpRestrictedException.class)
	public ResponseEntity<ErrorResponse> signuprestrictedException(SignUpRestrictedException exc, WebRequest request) {
		if (exc.getCode() == "SGR-002" || exc.getCode() == "SGR-001") {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.CONFLICT);

		} else {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
		}
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
	public ResponseEntity<ErrorResponse> signoutrestrictedException(SignOutRestrictedException exc,
			WebRequest request) {
		if (exc.getCode() == "SGR-001") {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
		} else {
			return new ResponseEntity<ErrorResponse>(
					new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()), HttpStatus.UNAUTHORIZED);
		}
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> usernotfoundException(UserNotFoundException exc, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exc.getCode()).message(exc.getErrorMessage()),
				HttpStatus.NOT_FOUND);
	}

}