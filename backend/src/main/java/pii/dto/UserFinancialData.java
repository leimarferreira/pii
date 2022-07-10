package pii.dto;

import java.math.BigDecimal;

public record UserFinancialData (
	long userId,
	BigDecimal creditExpenses,
	BigDecimal debitExpenses,
	BigDecimal moneyExpenses,
	BigDecimal totalExpenses,
	BigDecimal totalIncomes,
	BigDecimal totalCredit,
	BigDecimal balance
) {}
