package pii.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public record IncomeDTO(
		@Min(value = 1, message = "Valor mínimo é 1.")
		Long id,
		@Min(value = 1, message = "Valor mínimo é 1.")
		Long userId,
		BigDecimal value,
		@Max(value = Long.MAX_VALUE, message = "Valor máximo excedido.")
		@Min(value = 1, message = "Valor mínimo é 1.")
		Long date,
		@Size(max = 200, message = "Tamanho máximo de 200 caracteres.")
		String description,
		@Min(value = 1, message = "Valor mínimo é 1.")
		Long categoryId
) {}
