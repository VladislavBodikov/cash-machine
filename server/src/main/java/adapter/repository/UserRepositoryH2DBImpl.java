package adapter.repository;

import domain.User;
import domain.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryH2DBImpl implements UserRepository {

    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL = "jdbc:h2:~/my_db";

    private final String USER = "sa";
    private final String PASS = "";

    private long nextAvailableId;

    public UserRepositoryH2DBImpl() {
        dropTableIfExist("USERS");
        createTableUsers();
        nextAvailableId = getNextId();

    }

    @Override
    public Optional<User> create(User user) {
        Optional<User> toReturn = Optional.empty();
        if (isUserExist(user.getFirstName(), user.getLastName())) {
            return toReturn;
        }

        String sql = "INSERT INTO USERS VALUES(?,?,?,?);";
        int rows = 0;
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, nextAvailableId);
                preparedStatement.setString(2, user.getFirstName());
                preparedStatement.setString(3, user.getLastName());
                preparedStatement.setString(4,"emptyPassportData");

                rows = preparedStatement.executeUpdate();

                connection.commit();

                if (rows > 0) {
                    toReturn = Optional.of(user);
                    nextAvailableId++;
                }
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public Optional<User> findUserById(long id) {
        User user = null;
        String sql = "SELECT id, first_name, last_name " +
                "FROM USERS WHERE id = '" + id + "'  LIMIT 1";
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        user = User.builder()
                                .id(resultSet.getLong("id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name"))
                                .build();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findUserByName(String firstName, String lastName) {
        User user = null;
        String sql = "SELECT id, first_name, last_name " +
                "FROM USERS WHERE first_name = '" + firstName + "' AND last_name = '" + lastName + "' LIMIT 1";
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        user = User.builder()
                                .id(resultSet.getLong("id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name"))
                                .build();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean isUserExist(String firstName,String lastName) {
        return findUserByName(firstName, lastName).isPresent();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name FROM USERS;";

        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        User user = User.builder()
                                .id(resultSet.getLong("id"))
                                .firstName(resultSet.getString("first_name"))
                                .lastName(resultSet.getString("last_name"))
                                .build();

                        users.add(user);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean removeUser(String firstName, String lastName) {
        if (!isUserExist(firstName, lastName)) {
            return false;
        }

        int rows = 0;
        String sql = "DELETE FROM USERS WHERE first_name = '" + firstName + "' AND last_name = '" + lastName + "'";

        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                rows = preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return rows > 0;
    }

    // organizational methods
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
                "( id BIGINT not NULL, " +
                "first_name VARCHAR(255)," +
                "last_name VARCHAR(255)," +
                "passport_data VARCHAR(255)," +
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

    private long getNextId() {
        String sql = "SELECT id FROM USERS ORDER BY id DESC LIMIT 1;";
        long lastActualId = 0;
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        lastActualId = resultSet.getLong("id");
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return ++lastActualId;
    }
}
