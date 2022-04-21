package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.User;

@Repository
public interface UserRepository {

	public List<User> findAll();

	public Optional<User> findById(Long id);

	public Optional<User> findByEmail(String email);

	public Optional<User> save(User user);

	public Optional<User> update(Long id, User user);

	public boolean delete(Long id);
}
