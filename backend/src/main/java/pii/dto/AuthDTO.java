package pii.dto;

public record AuthDTO (
		String name,
		String email,
		String password,
		String role
) {}
