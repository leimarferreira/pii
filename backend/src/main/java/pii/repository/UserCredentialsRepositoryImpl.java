package pii.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.UserCredentialsDAO;
import pii.model.UserCredentials;

@Component
public class UserCredentialsRepositoryImpl implements UserCredentialsRepository {
	
	@Autowired
	private UserCredentialsDAO userCredentialsDAO;
	
	@Override
	public Optional<UserCredentials> findByUserId(Long id) {
		return userCredentialsDAO.findByUserId(id);
	}
	
	@Override
	public Optional<UserCredentials> findByUserEmail(String email) {
		return userCredentialsDAO.findByUserEmail(email);
	}
	
	@Override
	public Optional<UserCredentials> save(UserCredentials userCredentials) {
		var id = userCredentialsDAO.save(userCredentials);
		
		if (id.isPresent()) {
			return userCredentialsDAO.findById(id.get());
		}
		
		return Optional.empty();
	}
	
	@Override
	public Optional<UserCredentials> update(Long id, UserCredentials userCredentials) {
		userCredentialsDAO.update(id, userCredentials);
		return userCredentialsDAO.findById(id);
	}
	
	@Override
	public Boolean delete(Long id) {
		return userCredentialsDAO.delete(id);
	}
}
