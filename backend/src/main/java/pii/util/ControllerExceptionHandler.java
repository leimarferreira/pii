package pii.util;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pii.dto.ErrorResponseDTO;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		var response = new ErrorResponseDTO(HttpStatus.BAD_REQUEST, "Erro ao validar argumentos.");
		response.setValidationErrors(exception);
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorResponseDTO> handleUnknowExceptions(Exception exception) {
		var message = "Erro desconhecido.";
		var response = new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, message);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
}
