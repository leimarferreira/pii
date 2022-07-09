package pii.model;

import java.math.BigDecimal;

public record Invoice(
	long id,
	String month,
	long cardId,
	BigDecimal value
) {}
