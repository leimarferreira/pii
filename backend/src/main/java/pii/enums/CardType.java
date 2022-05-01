package pii.enums;

import java.util.Arrays;
import java.util.Optional;

public enum CardType {
	CREDIT(1),
	DEBIT(2);
	
	private int value;
	
	CardType(int value) {
		this.value = value;
	}
	
	public static Optional<CardType> valueOf(int value) {
		return Arrays.stream(values())
				.filter(type -> type.value == value)
				.findFirst();
	}
	
	public Integer getValue() {
		return value;
	}
}
