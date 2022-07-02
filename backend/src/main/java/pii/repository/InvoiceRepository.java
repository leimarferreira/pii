package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.Invoice;

@Repository
public interface InvoiceRepository {
	
	public List<Invoice> findAll();
	
	public List<Invoice> findAllByCardId(long cardId);
	
	public Optional<Invoice> findById(long id);
	
	public Optional<Invoice> findByMonthAndCardId(String month, long cardId);
	
	public Optional<Invoice> save(Invoice invoice);
	
	public Optional<Invoice> update(long id, Invoice invoice);
	
	public boolean delete(long id);
}
