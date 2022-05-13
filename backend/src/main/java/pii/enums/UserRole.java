package pii.enums;

import java.util.Arrays;
import java.util.Optional;

public enum UserRole {
	USER(1),
	ADMIN(2);
	
	private int value;
	
	UserRole(int value) {
		this.value = value;
	}
	
	public static Optional<UserRole> valueOf(int value) {
		return Arrays.stream(values())
				.filter(role -> role.value == value)
				.findFirst();
	}
	
	public Integer getValue() {
		return value;
	}
}
