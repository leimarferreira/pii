package pii.dto;

import org.springframework.stereotype.Component;

import pii.model.Invoice;

@Component
public class InvoiceDTOMapper {
	
	public InvoiceDTO objToDTO(Invoice invoice) {
		return new InvoiceDTO(
				invoice.id(),
				invoice.month(),
				invoice.cardId(),
				invoice.value());
	}
	
	public Invoice dtoToObj(InvoiceDTO dto) {
		return new Invoice(
				dto.id(),
				dto.month(),
				dto.cardId(),
				dto.value());
	}
}
