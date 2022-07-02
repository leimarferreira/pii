package pii.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pii.dto.InvoiceDTO;
import pii.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
	
	@Autowired
	private InvoiceService service;
	
//	@GetMapping
//	public ResponseEntity<List<InvoiceDTO>> getAll() {
//		var result = service.findAll();
//		var status = HttpStatus.OK;
//		
//		if (result.isEmpty()) {
//			status = HttpStatus.NO_CONTENT;
//		}
//		
//		return ResponseEntity.status(status).body(result);
//	}
	
	@GetMapping("/card/{id}")
	public ResponseEntity<List<InvoiceDTO>> getAllByCardId(@PathVariable(name = "id") long cardId) {
		var result = service.findAllByCardId(cardId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
	
//	@GetMapping("/{id}")
//	public ResponseEntity<Optional<InvoiceDTO>> getById(@PathVariable(name = "id") long id) {
//		var result = service.findById(id);
//		var status = HttpStatus.OK;
//		
//		if (result.isEmpty()) {
//			status = HttpStatus.NOT_FOUND;
//		}
//		
//		return ResponseEntity.status(status).body(result);
//	}
//	
//	@PostMapping
//	public ResponseEntity<Optional<InvoiceDTO>> post(@RequestBody InvoiceDTO dto) {
//		var result = service.save(dto);
//		var status = HttpStatus.OK;
//		
//		if (result.isEmpty()) {
//			status = HttpStatus.BAD_REQUEST;
//		}
//		
//		return ResponseEntity.status(status).body(result);
//	}
//	
//	@PutMapping("/{id}")
//	public ResponseEntity<Optional<InvoiceDTO>> put(@PathVariable(name = "id") long id, @RequestBody InvoiceDTO dto) {
//		var existingInvoice = service.findById(id);
//		
//		var result = Optional.<InvoiceDTO>empty();
//		var status = HttpStatus.OK;
//		
//		if (existingInvoice.isPresent()) {
//			result = service.update(id, dto);
//		} else {
//			result = service.save(dto);
//			
//			if (result.isPresent()) {
//				status = HttpStatus.CREATED;
//			}
//		}
//		
//		if (result.isEmpty()) {
//			status = HttpStatus.BAD_REQUEST;
//		}
//		
//		return ResponseEntity.status(status).body(result);
//	}
//	
//	public ResponseEntity<Boolean> delete(long id) {
//		var result = service.delete(id);
//		var status = HttpStatus.NOT_FOUND;
//		
//		if (result) {
//			status = HttpStatus.OK;
//		}
//		
//		return ResponseEntity.status(status).body(result);
//	}
}
