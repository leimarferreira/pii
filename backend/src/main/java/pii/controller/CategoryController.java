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

import pii.dto.CategoryDTO;
import pii.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/all")
	public ResponseEntity<List<CategoryDTO>> getAll() {
		var result = categoryService.findAll();
		var status = HttpStatus.OK;

		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}

		return ResponseEntity.status(status).body(result);
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<Optional<CategoryDTO>> getById(@PathVariable(name = "id") Long id) {
		var result = categoryService.findById(id);
		var status = HttpStatus.OK;

		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}

		return ResponseEntity.status(status).body(result);
	}

	@GetMapping("/name")
	public ResponseEntity<Optional<CategoryDTO>> getByName(@PathVariable(name = "name") String name) {
		var result = categoryService.findByName(name);
		var status = HttpStatus.OK;

		if (result.isEmpty()) {
			status = HttpStatus.NOT_FOUND;
		}

		return ResponseEntity.status(status).body(result);
	}

	@PostMapping
	public ResponseEntity<Optional<CategoryDTO>> post(@RequestBody CategoryDTO category) {
		var result = categoryService.save(category);
		var status = HttpStatus.CREATED;

		if (result.isEmpty()) {
			status = HttpStatus.CONFLICT;
		}

		return ResponseEntity.status(status).body(result);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Optional<CategoryDTO>> put(@PathVariable(name = "id") Long id,
			@RequestBody @Valid CategoryDTO category) {
		var existingCategory = categoryService.findById(id);
		
		var result = Optional.<CategoryDTO>empty();
		var status = HttpStatus.OK;
		
		if (existingCategory.isPresent()) {
			result = categoryService.update(id, category);
		} else {
			result = categoryService.save(category);
			
			if (result.isPresent()) {
				status = HttpStatus.CREATED;
			} else {
				status = HttpStatus.CONFLICT;
			}
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<CategoryDTO>> delete(@PathVariable(name = "id") Long id) {
		var result = categoryService.delete(id);
		var status = HttpStatus.OK;
		
		if (!result) {
			status = HttpStatus.NOT_FOUND;
		}
		
		return ResponseEntity.status(status).body(Optional.empty());
	}
}
