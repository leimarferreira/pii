package pii.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pii.model.UserCredentials;
import pii.repository.UserCredentialsRepository;

@Service
public class UserCredentialsService implements UserDetailsService {

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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		var result = findByUserEmail(email);
		
		if (result.isEmpty()) {
			throw new UsernameNotFoundException("Usuário não encontrado no sistema.");
		}
		
		var userCredentials = result.get();
		return new User(email, userCredentials.password(),
				getAuthorities(userCredentials.role().name()));
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
	}
}
