package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dao.ParcelDAO;
import pii.model.Parcel;

@Component
public class ParcelRepositoryImpl implements ParcelRepository {
	
	@Autowired
	private ParcelDAO dao;

	@Override
	public List<Parcel> findAllByExpenseId(long expenseId) {
		return dao.findAllByExpenseId(expenseId);
	}
	
	@Override
	public List<Parcel> findAllByInvoiceId(long invoiceId) {
		return dao.findAllByInvoiceId(invoiceId);
	}

	@Override
	public Optional<Parcel> save(Parcel parcel) {
		var id = dao.save(parcel);
		
		if (id == 0l) {
			return Optional.empty();
		}
		
		return dao.findById(id);
	}

	@Override
	public boolean deleteAllByExpenseId(long id) {
		return dao.deleteAllByExpenseId(id);
	}
}
