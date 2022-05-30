package pii.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record Income(
		Long id,
		Long userId,
		BigDecimal value,
		LocalDateTime date,
		String description,
		Long categoryId
) {
	public Income(Long id, Long userId, BigDecimal value, Long date, String description, Long categoryId) {
		this(
				id,
				userId,
				value,
				LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC),
				description,
				categoryId
		);
	}
	
	public Long dateAsLong() {
		return date.toEpochSecond(ZoneOffset.UTC);
	}
}
