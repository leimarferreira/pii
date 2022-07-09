package pii.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

public record Parcel(
		long id,
		long expenseId,
		long invoiceId,
		int parcelNumber,
		LocalDate date,
		BigDecimal value
) {
	public Parcel(long id, long expenseId, long invoiceId, int parcelNumber, long date, BigDecimal value) {
		this(id, expenseId, invoiceId, parcelNumber, LocalDate.ofInstant(Instant.ofEpochSecond(date), ZoneOffset.UTC), value);
	}
	
	public long dateAsLong() {
		return date.toEpochSecond(LocalTime.MIDNIGHT, ZoneOffset.UTC);
	}
}
