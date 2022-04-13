package pii.dto;

import org.springframework.stereotype.Component;

import pii.model.User;

@Component
public class UserDTOMapper {
	public UserDTO toDTO(User user) {
		return new UserDTO(user.id(), user.name(), user.email(), user.avatar());
	}
	
	public User toObj(UserDTO dto) {
		return new User(dto.id(), dto.name(), dto.email(), dto.avatar());
	}
}
