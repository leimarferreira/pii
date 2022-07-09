package pii.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pii.dto.ParcelDTO;
import pii.service.ParcelService;

@RestController
@RequestMapping("/parcel")
public class ParcelController {
	
	@Autowired
	private ParcelService service;
	
	@GetMapping("/invoice/{id}")
	public ResponseEntity<List<ParcelDTO>> findAllByInvoiceId(@PathVariable(name = "id") long invoiceId) {
		var result = service.findAllByInvoiceId(invoiceId);
		var status = HttpStatus.OK;
		
		if (result.isEmpty()) {
			status = HttpStatus.NO_CONTENT;
		}
		
		return ResponseEntity.status(status).body(result);
	}
}
