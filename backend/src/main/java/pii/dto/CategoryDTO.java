package pii.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public record CategoryDTO(
		Long id,
		
		@Max(value = 50, message = "Tamanho máximo para o nome da categoria é 50 caracteres.")
		@NotEmpty(message = "Atributo é requerido.")
		String name
) {}