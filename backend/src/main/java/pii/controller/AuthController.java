package pii.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pii.dto.AuthDTO;
import pii.dto.JwtTokenDTO;
import pii.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@PostMapping("/public/register")
	public ResponseEntity<Optional<JwtTokenDTO>> register(@Valid @RequestBody AuthDTO dto) {
		var result = service.register(dto);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PostMapping("/admin/register")
	public ResponseEntity<Optional<JwtTokenDTO>> registerAdmin(@Valid @RequestBody AuthDTO dto) {
		var result = service.registerAdmin(dto);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PostMapping("/public/login")
	public ResponseEntity<Optional<JwtTokenDTO>> login(@RequestBody AuthDTO dto) {
		var result = service.login(dto);
		var status = HttpStatus.UNAUTHORIZED;
		
		if (result.isPresent()) {
			status = HttpStatus.OK;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateToken() {
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	@PutMapping("/credentials/password/{id}")
	public ResponseEntity<Optional<JwtTokenDTO>> updatePassword(@PathVariable(name = "id") Long userId, @RequestBody AuthDTO dto) {
		return ResponseEntity.status(HttpStatus.OK).body(service.updatePassword(userId, dto));
	}
}
