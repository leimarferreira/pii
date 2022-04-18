package pii.controller;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pii.dto.UserDTO;
import pii.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<Iterable<UserDTO>> getAll() {
		var result = userService.findAll();
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Optional<UserDTO>> getById(@PathVariable Long id) {
		var result = userService.findById(id);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/email/{email}")
	public ResponseEntity<Optional<UserDTO>> getByEmail(@PathVariable @Size(max = 255) String email) {
		var result = userService.findByEmail(email);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PostMapping
	public ResponseEntity<Optional<UserDTO>> post(@Valid @RequestBody UserDTO user) {
		var result = userService.save(user);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Optional<UserDTO>> update(@PathVariable Long id, @Valid @RequestBody UserDTO user) {
		var result = userService.update(id, user);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<Void>> delete(@PathVariable Long id) {
		var result = userService.delete(id);
		var status = HttpStatus.OK;
		
		if (!result) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(Optional.empty());
	}
}
