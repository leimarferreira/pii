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
import pii.model.Card;

@Component
public class CardDAO {
	
	@Autowired
	private Connection connection;
	
	private final Logger logger = LoggerFactory.getLogger(CardDAO.class);
	
	public List<Card> findAll() {
		var query = "SELECT * FROM card";
		
		var cards = new LinkedList<Card>();
		
		try (var findAllCards = connection.prepareStatement(query)) {
			var result = findAllCards.executeQuery();
			
			while (result.next()) {
				var card = new Card(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getLong("number"),
						result.getInt("type"),
						result.getString("brand"),
						result.getBigDecimal("limit"),
						result.getBigDecimal("current_value"),
						result.getInt("due_date"));
				
				cards.add(card);
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar cartões no banco de dados.", exception);
			throw new UncheckedSQLException("Falha ao buscar cartões.", exception);
		}
		
		return cards;
	}
	
	public List<Card> findAllByUserId(Long userId) {
		var query = "SELECT * FROM card WHERE user_id = ?";
		
		var cards = new LinkedList<Card>();
		
		try (var findAllCards = connection.prepareStatement(query)) {
			findAllCards.setLong(1, userId);
			var result = findAllCards.executeQuery();
			
			while (result.next()) {
				var card = new Card(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getLong("number"),
						result.getInt("type"),
						result.getString("brand"),
						result.getBigDecimal("limit"),
						result.getBigDecimal("current_value"),
						result.getInt("due_date"));
				
				cards.add(card);
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar cartões no banco de dados.", exception);
			throw new UncheckedSQLException("Falha ao buscar cartões do usuário.", exception);
		}
		
		return cards;
	}
	
	public Optional<Card> findById(Long cardId) {
		var query = "SELECT * FROM card WHERE id = ?";
		
		try (var findCard = connection.prepareStatement(query)) {
			findCard.setLong(1, cardId);
			
			var result = findCard.executeQuery();
			
			if (result.next()) {
				var card = new Card(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getLong("number"),
						result.getInt("type"),
						result.getString("brand"),
						result.getBigDecimal("limit"),
						result.getBigDecimal("current_value"),
						result.getInt("due_date"));
				
				return Optional.of(card);	
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar cartão no banco de dados.", exception);
			throw new UncheckedSQLException("Falha ao buscar cartão.", exception);
		}
		
		return Optional.empty();
	}
	
	public Optional<Card> findByCardNumber(Long number) {
		var query = "SELECT * FROM card WHERE number = ?";
		
		try (var findCard = connection.prepareStatement(query)) {
			findCard.setLong(1, number);
			
			var result = findCard.executeQuery();
			
			if (result.next()) {
				var card = new Card(
						result.getLong("id"),
						result.getLong("user_id"),
						result.getLong("number"),
						result.getInt("type"),
						result.getString("brand"),
						result.getBigDecimal("limit"),
						result.getBigDecimal("current_value"),
						result.getInt("due_date"));
				
				return Optional.of(card);	
			}
		} catch (SQLException exception) {
			logger.error("Falha ao buscar cartão no banco de dados.", exception);
			throw new UncheckedSQLException("Falha ao buscar cartão.", exception);
		}
		
		return Optional.empty();
	}
	
	public Optional<Long> save(Card card) {
		var statement = """
				INSERT INTO card (user_id, number, type, brand, `limit`, current_value, due_date)
					VALUES(?, ?, ?, ?, ?, ?, ?)""";
		
		try (var insertCard = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
			insertCard.setLong(1, card.userId());
			insertCard.setLong(2, card.number());
			insertCard.setInt(3, card.type().getValue());
			insertCard.setString(4, card.brand());
			insertCard.setBigDecimal(5, card.limit());
			insertCard.setBigDecimal(6, card.currentValue());
			insertCard.setInt(7, card.dueDate());
			
			insertCard.executeUpdate();
			
			var result = insertCard.getGeneratedKeys();
			result.next();
			return Optional.ofNullable(result.getLong("GENERATED_KEY"));
		} catch (SQLException exception) {
			if (exception instanceof SQLIntegrityConstraintViolationException) {
				throw new ConflictException("Número já utilizado por outro cartão no sistema.");
			} else {
				logger.error("Erro ao inserir cartão no banco de dados.", exception);
				throw new UncheckedSQLException("Erro ao salvar cartão.", exception);
			}
		}
	}
	
	public void update(Long id, Card card) {
		var statement = """
				UPDATE card SET
					user_id = ?,
					number = ?,
					type = ?,
					brand = ?,
					`limit` = ?,
					current_value = ?,
					due_date = ?
				WHERE id = ?""";
		
		try (var updateCard = connection.prepareStatement(statement)) {
			updateCard.setLong(1, card.userId());
			updateCard.setLong(2, card.number());
			updateCard.setInt(3, card.type().getValue());
			updateCard.setString(4, card.brand());
			updateCard.setBigDecimal(5, card.limit());
			updateCard.setBigDecimal(6, card.currentValue());
			updateCard.setInt(7, card.dueDate());
			updateCard.setLong(8, id);
			
			updateCard.executeUpdate();
		} catch (SQLException exception) {
			if (exception instanceof SQLIntegrityConstraintViolationException) {
				throw new ConflictException("Número já utilizado por outro cartão no sistema.");
			} else {
				logger.error("Erro ao atualizar cartão no banco de dados.", exception);
				throw new UncheckedSQLException("Erro ao atualizar informações do cartão.", exception);
			}
		}
	}
	
	public boolean delete(Long id) {
		var statement = "DELETE FROM card WHERE id = ?";
		
		try (var deleteCard = connection.prepareStatement(statement)) {
			deleteCard.setLong(1, id);
			var result = deleteCard.executeUpdate();
			
			return result > 0;
		} catch (SQLException exception) {
			logger.error("Erro ao deletar cartão no banco de dados.", exception);
			throw new UncheckedSQLException("Erro ao tentar deletar cartão do sistema.");
		}
	}
}
