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
		createUserTable();
	}
	
	private void createUserTable() {
		var sql = "CREATE TABLE IF NOT EXISTS user ("
				+ "id BIGINT NOT NULL AUTO_INCREMENT,"
				+ "name VARCHAR(255) NOT NULL,"
				+ "email VARCHAR(255) UNIQUE NOT NULL,"
				+ "avatar VARCHAR(2048),"
				+ "PRIMARY KEY (id))";
				
		try (var statement = connection.prepareStatement(sql)) {
			statement.execute();
			logger.info("Criada tabela 'user' no banco de dados.");
		} catch (SQLException e) {
			logger.error("Erro ao criar tabela de usuário.");
			e.printStackTrace();
		}
	}
}
