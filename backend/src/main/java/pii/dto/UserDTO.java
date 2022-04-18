package pii.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record UserDTO (
		Long id,
		@NotBlank
		@NotEmpty
		@NotNull
		@Size(max = 100)
		@JsonInclude(Include.NON_NULL)
		String name,
		@NotBlank
		@NotEmpty
		@NotNull
		@Size(max = 100)
		@JsonInclude(Include.NON_NULL)
		String email,
		@Size(max = 2048)
		@JsonInclude(Include.NON_NULL)
		String avatar
) {}
