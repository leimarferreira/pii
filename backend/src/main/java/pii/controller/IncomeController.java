package pii.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import pii.dto.IncomeDTO;
import pii.service.IncomeService;

@RestController
@RequestMapping("/income")
public class IncomeController {
	
	@Autowired
	private IncomeService service;
	
	@GetMapping("/all")
	public ResponseEntity<List<IncomeDTO>> getAll() {
		var result = service.findAll();
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/all/user/{id}")
	public ResponseEntity<List<IncomeDTO>> getAllByUserId(@PathVariable(name = "id") Long userId) {
		var result = service.findAllByUserId(userId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/all/category/{id}")
	public ResponseEntity<List<IncomeDTO>> getAllByCategoryId(@PathVariable(name = "id") Long categoryId) {
		var result = service.findAllByCategoryId(categoryId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Optional<IncomeDTO>> getById(@PathVariable Long id) {
		var result = service.findById(id);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PostMapping
	public ResponseEntity<Optional<IncomeDTO>> postIncome(@Valid @RequestBody IncomeDTO income) {
		var result = service.save(income);
		var status = HttpStatus.CREATED;
		
		if (result.isEmpty()) {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Optional<IncomeDTO>> putIncome(@PathVariable Long id, @Valid @RequestBody IncomeDTO income) {
		var existingIncome = service.findById(id);
		
		var result = Optional.<IncomeDTO>empty();
		var status = HttpStatus.OK;
		
		if (existingIncome.isPresent()) {
			result = service.update(id, income);
		} else {
			result = service.save(income);
			status = HttpStatus.CREATED;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<Void>> deleteIncome(@PathVariable Long id) {
		var result = service.delete(id);
		var status = HttpStatus.OK;
		
		if (!result) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(Optional.empty());
	}
}
