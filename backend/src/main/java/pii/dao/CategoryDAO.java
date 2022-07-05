package pii.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.exception.ConflictException;
import pii.exception.UncheckedSQLException;
import pii.model.Category;

@Component
public class CategoryDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);
	
	public List<Category> findAll() {
		var query = "SELECT * FROM category";
		
		var categories = new LinkedList<Category>();
		try (var findAll = connection.prepareStatement(query)) {
			var result = findAll.executeQuery();
			
			while (result.next()) {
				var category = new Category(
						result.getLong("id"),
						result.getString("name"));
				categories.add(category);
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar categorias no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar categorias.", exception);
		}
		
		return categories;
	}
	
	public Optional<Category> findById(Long id) {
		var query = "SELECT * FROM category WHERE id = ?";
		
		try (var findById = connection.prepareStatement(query)) {
			findById.setLong(1, id);
			
			var result = findById.executeQuery();
			
			if (result.next()) {
				var category = new Category(
						result.getLong("id"),
						result.getString("name"));
				
				return Optional.of(category);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			logger.error("Falha ao busca categoria no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar categoria.", exception);
		}
	}
	
	public Optional<Category> findByName(String name) {
		var query = "SELECT * FROM category WHERE name = ?";
		
		try (var findByName = connection.prepareStatement(query)) {
			findByName.setString(1, name);
			
			var result = findByName.executeQuery();
			
			if (result.next()) {
				var category = new Category(
						result.getLong("id"),
						result.getString("name"));
				
				return Optional.of(category);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			logger.error("Falha ao busca categoria no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar categoria.", exception);
		}
	}
	
	public Optional<Long> save(Category category) {
		var statement = "INSERT INTO category (name) VALUES (?)";
		
		try (var insertCategory = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
			insertCategory.setString(1, category.name());
			
			insertCategory.executeUpdate();
			
			var result = insertCategory.getGeneratedKeys();
			result.next();
			return Optional.ofNullable(result.getLong("GENERATED_KEY"));
		} catch (SQLException exception) {
			if (exception instanceof SQLIntegrityConstraintViolationException) {
				var errorCode = exception.getErrorCode();
				var message = switch (errorCode) {
					case 1062 -> "Já existe uma categoria com o mesmo nome no banco de dados.";
					default -> "Erro ao salvar categoria.";
				};
				throw new ConflictException(message);
			} else {
				logger.error("Erro ao inserir categoria no banco de dados.", exception);
				throw new UncheckedSQLException("Erro ao salvar categoria.", exception);
			}
		}
	}
	
	public void update(Long id, Category category) {
		var statement = "UPDATE category SET name = ? WHERE id = ?";
		
		try (var updateCategory = connection.prepareStatement(statement)) {
			updateCategory.setString(1, category.name());
			updateCategory.setLong(2, id);
			
			updateCategory.executeUpdate();
		} catch (SQLException exception) {
			if (exception instanceof SQLIntegrityConstraintViolationException) {
				var errorCode = exception.getErrorCode();
				var message = switch (errorCode) {
					case 1062 -> "Já existe uma categoria com o mesmo nome no banco de dados.";
					default -> "Erro ao atualizar categoria.";
				};
				throw new ConflictException(message);
			} else {
				logger.error("Erro ao atualizar categoria no banco de dados.", exception);
				throw new UncheckedSQLException("Erro ao atualizar categoria.", exception);
			}
		}
	}
	
	public boolean delete(Long id) {
		var statement = "DELETE FROM category WHERE id = ?";
		
		try (var deleteCategory = connection.prepareStatement(statement)) {
			deleteCategory.setLong(1, id);
			var result = deleteCategory.executeUpdate();
			
			return result > 0;
		} catch (SQLException exception) {
			logger.error("Erro ao deletar categoria no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao tentar deletar categoria do sistema.");
		}
	}
}
