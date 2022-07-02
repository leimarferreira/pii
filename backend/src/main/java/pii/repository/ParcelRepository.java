package pii.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import pii.model.Parcel;

@Repository
public interface ParcelRepository {
	
	public List<Parcel> findAllByExpenseId(long expenseId);
	
	public List<Parcel> findAllByInvoiceId(long invoiceId);
	
	public Optional<Parcel> save(Parcel parcel);
	
	public boolean deleteAllByExpenseId(long id);
}
