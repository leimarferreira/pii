package pii.model;

import pii.enums.UserRole;

public record UserCredentials(
		Long id,
		Long userId,
		String email,
		String password,
		String salt,
		UserRole role
) {

	public UserCredentials(Long id, Long userId, String email, String password, String salt, int role) {
		this(id, userId, email, password, salt, UserRole.valueOf(role).get());
	}
}
