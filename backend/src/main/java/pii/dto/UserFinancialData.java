package pii.dto;

import java.math.BigDecimal;

public record UserFinancialData (
	long userId,
	BigDecimal totalExpenses,
	BigDecimal totalIncomes,
	BigDecimal totalCards,
	BigDecimal balance
) {}
