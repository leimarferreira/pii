package pii.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pii.model.User;

@Repository
public interface UserRepository {
	
	public List<User> findAll();
	public User findById(Long id);
	public User findByEmail(String email);
	public User save(User user);
	public User update(Long id, User user);
	public boolean delete(Long id);
}
