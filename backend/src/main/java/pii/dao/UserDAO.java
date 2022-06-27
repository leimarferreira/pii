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
import pii.model.User;

@Component
public class UserDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(UserDAO.class);
	
	public List<User> findAll() {
		var query = "SELECT * FROM user";
		var users = new LinkedList<User>();
		
		try (var findAllUsers = connection.prepareStatement(query)) {
			var result = findAllUsers.executeQuery();
			
			while (result.next()) {
				var user = new User(
						result.getLong("id"),
						result.getString("name"),
						result.getString("email"),
						result.getString("avatar"));
				
				users.add(user);
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar usuários no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar usuários.", exception);
		}
		
		return users;
	}
	
	public Optional<User> findById(Long id) {
		var query = "SELECT * FROM user WHERE id = ?";
		
		try (var findUser = connection.prepareStatement(query)) {
			findUser.setLong(1, id);
			var result = findUser.executeQuery();
			
			if (result.next()) {
				var user = new User(
						result.getLong("id"),
						result.getString("name"),
						result.getString("email"),
						result.getString("avatar"));
				
				return Optional.of(user);
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar usuário.", exception);
		}
		
		return Optional.empty();
	}
	
	public Optional<User> findByEmail(String email) {
		var query = "SELECT * FROM user WHERE email = ?";

		try (var findUser = connection.prepareStatement(query)) {
			findUser.setString(1, email);
			var result = findUser.executeQuery();

			if (result.next()) {
				var user = new User(
						result.getLong("id"),
						result.getString("name"),
						result.getString("email"),
						result.getString("avatar"));

				return Optional.of(user);
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao buscar usuário.", exception);
		}
		
		return Optional.empty();
	}
	
	public Optional<Long> save(User user) {
		var statement = "INSERT INTO user (name, email, avatar) VALUES (?, ?, ?)";
		
		try (var saveUser = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
			saveUser.setString(1, user.name());
			saveUser.setString(2, user.email());
			saveUser.setString(3, user.avatar());
			
			saveUser.executeUpdate();
			
			var result = saveUser.getGeneratedKeys();
			result.next();
			return Optional.ofNullable(result.getLong("GENERATED_KEY"));
		} catch (SQLException exception) {
			if (exception instanceof SQLIntegrityConstraintViolationException) {
				throw new ConflictException("Email já utilizado por outro usuário.");
			} else {
				logger.error("Erro ao inserir usuário no banco de dados.", exception);
				throw new UncheckedSQLException("Erro ao salvar usuário.", exception);
			}
		}
	}
	
	public void update(Long id, User user) {
		var statement = "UPDATE user SET name = ?, email = ?, avatar = ? WHERE id = ?";
		
		try (var updateUser = connection.prepareStatement(statement)) {
			updateUser.setString(1, user.name());
			updateUser.setString(2, user.email());
			updateUser.setString(3, user.avatar());
			updateUser.setLong(4, id);
			updateUser.executeUpdate();
		} catch (SQLException exception) {
			if (exception instanceof SQLIntegrityConstraintViolationException) {
				throw new ConflictException("Email já utilizado por outro usuário.");
			} else {
				logger.error("Erro ao atualizar usuário no banco de dados.", exception);
				throw new UncheckedSQLException("Erro ao atualizar usuário.", exception);
			}
		}
	}
	
	public boolean delete(Long id) {
		var statement = "DELETE FROM user WHERE id = ?";
		
		try (var deleteUser = connection.prepareStatement(statement)) {
			deleteUser.setLong(1, id);
			var result = deleteUser.executeUpdate();
			return result > 0;
		} catch (SQLException exception) {
			logger.error("Falha ao deletar usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao deletar usuário.", exception);
		}
	}
}
