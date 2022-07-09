package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.InvoiceDAO;
import pii.model.Invoice;

@Component
public class InvoiceRepositoryImpl implements InvoiceRepository {
	
	@Autowired
	private InvoiceDAO invoiceDAO;

	@Override
	public List<Invoice> findAll() {
		return invoiceDAO.findAll();
	}

	@Override
	public List<Invoice> findAllByCardId(long cardId) {
		return invoiceDAO.findAllByCardId(cardId);
	}

	@Override
	public Optional<Invoice> findById(long id) {
		return invoiceDAO.findById(id);
	}

	@Override
	public Optional<Invoice> findByMonthAndCardId(String month, long cardId) {
		return invoiceDAO.findByMonthAndCardId(month, cardId);
	}

	@Override
	public Optional<Invoice> save(Invoice invoice) {
		var saveResult = invoiceDAO.save(invoice);
		
		if (saveResult.isPresent()) {
			var id = saveResult.get();
			return invoiceDAO.findById(id);
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<Invoice> update(long id, Invoice invoice) {
		if (invoiceDAO.update(id, invoice)) {
			return invoiceDAO.findById(id);
		}
		
		return Optional.empty();
	}

	@Override
	public boolean delete(long id) {
		return invoiceDAO.delete(id);
	}

	@Override
	public boolean deleteAllEmptyInvoices() {
		return invoiceDAO.deleteAllEmptyInvoices();
	}

}
