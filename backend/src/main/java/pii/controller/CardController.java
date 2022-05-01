package pii.controller;

import java.util.List;
import java.util.Optional;

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

import pii.dto.CardDTO;
import pii.service.CardService;

@RestController
@RequestMapping("/card")
public class CardController {
	
	@Autowired
	private CardService service;
	
	@GetMapping
	public ResponseEntity<List<CardDTO>> getAll() {
		var result = service.findAll();
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<List<CardDTO>> getAllByUserId(@PathVariable(name = "id") Long userId) {
		var result = service.findAllByUserId(userId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Optional<CardDTO>> getById(@PathVariable(name = "id") Long id) {
		var result = service.findById(id);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/number/{number}")
	public ResponseEntity<Optional<CardDTO>> getByNumber(@PathVariable(name = "number") Long number) {
		var result = service.findByNumber(number);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PostMapping
	public ResponseEntity<Optional<CardDTO>> post(@RequestBody CardDTO dto) {
		var result = service.save(dto);
		var status = HttpStatus.CREATED;
		
		if (result.isEmpty()) {
			status = HttpStatus.CONFLICT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Optional<CardDTO>> put(@PathVariable(name = "id") Long id, @RequestBody CardDTO dto) {
		var existingCard = service.findById(id);
		
		var result = Optional.<CardDTO>empty();
		var status = HttpStatus.OK;
		
		if (existingCard.isPresent()) {
			result = service.update(id, dto);
		} else {
			result = service.save(dto);
			status = HttpStatus.CREATED;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<CardDTO>> delete(@PathVariable Long id) {
		var result = service.delete(id);
		var status = HttpStatus.OK;
		
		if (!result) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(Optional.empty());
	}
}
