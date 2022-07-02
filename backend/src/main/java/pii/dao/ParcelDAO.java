package pii.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.exception.UncheckedSQLException;
import pii.model.Parcel;

@Component
public class ParcelDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(ParcelDAO.class);
	
	public List<Parcel> findAllByExpenseId(long expenseId) {
		var query = "SELECT * FROM parcel WHERE expense_id = ?";
		
		try (var findAllByExpenseId = connection.prepareStatement(query)) {
			findAllByExpenseId.setLong(1, expenseId);
			
			var result = findAllByExpenseId.executeQuery();
			
			var parcels = new LinkedList<Parcel>();
			
			while (result.next()) {
				var parcel = new Parcel(
						result.getLong("id"),
						result.getLong("expense_id"),
						result.getLong("invoice_id"),
						result.getInt("parcel_number"),
						result.getLong("date"),
						result.getBigDecimal("value"));
				parcels.add(parcel);
			}
			
			return parcels;
		} catch (SQLException exception) {
			var message = String.format("Erro ao buscar parcelas da despesa com id = %d.", expenseId);
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public List<Parcel> findAllByInvoiceId(long invoiceId) {
		var query = "SELECT * FROM parcel WHERE invoice_id = ?";

		try (var findAllByExpenseId = connection.prepareStatement(query)) {
			findAllByExpenseId.setLong(1, invoiceId);
			
			var result = findAllByExpenseId.executeQuery();
			
			var parcels = new LinkedList<Parcel>();
			
			while (result.next()) {
				var parcel = new Parcel(
						result.getLong("id"),
						result.getLong("expense_id"),
						result.getLong("invoice_id"),
						result.getInt("parcel_number"),
						result.getLong("date"),
						result.getBigDecimal("value"));
				parcels.add(parcel);
			}
			
			return parcels;
		} catch (SQLException exception) {
			var message = String.format("Erro ao buscar parcelas da fatura com id = %d.", invoiceId);
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public Optional<Parcel> findById(long id) {
		var query = "SELECT * FROM parcel WHERE id = ?";
		
		try (var findParcel = connection.prepareStatement(query)) {
			findParcel.setLong(1, id);
			
			var result = findParcel.executeQuery();
			
			if (result.next()) {
				var parcel = new Parcel(
						result.getLong("id"),
						result.getLong("expense_id"),
						result.getLong("invoice_id"),
						result.getInt("parcel_number"),
						result.getLong("date"),
						result.getBigDecimal("value"));
				
				return Optional.of(parcel);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			var message = String.format("Falha ao obter parcela com id = %d.", id);
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public long save(Parcel parcel) {
		var statement = "INSERT INTO parcel (expense_id, invoice_id, parcel_number, date, value) VALUES (?, ?, ?, ?, ?)";
		
		try (var insertParcel = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
			insertParcel.setLong(1, parcel.expenseId());
			insertParcel.setLong(2, parcel.invoiceId());
			insertParcel.setInt(3, parcel.parcelNumber());
			insertParcel.setLong(4, parcel.dateAsLong());
			insertParcel.setBigDecimal(5, parcel.value());
			
			insertParcel.executeUpdate();
			
			var result = insertParcel.getGeneratedKeys();
			
			if (result.next()) {
				return result.getLong("GENERATED_KEY");
			}
			
			return 0l;
		} catch (SQLException exception) {
			var message = "Falha ao salvar parcela.";
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public boolean deleteAllByExpenseId(long expenseId) {
		var statement = "DELETE FROM parcel WHERE expense_id = ?";
		
		try (var deleteAllByExpenseId = connection.prepareStatement(statement)) {
			deleteAllByExpenseId.setLong(1, expenseId);
			
			var result = deleteAllByExpenseId.executeUpdate();
			
			return result > 0;
		} catch (SQLException exception) {
			var message = String.format("Falha ao remover parcelas da despesa de id = %d", expenseId);
			logger.error(message, exception);
			throw new UncheckedSQLException(message);
		}
	}
}
