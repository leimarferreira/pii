package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.UserDAO;
import pii.model.User;

@Component
public class UserRepositoryImpl implements UserRepository {
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return userDAO.findById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userDAO.findByEmail(email);
	}

	@Override
	public Optional<User> save(User user) {
		var id = userDAO.save(user);
		
		if (id.isPresent()) {
			return userDAO.findById(id.get());
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<User> update(Long id, User user) {
		userDAO.update(id, user);
		return userDAO.findById(id);
	}

	@Override
	public boolean delete(Long id) {
		return userDAO.delete(id);
	}
}
