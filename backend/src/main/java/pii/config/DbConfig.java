package pii.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class DbConfig {
	
	@Value("${database.dbms}")
	private String dbms;
	@Value("${database.address}")
	private String address;
	@Value("${database.port}")
	private String port;
	@Value("${database.dbname}")
	private String databaseName;
	@Value("${database.user}")
	private String user;
	@Value("${database.password}")
	private String password;
	
	private final Logger logger = LoggerFactory.getLogger(DbConfig.class);
	
	@Bean
	public Connection getDbConnection() {
		try {
			var url = String.format("jdbc:%s://%s:%s/%s", dbms, address, port, databaseName);
			var connection = DriverManager.getConnection(url, user, password);
			return connection;
		} catch (SQLException e) {
			logger.error("Erro ao estabelecer conexão com o banco de dados.");
			return null;
		}
	}
}
