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
import pii.model.Expense;

@Component
public class ExpenseDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(ExpenseDAO.class);
	
	public List<Expense> findAll() {
		var query = "SELECT * FROM expenses";
		
		try (var findAll = connection.prepareStatement(query)) {
			var result = findAll.executeQuery();
			
			var expenses = new LinkedList<Expense>();
			while (result.next()) {
				var expense = new Expense(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getBigDecimal("value"),
						result.getString("description"),
						result.getLong("category_id"),
						result.getInt("payment_method"),
						result.getInt("number_of_parcels"),
						result.getBoolean("is_paid"),
						result.getLong("card_id"),
						result.getLong("due_date"));
				
				expenses.add(expense);
			}
			
			return expenses;
		} catch (SQLException exception) {
			logger.error("Falha ao buscar despesas no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar despesas.", exception);
		}
	}
	
	public List<Expense> findAllByUserId(Long userId) {
		var query = "SELECT * FROM expenses WHERE user_id = ?";
		
		try (var findAllByUserId = connection.prepareStatement(query)) {
			findAllByUserId.setLong(1, userId);
			
			var result = findAllByUserId.executeQuery();
			
			var expenses = new LinkedList<Expense>();
			while (result.next()) {
				var expense = new Expense(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getBigDecimal("value"),
						result.getString("description"),
						result.getLong("category_id"),
						result.getInt("payment_method"),
						result.getInt("number_of_parcels"),
						result.getBoolean("is_paid"),
						result.getLong("card_id"),
						result.getLong("due_date"));
				
				expenses.add(expense);
			}
			
			return expenses;
		} catch (SQLException exception) {
			logger.error("Falha ao buscar despesas no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar despesas.", exception);
		}
	}
	
	public List<Expense> findAllByCategoryId(Long categoryId) {
		var query = "SELECT * FROM expenses WHERE category_id = ?";
		
		try (var findAllByCategoryId = connection.prepareStatement(query)) {
			findAllByCategoryId.setLong(1, categoryId);
			
			var result = findAllByCategoryId.executeQuery();
			
			var expenses = new LinkedList<Expense>();
			while (result.next()) {
				var expense = new Expense(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getBigDecimal("value"),
						result.getString("description"),
						result.getLong("category_id"),
						result.getInt("payment_method"),
						result.getInt("number_of_parcels"),
						result.getBoolean("is_paid"),
						result.getLong("card_id"),
						result.getLong("due_date"));
				
				expenses.add(expense);
			}
			
			return expenses;
		} catch (SQLException exception) {
			logger.error("Falha ao buscar despesas no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar despesas.", exception);
		}
	}
	
	public Optional<Expense> findById(Long id) {
		var query = "SELECT * FROM expenses WHERE id = ?";
		
		try (var findById = connection.prepareStatement(query)) {
			findById.setLong(1, id);
			
			var result = findById.executeQuery();
			
			if (result.next()) {
				var expense = new Expense(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getBigDecimal("value"),
						result.getString("description"),
						result.getLong("category_id"),
						result.getInt("payment_method"),
						result.getInt("number_of_parcels"),
						result.getBoolean("is_paid"),
						result.getLong("card_id"),
						result.getLong("due_date"));
				
				return Optional.of(expense);
			}
			return Optional.empty();
		} catch (SQLException exception) {
			logger.error("Falha ao buscar despesa no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao despesa receita.", exception);
		}
	}
	
	public Optional<Long> save(Expense expense) {
		var statement = """
				INSERT INTO expenses (user_id, value, description, category_id, payment_method, number_of_parcels, is_paid, card_id, due_date)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";
		
		try (var insertExpense = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
			insertExpense.setLong(1, expense.userId());
			insertExpense.setBigDecimal(2, expense.value());
			insertExpense.setString(3, expense.description());
			insertExpense.setLong(4, expense.categoryId());
			insertExpense.setInt(5, expense.paymentMethod().getValue());
			insertExpense.setInt(6, expense.numberOfParcels());
			insertExpense.setBoolean(7, expense.isPaid());
			insertExpense.setLong(8, expense.cardId());
			insertExpense.setLong(9, expense.dueDate());
			
			insertExpense.executeUpdate();
			
			var result = insertExpense.getGeneratedKeys();
			result.next();
			return Optional.ofNullable(result.getLong("GENERATED_KEY"));
		} catch (SQLException exception) {
			logger.error("Erro ao inserir despesa no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao salvar despesa.", exception);
		}
	}
	
	public void update(Long id, Expense expense) {
		var statement = """
				UPDATE expenses SET
					user_id = ?,
					value = ?,
					description = ?,
					category_id = ?,
					payment_method = ?,
					number_of_parcels = ?,
					is_paid = ?,
					card_id = ?,
					due_date = ?
				WHERE id = ?
				""";
		
		try (var updateExpense = connection.prepareStatement(statement)) {
			updateExpense.setLong(1, expense.userId());
			updateExpense.setBigDecimal(2, expense.value());
			updateExpense.setString(3, expense.description());
			updateExpense.setLong(4, expense.categoryId());
			updateExpense.setInt(5, expense.paymentMethod().getValue());
			updateExpense.setInt(6, expense.numberOfParcels());
			updateExpense.setBoolean(7, expense.isPaid());
			updateExpense.setLong(8, expense.cardId());
			updateExpense.setLong(9, expense.dueDate());
			updateExpense.setLong(10, id);
			
			updateExpense.executeUpdate();
		} catch (SQLException exception) {
			logger.error("Erro ao atualizar despesa no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao atualizar despesa.", exception);
		}
	}
	
	public Boolean delete(Long id) {
		var statement = "DELETE FROM expenses WHERE id = ?";
		
		try (var deleteExpense = connection.prepareStatement(statement)) {
			deleteExpense.setLong(1, id);
			
			var result = deleteExpense.executeUpdate();
			return result > 0;
		} catch (SQLException exception) {
			logger.error("Falha ao deletar despesa no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao deletar despesa.", exception);
		}
	}
}
