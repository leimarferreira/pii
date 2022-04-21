package pii.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record UserDTO (
		Long id,
		@NotBlank(message = "O campo não pode estar em branco.")
		@NotEmpty(message = "O campo não pode estar vazio.")
		@NotNull(message = "O campo não pode ser nulo.")
		@Size(max = 255, message = "Tamanho máximo de 255 caracteres.")
		@JsonInclude(Include.NON_NULL)
		String name,
		@NotBlank(message = "O campo não pode estar em branco.")
		@NotEmpty(message = "O campo não pode estar vazio.")
		@NotNull(message = "O campo não pode ser nulo.")
		@Size(max = 255, message = "Tamanho máximo de 255 caracteres.")
		@Email(message = "O campo não possui um formato válido.")
		@JsonInclude(Include.NON_NULL)
		String email,
		@Size(max = 2048, message = "Tamanho máximo de 2048 caracteres.")
		@JsonInclude(Include.NON_NULL)
		String avatar
) {}
