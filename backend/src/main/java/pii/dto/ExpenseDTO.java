package pii.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public record ExpenseDTO(
		@Min(value = 1, message = "Valor mínimo é 1.")
		Long id,
		@Min(value = 1, message = "Valor mínimo é 1.")
		Long userId,
		BigDecimal value,
		@Size(max = 200, message = "Tamanho máximo de 200 caracteres.")
		String description,
		@Min(value = 1, message = "Valor mínimo é 1.")
		Long categoryId,
		@Min(value = 1, message = "Valor mínimo é 1.")
		Integer paymentMethod,
		@Min(value = 1, message = "Valor mínimo é 1.")
		Integer numberOfParcels, // apenas se for crédito
		Boolean isPaid,
		@Min(value = 0, message = "Valor mínimo é 1.")
		long cardId,
		Long dueDate
) {}
