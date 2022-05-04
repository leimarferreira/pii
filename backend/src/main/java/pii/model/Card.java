package pii.model;

import java.math.BigDecimal;

import pii.enums.CardType;

public record Card(
		Long id,
		Long userId,
		String number,
		CardType type,
		String brand,
		BigDecimal limit, // se for débito, o limite é a receita do mês
		BigDecimal currentValue, // apenas crédito
		int dueDate // dia do fechamento
) {
	
	public Card(Long id, Long userId, String number, int type, String brand,
			BigDecimal limit, BigDecimal currentValue, int dueDate) {
		this(id, userId, number, CardType.valueOf(type).orElse(CardType.CREDIT),
				brand, limit, currentValue, dueDate);
	}
}
