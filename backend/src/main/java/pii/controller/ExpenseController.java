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

import pii.dto.ExpenseDTO;
import pii.service.ExpenseService;

@RestController
@RequestMapping("/expense")
public class ExpenseController {
	
	@Autowired
	private ExpenseService service;
	
	@GetMapping("/all")
	public ResponseEntity<List<ExpenseDTO>> getAll() {
		var result = service.findAll();
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<List<ExpenseDTO>> getAllByUserId(@PathVariable(name = "id") Long userId) {
		var result = service.findAllByUserId(userId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/category/{id}")
	public ResponseEntity<List<ExpenseDTO>> getAllByCategoryId(@PathVariable(name = "id") Long categoryId) {
		var result = service.findAllByCategoryId(categoryId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<ExpenseDTO>> getById(@PathVariable(name = "id") Long id) {
		var result = service.findById(id);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PostMapping("/")
	public ResponseEntity<Optional<ExpenseDTO>> post(@RequestBody @Valid ExpenseDTO expenseDTO) {
		var result = service.save(expenseDTO);
		var status = HttpStatus.CREATED;
		
		if (result.isEmpty()) {
			status = HttpStatus.CONFLICT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Optional<ExpenseDTO>> put(@PathVariable(name = "id") Long id, @RequestBody @Valid ExpenseDTO expenseDTO) {
		var existingExpense = service.findById(id);
		
		var result = Optional.<ExpenseDTO>empty();
		var status = HttpStatus.I_AM_A_TEAPOT;
		
		if (existingExpense.isPresent()) {
			result = service.update(id, expenseDTO);
			status = HttpStatus.OK;
		} else {
			result = service.save(expenseDTO);
			status = HttpStatus.CREATED;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
		var result = service.delete(id);
		var status = HttpStatus.NOT_FOUND;
		
		if (result) {
			status = HttpStatus.OK;
		}
		
		return ResponseEntity.status(status).body(null);
	}
}
