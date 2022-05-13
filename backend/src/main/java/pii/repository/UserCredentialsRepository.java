package pii.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.UserCredentials;

@Repository
public interface UserCredentialsRepository {

	Optional<UserCredentials> findByUserId(Long userId);

	Optional<UserCredentials> findByUserEmail(String email);

	Optional<UserCredentials> save(UserCredentials userCredentials);

	Optional<UserCredentials> update(Long id, UserCredentials userCredentials);

	Boolean delete(Long id);
}
