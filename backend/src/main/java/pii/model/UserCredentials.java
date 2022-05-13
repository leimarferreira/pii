package pii.model;

import pii.enums.UserRole;

public record UserCredentials(
		Long id,
		Long userId,
		String email,
		String password,
		UserRole role
) {

	public UserCredentials(Long id, Long userId, String email, String password, int role) {
		this(id, userId, email, password, UserRole.valueOf(role).get());
	}
	
	public UserCredentials(Long userId, String email, String password, UserRole role) {
		this(null, userId, email, password, role);
	}
}
