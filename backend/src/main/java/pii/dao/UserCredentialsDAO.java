package pii.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.exception.ConflictException;
import pii.exception.UncheckedSQLException;
import pii.model.UserCredentials;

@Component
public class UserCredentialsDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(UserCredentialsDAO.class);
	
	public Optional<UserCredentials> findById(Long id) {
		var query = """
				SELECT uc.id, uc.user_id, user.email, uc.password, uc.salt, uc.user_role
				FROM user_credentials uc
				INNER JOIN user ON user.id = uc.user_id
				WHERE uc.id = ?
				""";
		
		try (var findCredentials = connection.prepareStatement(query)) {
			findCredentials.setLong(1, id);
			
			var result = findCredentials.executeQuery();
			
			if (result.next()) {
				var userCredentials = new UserCredentials(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getString(result.getString("email")),
						result.getString("password"),
						result.getString("salt"),
						result.getInt("user_role"));
				
				return Optional.of(userCredentials);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			logger.error("Falha ao buscar credenciais de usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Falha ao buscar credenciais de usuário.", exception);
		}
	}
	
	public Optional<UserCredentials> findByUserId(Long userId) {
		var query = """
				SELECT uc.id, uc.user_id, user.email, uc.password, uc.salt, uc.user_role
				FROM user_credentials uc
				INNER JOIN user ON user.id = uc.user_id
				WHERE uc.user_id = ?
				""";
		
		try (var findCredentials = connection.prepareStatement(query)) {
			findCredentials.setLong(1, userId);
			
			var result = findCredentials.executeQuery();
			
			if (result.next()) {
				var userCredentials = new UserCredentials(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getString(result.getString("email")),
						result.getString("password"),
						result.getString("salt"),
						result.getInt("user_role"));
				
				return Optional.of(userCredentials);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			logger.error("Falha ao buscar credenciais de usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Falha ao buscar credenciais de usuário.", exception);
		}
	}
	
	public Optional<UserCredentials> findByUserEmail(String email) {
		var query = """
				SELECT uc.id, uc.user_id, user.email, uc.password, uc.salt, uc.user_role
				FROM user_credentials uc INNER JOIN user ON user.id = uc.user_id 
				WHERE user.email = ?
				""";
		
		try (var findUserCredentials = connection.prepareStatement(query)) {
			findUserCredentials.setString(1, email);
			
			var result = findUserCredentials.executeQuery();
			
			if (result.next()) {
				var userCredentials = new UserCredentials(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getString(result.getString("email")),
						result.getString("password"),
						result.getString("salt"),
						result.getInt("user_role"));
				
				return Optional.of(userCredentials);
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			logger.error("Falha ao buscar credenciais de usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Falha ao buscar credenciais de usuário.", exception);
		}
	}
	
	public Optional<Long> save(UserCredentials userCredentials) {
		var statement = """
				INSERT INTO user_credentials(user_id, password, salt, user_role)
				VALUES (?, ?, ?, ?)
				""";
		
		try (var insertUserCredentials = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
			insertUserCredentials.setLong(1, userCredentials.userId());
			insertUserCredentials.setString(2, userCredentials.password());
			insertUserCredentials.setString(3, userCredentials.salt());
			insertUserCredentials.setInt(4, userCredentials.role().getValue());
			
			insertUserCredentials.executeUpdate();
			
			var result = insertUserCredentials.getGeneratedKeys();
			
			if (result.next()) {
				return Optional.of(result.getLong("GENERATED_KEY"));
			}
			
			return Optional.empty();
		} catch (SQLException exception) {
			if (exception instanceof SQLIntegrityConstraintViolationException) {
				var errorCode = exception.getErrorCode();
				
				var message = switch(errorCode) {
					case 1452 -> String.format("Usuário com id '%d' não existe.", userCredentials.userId());
					case 1062 -> String.format("Credenciais já definidas para usuário com id '%d'.", userCredentials.userId());
					default -> String.format("Erro ao salvar as credenciais do usuário com id '%d'.", userCredentials.userId());
				};
				
				throw new ConflictException(message);
			} else {
				logger.error("Falha ao inserir as credenciais do usuário no banco de dados.");
				throw new UncheckedSQLException("Erro ao salvar as credenciais do usuário.");
			}
		}
	}
	
	public void update(Long id, UserCredentials userCredentials) {
		var statement = """
				UPDATE user_credentials SET
					password = ?,
					salt = ?,
					user_role = ?
				WHERE id = ?
				""";
		
		try (var updateUserCredentials = connection.prepareStatement(statement)) {
			updateUserCredentials.setString(1, userCredentials.password());
			updateUserCredentials.setString(2, userCredentials.salt());
			updateUserCredentials.setInt(3, userCredentials.role().getValue());
			updateUserCredentials.setLong(4, id);
			
			updateUserCredentials.executeUpdate();
		} catch (SQLException exception) {
			logger.error("Falha ao atualizar credenciais do usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao atualizar credenciais do usuário.");
		}
	}
	
	public boolean delete(Long id) {
		var statement = "DELETE FROM user_credentials WHERE id = ?";
		
		try (var deleteUserCredentials = connection.prepareStatement(statement)) {
			deleteUserCredentials.setLong(1, id);
			return deleteUserCredentials.execute();
		} catch (SQLException exception) {
			logger.error("Falha ao deletar as credenciais do usuário no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao deletar as credenciais do usuário.", exception);
		}
	}
}
