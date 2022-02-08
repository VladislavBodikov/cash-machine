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

    private int nextAvailableId;

    public UserRepositoryH2DBImpl() {
//        dropTableIfExist("USERS");
//        dropTableIfExist("CARDS");
//        dropTableIfExist("SCORES");
        createTableUsers();
        nextAvailableId = getNextId();
//        createTableCards();

    }

    @Override
    public Optional<User> create(User user) {
        Optional<User> toReturn = Optional.empty();
        if (isUserExist(user.getLogin(), user.getPassword())) {
            return toReturn;
        }

        String sql = "INSERT INTO USERS VALUES(?,?,?);";
        int rows = 0;
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, nextAvailableId);
                preparedStatement.setString(2, user.getLogin());
                preparedStatement.setString(3, user.getPassword());

                rows = preparedStatement.executeUpdate();

                connection.commit();

                if (rows > 0){
                    User userInDB = new User();
                    userInDB.setId(nextAvailableId);
                    userInDB.setLogin(user.getLogin());
                    userInDB.setPassword(user.getPassword());
                    toReturn = Optional.of(userInDB);
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

    public Optional<User> findUser(String login,String password){
        User user = null;
        String sql = "SELECT id, login, password " +
                "FROM USERS WHERE login = '" + login + "' AND password = '" + password + "' LIMIT 1";
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setLogin(resultSet.getString("login"));
                        user.setPassword(resultSet.getString("password"));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean isUserExist(String login, String password) {
        return findUser(login,password).isPresent();
    }

    @Override
    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String sql = "SELECT id,login,password FROM USERS;";

        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setLogin(resultSet.getString("login"));
                        user.setPassword(resultSet.getString("password"));

                        users.add(user);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return users;
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
                "( id INT not NULL, " +
                "login VARCHAR(255)," +
                "password VARCHAR(255)," +
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

    private boolean createTableCards() {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                "CARDS" +
                "( id INT not NULL, " +
                "user_id VARCHAR(255)," +
                "card_number VARCHAR(255)," +
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

    private int getNextId() {
        String sql = "SELECT id FROM USERS ORDER BY id DESC LIMIT 1;";
        int lastActualId = 0;
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.first()) {
                        lastActualId = resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return ++lastActualId;
    }
}
