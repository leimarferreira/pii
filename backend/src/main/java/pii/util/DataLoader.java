package pii.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(DataLoader.class);
	
	@PostConstruct
	private void createTables() {
		logger.info("Inicializando banco de dados.");
		createUserTable();
		createCardTable();
	}
	
	private void createUserTable() {
		var sql = """
				CREATE TABLE IF NOT EXISTS user (
					id BIGINT NOT NULL AUTO_INCREMENT,
					name VARCHAR(255) NOT NULL,
					email VARCHAR(255) UNIQUE NOT NULL,
					avatar VARCHAR(2048),
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
					number BIGINT NOT NULL UNIQUE,
					type INT NOT NULL,
					brand VARCHAR(50) NOT NULL,
					`limit` DECIMAL(65, 30) NOT NULL,
					current_value DECIMAL(65, 30),
					due_date INT NOT NULL,
					PRIMARY KEY (ID)
				)
				""";
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada tabela 'card' no banco de dados.");
		} catch (SQLException exception) {
			logger.error("Erro ao criar tabela de cartões.", exception);
		}
	}
}
