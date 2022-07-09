package pii.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pii.dto.InvoiceDTO;
import pii.dto.InvoiceDTOMapper;
import pii.model.Invoice;
import pii.repository.InvoiceRepository;

@Service
public class InvoiceService {
	
	@Autowired
	private InvoiceRepository repository;
	
	@Autowired
	private InvoiceDTOMapper dtoMapper;
	
	public List<InvoiceDTO> findAll() {
		return repository
				.findAll()
				.stream()
				.map(dtoMapper::objToDTO)
				.toList();
	}
	
	public List<InvoiceDTO> findAllByCardId(long cardId) {
		return repository
				.findAllByCardId(cardId)
				.stream()
				.map(dtoMapper::objToDTO)
				.toList();
	}
	
	public Optional<InvoiceDTO> findById(long id) {
		return repository.findById(id)
				.map(dtoMapper::objToDTO);
	}
	
	public Optional<InvoiceDTO> save(InvoiceDTO invoiceDTO) {
		var invoice = dtoMapper.dtoToObj(invoiceDTO);
		
		return repository.save(invoice)
				.map(dtoMapper::objToDTO);
	}
	
	public Optional<InvoiceDTO> update(long id, InvoiceDTO invoiceDTO) {
		var invoice = dtoMapper.dtoToObj(invoiceDTO);
		
		return repository.update(id, invoice)
				.map(dtoMapper::objToDTO);
	}
	
	public boolean delete(long id) {
		return repository.delete(id);
	}
	
	public boolean deleteAllEmptyInvoices() {
		return repository.deleteAllEmptyInvoices();
	}
	
	public Optional<Invoice> getInvoiceByMonthAndCardId(String month, long cardId) {
		var result = repository.findByMonthAndCardId(month, cardId);
		
		if (result.isPresent()) {
			return result;
		}
		
		var invoice = new Invoice(0, month, cardId, new BigDecimal(0));
				
		return repository.save(invoice);
	}
}
