package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String SQL = "CREATE TABLE IF NOT EXISTS users (" + "id BIGINT AUTO_INCREMENT PRIMARY KEY, " + "name VARCHAR(50), " + "lastName VARCHAR(50), " + "age TINYINT)";

    private static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS users";
    private static final String INSERT_USER_SQL = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_USER_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";
    private static final String CLEAN_TABLE_SQL = "TRUNCATE TABLE users";

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL);
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(DROP_TABLE_SQL);
            System.out.println("Таблица User(ов) удалена!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Из таблицы был удален user под номером: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS_SQL)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
                System.out.println("Получение всех users из базы: " + user.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(CLEAN_TABLE_SQL);
            System.out.println("Таблица была очищена!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
