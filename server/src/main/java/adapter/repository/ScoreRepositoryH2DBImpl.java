package adapter.repository;

import domain.Score;
import domain.ScoreRepository;

import java.sql.*;
import java.util.Optional;

public class ScoreRepositoryH2DBImpl implements ScoreRepository {

    private final String DB_URL = "jdbc:h2:~/cash_machine";

    private final String USER = "sa";
    private final String PASS = "";

    private int nextAvailableId;

    public ScoreRepositoryH2DBImpl(){
        createTableScores();
        nextAvailableId = getNextId();
    }

    @Override
    public Optional<Score> findScoreByCardNumber() {
        return Optional.empty();
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

    private boolean createTableScores() {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                "SCORES" +
                "( id INT not NULL, " +
                "card_number VARCHAR(255)," +
                "score_number VARCHAR(255)," +
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
        String sql = "SELECT id FROM CUSTOMER ORDER BY id DESC LIMIT 1;";
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
