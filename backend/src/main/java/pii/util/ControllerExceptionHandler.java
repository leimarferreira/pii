package pii.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pii.dto.ErrorResponseDTO;
import pii.exception.ConflictException;
import pii.exception.UncheckedSQLException;

@RestControllerAdvice
public class ControllerExceptionHandler {
	
	private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		var response = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Erro ao validar argumentos.");
		response.setValidationErrors(exception);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	
	@ExceptionHandler(UncheckedSQLException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponseDTO> handleUncheckedSQLException(UncheckedSQLException exception) {
		var response = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
		return ResponseEntity.status(response.getStatus()).body(response);
	}
	
	@ExceptionHandler(ConflictException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException exception) {
		var response = new ErrorResponseDTO(HttpStatus.CONFLICT, exception.getMessage());
		return ResponseEntity.status(response.getStatus()).body(response);
	}
	
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponseDTO> handleAuthenticationException(AuthenticationException exception) {
		var response = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED, exception.getMessage());
		return ResponseEntity.status(response.getStatus()).body(response);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponseDTO> handleUsernameNotFoundException(UsernameNotFoundException exception) {
		var response = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED, exception.getMessage());
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponseDTO> handleUnknowExceptions(Exception exception) {
		logger.error("Erro desconhecido.", exception);
		var response = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Erro desconhecido.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
