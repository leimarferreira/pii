package pii.dto;

import java.math.BigDecimal;

public record InvoiceDTO(
		long id,
		String month,
		long cardId,
		BigDecimal value
) {}
