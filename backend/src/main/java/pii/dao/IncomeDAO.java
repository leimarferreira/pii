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
import pii.model.Income;

@Component
public class IncomeDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(IncomeDAO.class);
	
	public List<Income> findAll() {
		var query = "SELECT * FROM incomes";
		
		try (var findAll = connection.prepareStatement(query)) {
			var result = findAll.executeQuery();
			
			var incomes = new LinkedList<Income>();
			while (result.next()) {
					var income = new Income(
							result.getLong("id"),
							result.getLong("user_id"),
							result.getBigDecimal("value"),
							result.getLong("date"),
							result.getString("description"),
							result.getLong("category_id"));
					
					incomes.add(income);
			}
			
			return incomes;
		} catch (SQLException exception) {
			logger.error("Falha ao buscar receitas no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar receitas.", exception);
		}
	}
	
	public List<Income> findAllByUserId(Long userId) {
		var query = "SELECT * FROM incomes WHERE user_id = ?";
		
		try (var findAll = connection.prepareStatement(query)) {
			findAll.setLong(1, userId);
			var result = findAll.executeQuery();
			
			var incomes = new LinkedList<Income>();
			while (result.next()) {
					var income = new Income(
							result.getLong("id"),
							result.getLong("user_id"),
							result.getBigDecimal("value"),
							result.getLong("date"),
							result.getString("description"),
							result.getLong("category_id"));
					
					incomes.add(income);
			}
			
			return incomes;
		} catch (SQLException exception) {
			logger.error("Falha ao buscar receitas no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar receitas.", exception);
		}
	}
	
	public List<Income> findAllByCategoryId(Long categoryId) {
		var query = "SELECT * FROM incomes WHERE category_id = ?";

		try (var findAll = connection.prepareStatement(query)) {
			findAll.setLong(1, categoryId);
			var result = findAll.executeQuery();
			
			var incomes = new LinkedList<Income>();
			while (result.next()) {
					var income = new Income(
							result.getLong("id"),
							result.getLong("user_id"),
							result.getBigDecimal("value"),
							result.getLong("date"),
							result.getString("description"),
							result.getLong("category_id"));
					
					incomes.add(income);
			}
			
			return incomes;
		} catch (SQLException exception) {
			logger.error("Falha ao buscar receitas no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar receitas.", exception);
		}
	}
	
	public Optional<Income> findById(Long id) {
		var query = "SELECT * FROM incomes WHERE id = ?";
		
		try (var findIncome = connection.prepareStatement(query)) {
			findIncome.setLong(1, id);
			
			var result = findIncome.executeQuery();
			
			if (result.next()) {
				var income = new Income(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getBigDecimal("value"),
						result.getLong("date"),
						result.getString("description"),
						result.getLong("category_id"));
				
				return Optional.of(income);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			logger.error("Falha ao buscar receita no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar receita.", exception);
		}
	}
	
	public Optional<Long> save(Income income) {
		var statement = """
				INSERT INTO incomes (user_id, value, date, description, category_id)
				VALUES (?, ?, ?, ?, ?)
				""";
		
		try (var insertIncome = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
			insertIncome.setLong(1, income.userId());
			insertIncome.setBigDecimal(2, income.value());
			insertIncome.setLong(3, income.dateAsLong());
			insertIncome.setString(4, income.description());
			insertIncome.setLong(5, income.categoryId());
			
			insertIncome.executeUpdate();
			var result = insertIncome.getGeneratedKeys();
			result.next();
			return Optional.ofNullable(result.getLong("GENERATED_KEY"));
		} catch (SQLException exception) {
			logger.error("Erro ao inserir receita no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao salvar receita.", exception);
		}
	}
	
	public void update(Long id, Income income) {
		var statement = """
				UPDATE incomes SET
					user_id = ?,
					value = ?,
					date = ?,
					description = ?,
					category_id = ?
				WHERE id = ?
				""";
		
		try (var updateIncome = connection.prepareStatement(statement)) {
			updateIncome.setLong(1, income.userId());
			updateIncome.setBigDecimal(2, income.value());
			updateIncome.setLong(3, income.dateAsLong());
			updateIncome.setString(4, income.description());
			updateIncome.setLong(5, income.categoryId());
			updateIncome.setLong(6, id);
			
			updateIncome.executeUpdate();
		} catch (SQLException exception) {
			logger.error("Erro ao atualizar receita no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao atualizar receita.", exception);
		}
	}
	
	public boolean delete(Long id) {
		var statement = "DELETE FROM incomes WHERE id = ?";

		try (var deleteIncome = connection.prepareStatement(statement)) {
			deleteIncome.setLong(1, id);
			var result = deleteIncome.executeUpdate();
			return result > 0;
		} catch (SQLException exception) {
			logger.error("Falha ao deletar receita no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao deletar receita.", exception);
		}
	}
}
