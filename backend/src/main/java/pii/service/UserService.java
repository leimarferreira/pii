package pii.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.UserDTO;
import pii.dto.UserDTOMapper;
import pii.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	@Autowired
	private UserDTOMapper mapper;
	
	public List<UserDTO> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::toDTO)
				.toList();
	}
	
	public Optional<UserDTO> findById(Long id) {
		var user = repository.findById(id);
		
		if (user != null) {
			var dto = mapper.toDTO(user);
			return Optional.of(dto);
		}
		
		return Optional.empty();
	}
	
	public Optional<UserDTO> findByEmail(String email) {
		var user = repository.findByEmail(email);
		
		if (user != null) {
			var dto = mapper.toDTO(user);
			return Optional.of(dto);
		}
		
		return Optional.empty();
	}
	
	public Optional<UserDTO> save(UserDTO userDTO) {
		var user = mapper.toObj(userDTO);
		var result = repository.save(user);
		
		if (result != null) {
			var dto = mapper.toDTO(result);
			return Optional.of(dto);
		}
		
		return Optional.empty();
	}
	
	public Optional<UserDTO> update(Long id, UserDTO userDTO) {
		var user = mapper.toObj(userDTO);
		var result = repository.update(id, user);
		
		if (result != null) {
			var dto = mapper.toDTO(result);
			return Optional.of(dto);
		}
		
		return Optional.empty();
	}
	
	public boolean delete(Long id) {
		return repository.delete(id);
	}
}
