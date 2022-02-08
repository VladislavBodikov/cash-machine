package adapter.repository;

import domain.User;
import domain.UserRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepositoryH2DBImpl implements UserRepository {

    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:~/cash_machine";

    private final String USER = "sa";
    private final String PASS = "";

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public Optional<User> findUserByCardNumber(String cardNumber) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserById(String id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    private Optional<Connection> getConnection() {
        try {
            return Optional.ofNullable(DriverManager.getConnection(DB_URL, USER, PASS));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private boolean dropTableIfExist(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
                connection.commit();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    private boolean createTableUsers() {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                "USERS" +
                "( id INT not NULL, " +
                "name VARCHAR(255)," +
                "email VARCHAR(255)," +
                "PRIMARY KEY ( id )) ;";
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
                connection.commit();
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
