package pii.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/register")
	public ResponseEntity<Optional<JwtTokenDTO>> register(@Valid @RequestBody AuthDTO dto) {
		var result = service.register(dto);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Optional<JwtTokenDTO>> login(@RequestBody AuthDTO dto) {
		var result = service.login(dto);
		var status = HttpStatus.UNAUTHORIZED;
		
		if (result.isPresent()) {
			status = HttpStatus.OK;
		}
		
		return ResponseEntity.status(status).body(result);
	}
}
