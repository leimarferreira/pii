package pii.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

import pii.enums.PaymentMethod;

public record Expense(
		Long id,
		Long userId,
		BigDecimal value,
		String description,
		Long categoryId,
		PaymentMethod paymentMethod,
		Integer numberOfParcels, // apenas se for crédito
		Boolean isPaid,
		long cardId,
		LocalDate dueDate
) {
	public Expense(Long id, Long userId, BigDecimal value, String description, Long categoryId, Integer paymentMethod, Integer numberOfParcels, Boolean isPaid, long cardId, Long dueDate) {
		this(id, userId, value, description, categoryId, PaymentMethod.valueOf(paymentMethod).get(), numberOfParcels,
				isPaid, cardId, LocalDate.ofInstant(Instant.ofEpochSecond(dueDate), ZoneOffset.UTC));
	}
	
	public long dueDateAsLong() {
		return dueDate.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
	}
}
