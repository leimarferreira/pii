package pii.dto;

import java.math.BigDecimal;

public record ParcelDTO(
		long id,
		String description,
		long dueDate,
		int parcelNumber,
		int numberOfParcels,
		BigDecimal value
) {}
