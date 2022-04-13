package pii.model;

public record User(
		Long id,
		String name,
		String email,
		String avatar
) {}
