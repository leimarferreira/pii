package pii.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.model.UserCredentials;
import pii.repository.UserCredentialsRepository;

@Service
public class UserCredentialsService {

	@Autowired
	private UserCredentialsRepository userCredentialsRepository;

	public Optional<UserCredentials> findByUserId(Long id) {
		return userCredentialsRepository.findByUserId(id);
	}

	public Optional<UserCredentials> findByUserEmail(String email) {
		return userCredentialsRepository.findByUserEmail(email);
	}

	public Optional<UserCredentials> save(UserCredentials userCredentials) {
		return userCredentialsRepository.save(userCredentials);
	}

	public Optional<UserCredentials> update(Long id, UserCredentials userCredentials) {
		return userCredentialsRepository.update(id, userCredentials);
	}

	public Boolean delete(Long id) {
		return userCredentialsRepository.delete(id);
	}
}
