package pii.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.exception.UncheckedSQLException;
import pii.model.Invoice;

@Component
public class InvoiceDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(InvoiceDAO.class);
	
	public List<Invoice> findAll() {
		var query = "SELECT * FROM invoices";
		
		try (var selectAll = connection.prepareStatement(query)) {
			var result = selectAll.executeQuery();
			
			var invoices = new LinkedList<Invoice>();
			while (result.next()) {
				var invoice = new Invoice(
						result.getLong("id"),
						result.getString("month"),
						result.getLong("card_id"),
						result.getBigDecimal("value"));
				invoices.add(invoice);
			}
			
			return invoices;
		} catch (SQLException exception) {
			var message = "Erro ao buscar faturas.";
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public List<Invoice> findAllByCardId(long cardId) {
		var query = "SELECT * FROM invoices WHERE card_id = ?";

		try (var selectAll = connection.prepareStatement(query)) {
			selectAll.setLong(1, cardId);
			var result = selectAll.executeQuery();

			var invoices = new LinkedList<Invoice>();
			while (result.next()) {
				var invoice = new Invoice(
						result.getLong("id"),
						result.getString("month"),
						result.getLong("card_id"),
						result.getBigDecimal("value"));
				invoices.add(invoice);
			}

			return invoices;
		} catch (SQLException exception) {
			var message = "Erro ao buscar faturas.";
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
// TODO: ver se precisa desse método mesmo
//	public List<Invoice> findAllByExpenseId() {
//		
//	}
	
	public Optional<Invoice> findById(long id) {
		var query = "SELECT * FROM invoices WHERE id = ?";
		
		try (var findInvoice = connection.prepareStatement(query)) {
			findInvoice.setLong(1, id);
			
			var result = findInvoice.executeQuery();
			if (result.next()) {
				var invoice = new Invoice(
						result.getLong("id"),
						result.getString("month"),
						result.getLong("card_id"),
						result.getBigDecimal("value"));
				
				return Optional.of(invoice);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			var message = String.format("Erro ao buscar fatura com id = %d.", id);
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public Optional<Invoice> findByMonthAndCardId(String month, long cardId) {
		var query = "SELECT * FROM invoices WHERE month = ? AND card_id = ?";
		
		try (var findInvoice = connection.prepareStatement(query)) {
			findInvoice.setString(1, month);
			findInvoice.setLong(2, cardId);
			
			var result = findInvoice.executeQuery();
			
			if (result.next()) {
				var invoice = new Invoice(
						result.getLong("id"),
						result.getString("month"),
						result.getLong("card_id"),
						result.getBigDecimal("value"));
				
				return Optional.of(invoice);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			var message = String.format("Erro ao buscar fatura do cartão com id = ? e mês = %s.", cardId, month);
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public Optional<Long> save(Invoice invoice) {
		var statement = "INSERT INTO invoices (month, card_id, value) VALUES (?, ?, ?)";
		
		try (var insertInvoice = connection.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
			insertInvoice.setString(1, invoice.month());
			insertInvoice.setLong(2, invoice.cardId());
			insertInvoice.setBigDecimal(3, invoice.value());
			
			insertInvoice.executeUpdate();
			
			var result = insertInvoice.getGeneratedKeys();
			result.next();
			return Optional.ofNullable(result.getLong("GENERATED_KEY"));
		} catch (SQLException exception) {
			var message = "Erro ao salvar fatura no banco de dados.";
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
	
	public boolean update(long id, Invoice invoice) {
		var statement = """
				UPDATE invoices SET
					month = ?,
					card_id = ?,
					value = ?
				WHERE id = ?
				""";
		
		try (var updateInvoice = connection.prepareStatement(statement)) {
			updateInvoice.setString(1, invoice.month());
			updateInvoice.setLong(2, invoice.cardId());
			updateInvoice.setBigDecimal(3, invoice.value());
			updateInvoice.setLong(4, id);
			
			var result = updateInvoice.executeUpdate();
			return result > 0;
		} catch (SQLException exception) {
			logger.error("Erro ao atualizar fatura.", exception);
			throw new UncheckedSQLException("Erro ao atualizar fatura.", exception);
		}
	}
	
	public boolean delete(long id) {
		var statement = "DELETE FROM invoices WHERE id = ?";
		
		try (var deleteInvoice = connection.prepareStatement(statement)) {
			deleteInvoice.setLong(1, id);
			
			var result = deleteInvoice.executeUpdate();
			return result > 0;
		} catch (SQLException exception) {
			var message = "Erro ao deletar fatura";
			logger.error(message, exception);
			throw new UncheckedSQLException(message, exception);
		}
	}
}
