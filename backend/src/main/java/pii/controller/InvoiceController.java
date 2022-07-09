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
	
	@GetMapping("/card/{id}")
	public ResponseEntity<List<InvoiceDTO>> getAllByCardId(@PathVariable(name = "id") long cardId) {
		var result = service.findAllByCardId(cardId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
}
