package pii.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

// TODO: terminar essa classe

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pii.model.User;
import pii.repository.UserRepository;

@Component
public class UserDAO implements UserRepository {
	
	@Autowired
	private Connection connection;
	
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	public User findById(Long id) {
		var query = "SELECT * FROM user WHERE id = ?";
		
		try (var findUser = connection.prepareStatement(query)) {
			findUser.setLong(1, id);
			var result = findUser.executeQuery();
			
			var user = new User(
					result.getLong("id"),
					result.getString("name"),
					result.getString("email"),
					result.getString("avatar"));
			
			return user;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public User findByEmail(String email) {
		var query = "SELECT * FROM user WHERE email = ?";

		try (var findUser = connection.prepareStatement(query)) {
			findUser.setString(1, email);
			var result = findUser.executeQuery();

			var user = new User(
					result.getLong("id"),
					result.getString("name"),
					result.getString("email"),
					result.getString("avatar"));

			return user;
		} catch (SQLException e) {
			return null;
		}
	}
	
	private User findLatestInsertedUser() {
		var query = "SELECT * FROM user WHERE id = LAST_INSERT_ID()";
		
		try (var findLatestUser = connection.prepareStatement(query)) {
			var result = findLatestUser.executeQuery();
			var user = new User(
					result.getLong("id"),
					result.getString("name"),
					result.getString("email"),
					result.getString("avatar"));
			
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User save(User user) {
		var statement = "INSERT INTO user VALUES (?, ?, ?)";
		
		try (var saveUser = connection.prepareStatement(statement)) {
			saveUser.setString(1, user.name());
			saveUser.setString(2, user.email());
			saveUser.setString(3, user.avatar());
			
			saveUser.execute();
			return findLatestInsertedUser();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public User update(Long id, User user) {
		var statement = "UPDATE user SET name = ?, email = ?, avatar = ? WHERE id = ?";
		
		try (var updateUser = connection.prepareStatement(statement)) {
			updateUser.setString(1, user.name());
			updateUser.setString(2, user.email());
			updateUser.setString(3, user.avatar());
			updateUser.setLong(4, user.id());
			
			updateUser.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return findById(id);
	}
	
	public boolean delete(Long id) {
		var statement = "DELETE FROM user WHERE id = ?";
		
		try (var deleteUser = connection.prepareStatement(statement)) {
			deleteUser.setLong(1, id);
			return deleteUser.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
