package pii.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.dto.AuthDTO;
import pii.service.AuthService;
import pii.service.UserService;

@Component
public class DataLoader {
	
	@Autowired
	private Connection connection;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private UserService userService;
	
	private final Logger logger = LoggerFactory.getLogger(DataLoader.class);
	
	@PostConstruct
	private void createTables() {
		logger.info("Inicializando banco de dados.");
		createUserTable();
		createCardTable();
		createAuthTable();
		createCategoryTable();
		createIncomeTable();
		createExpenseTable();
		createDefaultAdminAccount();
	}
	
	private void createUserTable() {
		var sql = """
				CREATE TABLE IF NOT EXISTS user (
					id BIGINT NOT NULL AUTO_INCREMENT,
					name VARCHAR(255) NOT NULL,
					email VARCHAR(255) UNIQUE NOT NULL,
					avatar MEDIUMTEXT,
					PRIMARY KEY (id)
				)
				""";
				
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada tabela 'user' no banco de dados.");
		} catch (SQLException e) {
			logger.error("Erro ao criar tabela de usuário.");
		}
	}
	
	private void createCardTable() {
		var sql = """
				CREATE TABLE IF NOT EXISTS card (
					id BIGINT NOT NULL AUTO_INCREMENT,
					user_id BIGINT NOT NULL,
					number VARCHAR(20) NOT NULL UNIQUE,
					type INT NOT NULL,
					brand VARCHAR(50) NOT NULL,
					`limit` DECIMAL(65, 30) NOT NULL,
					current_value DECIMAL(65, 30),
					due_date INT NOT NULL,
					PRIMARY KEY (ID),
					FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
				)
				""";
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada tabela 'card' no banco de dados.");
		} catch (SQLException exception) {
			logger.error("Erro ao criar tabela de cartões.", exception);
		}
	}
	
	private void createAuthTable() {
		var sql = """
				CREATE TABLE IF NOT EXISTS user_credentials (
					id BIGINT NOT NULL AUTO_INCREMENT,
					user_id BIGINT NOT NULL UNIQUE,
					password VARCHAR(60) NOT NULL,
					user_role INT NOT NULL,
					PRIMARY KEY (id),
					FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
				)
				""";
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada a tabela 'user_credentials' no banco de dados.");
		} catch (SQLException exception) {
			logger.error("Erro ao criar tabela 'user_credentials' no banco de dados.");
		}
	}
	
	private void createCategoryTable() {
		var sql = """
				CREATE TABLE IF NOT EXISTS category (
					id BIGINT NOT NULL AUTO_INCREMENT,
					name VARCHAR(50),
					PRIMARY KEY (id)
				)
				""";
		
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada a tabela 'category' no banco de dados.");
		} catch (SQLException exception) {
			logger.error("Erro ao criar tabela 'category' no banco de dados.");
		}
	}
	
	private void createIncomeTable() {
		var sql = """
				CREATE TABLE IF NOT EXISTS incomes (
					id BIGINT NOT NULL AUTO_INCREMENT,
					user_id BIGINT NOT NULL,
					value DECIMAL (65, 30) NOT NULL,
					date BIGINT NOT NULL,
					description VARCHAR(200),
					category_id BIGINT NOT NULL,
					PRIMARY KEY (id),
					FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
					FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
				)
				""";
		
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada a tabela 'incomes' no banco de dados.");
		} catch (SQLException exception) {
			logger.error("Erro ao criar tabela 'incomes' no banco de dados.");
		}
	}
	
	private void createExpenseTable() {
		var sql = """
				CREATE TABLE IF NOT EXISTS expenses (
					id BIGINT NOT NULL AUTO_INCREMENT,
					user_id BIGINT NOT NULL,
					value DECIMAL (65, 30) NOT NULL,
					description VARCHAR(200),
					category_id BIGINT NOT NULL,
					payment_method INTEGER NOT NULL,
					number_of_parcels INTEGER,
					is_paid BOOLEAN NOT NULL,
					card_id BIGINT,
					due_date BIGINT,
					PRIMARY KEY (id),
					FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
					FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
				)
				""";
		
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada a tabela 'expenses' no banco de dados.");
		} catch (SQLException exception) {
			logger.error("Erro ao criar tabela 'incomes' no banco de dados.");
		}
	}
	
	private void createDefaultAdminAccount() {
		var existingAdmin = userService.findByEmail("admin@admin.org");

		if (existingAdmin.isEmpty()) {
			var authDto = new AuthDTO("ADMIN", "admin@admin.org", "admin1234", "ADMIN");
			authService.registerAdmin(authDto);
		}
	}
}
