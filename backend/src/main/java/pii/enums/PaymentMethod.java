package pii.enums;

import java.util.Arrays;
import java.util.Optional;

public enum PaymentMethod {
	CREDIT(1),
	DEBIT(2),
	MONEY(3);
	
	private int value;
	
	PaymentMethod(Integer value) {
		this.value = value;
	}
	
	public static Optional<PaymentMethod> valueOf(int value) {
		return Arrays.stream(values())
				.filter(method -> method.value == value)
				.findFirst();
	}
	
	public Integer getValue() {
		return this.value;
	}
}
