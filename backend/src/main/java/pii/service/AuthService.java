package pii.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pii.dto.AuthDTO;
import pii.dto.JwtTokenDTO;
import pii.dto.UserDTO;
import pii.enums.UserRole;
import pii.exception.NotFoundException;
import pii.model.UserCredentials;
import pii.util.security.JWTUtil;

@Service
public class AuthService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserCredentialsService userCredentialService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public Optional<JwtTokenDTO> register(AuthDTO authDto) {
		var userDto = new UserDTO(authDto.name(), authDto.email());
		
		var saveUserResult = userService.save(userDto);
		
		if (saveUserResult.isPresent()) {
			var user = saveUserResult.get();
			
			var encodedPassword = passwordEncoder.encode(authDto.password());
			var userCredentials = new UserCredentials(user.id(), user.email(), encodedPassword, UserRole.USER);
			
			try {
				var saveUserCredentialsResult = userCredentialService.save(userCredentials);
				
				if (saveUserCredentialsResult.isEmpty()) {
					userService.delete(user.id());
					return Optional.empty();
				}
			} catch (Exception exception) {
				userService.delete(user.id());
				throw exception;
			}
			
			return Optional.of(new JwtTokenDTO(jwtUtil.generateToken(user.email()), userCredentials.role().name()));
		}
		
		return Optional.empty();
	}
	
	public Optional<JwtTokenDTO> registerAdmin(AuthDTO authDto) {
		var userDto = new UserDTO(authDto.name(), authDto.email());

		var saveUserResult = userService.save(userDto);

		if (saveUserResult.isPresent()) {
			var user = saveUserResult.get();

			var encodedPassword = passwordEncoder.encode(authDto.password());
			var userCredentials = new UserCredentials(user.id(), user.email(), encodedPassword, UserRole.ADMIN);

			try {
				var saveUserCredentialsResult = userCredentialService.save(userCredentials);

				if (saveUserCredentialsResult.isEmpty()) {
					userService.delete(user.id());
					return Optional.empty();
				}
			} catch (Exception exception) {
				userService.delete(user.id());
				throw exception;
			}

			return Optional.of(new JwtTokenDTO(jwtUtil.generateToken(user.email()), userCredentials.role().name()));
		}

		return Optional.empty();
	}
	
	public Optional<JwtTokenDTO> login(AuthDTO dto) {
		var credentials = userCredentialService.findByUserEmail(dto.email());
		
		if (credentials.isPresent()) {
			var authInputToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
			authenticationManager.authenticate(authInputToken);
			
			return Optional.of(new JwtTokenDTO(jwtUtil.generateToken(dto.email()), credentials.get().role().name()));
		}
		
		return Optional.empty();
	}
	
	public Optional<JwtTokenDTO> updateCredentials(Long userId, AuthDTO credential) {
		var currentCredentials = userCredentialService.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException("Credenciais de usuário não encontradas no sistema."));
		
		var newCredentials = new UserCredentials(currentCredentials.id(), credential.email(), credential.password(), currentCredentials.role());
		var result = userCredentialService.update(currentCredentials.id(), newCredentials);
		
		if (result.isPresent()) {
			var updatedCredentials = result.get();
			var authDto = new AuthDTO(null, updatedCredentials.email(), updatedCredentials.password(), null);
			return login(authDto);
		}
		
		return Optional.empty();
	}
	
	public Optional<JwtTokenDTO> updatePassword(Long userId, AuthDTO credentials) {
		var currentCredentials = userCredentialService.findByUserId(userId)
				.orElseThrow(() -> new NotFoundException("Credenciais de usuário não encontradas no sistema."));
		
		var encodedPassword = passwordEncoder.encode(credentials.password());
		var newCredentials = new UserCredentials(currentCredentials.id(), currentCredentials.email(), encodedPassword, currentCredentials.role());
		var result = userCredentialService.update(currentCredentials.id(), newCredentials);
		
		if (result.isPresent()) {
			var updatedCredentials = result.get();
			
			var authDto = new AuthDTO(null, updatedCredentials.email(), credentials.password(), null);
			return login(authDto);
		}
		
		return Optional.empty();
	}
}
