package pii.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class ErrorResponseDTO {
	@JsonIgnore(value = true)
	private final HttpStatus status;
	@Size(max = 255)
	private final String message;
	private List<ValidationError> validationErrors;

	public ErrorResponseDTO(HttpStatus status, String message, List<ValidationError> errors) {
		this.status = status;
		this.message = message;
		this.validationErrors = errors;
	}

	public ErrorResponseDTO(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

	public List<ValidationError> getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(List<ValidationError> errors) {
		this.validationErrors = errors;
	}

	public void setValidationErrors(MethodArgumentNotValidException exception) {
		exception.getFieldErrors().forEach(error -> {
			this.addValidationError(error.getField(), error.getDefaultMessage());
		});
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	private void addValidationError(String field, String message) {
		if (Objects.isNull(validationErrors)) {
			validationErrors = new ArrayList<>();
		}

		validationErrors.add(new ValidationError(field, message));
	}

	private final record ValidationError(String field, String message) {}
}
