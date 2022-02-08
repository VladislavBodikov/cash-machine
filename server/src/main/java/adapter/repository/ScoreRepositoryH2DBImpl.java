package adapter.repository;

import domain.Score;
import domain.ScoreRepository;
import domain.User;

import java.sql.*;
import java.util.Optional;

public class ScoreRepositoryH2DBImpl implements ScoreRepository {

    private final String DB_URL = "jdbc:h2:~/cash_machine";

    private final String USER = "sa";
    private final String PASS = "";

    private long nextAvailableId;

    public ScoreRepositoryH2DBImpl(){
        dropTableIfExist("SCORES");
        createTableScores();
        nextAvailableId = getNextId();
    }

    @Override
    public Optional<Score> createScore(Score score) {
        return Optional.empty();
    }

    @Override
    public Optional<Score> findScoreByCardNumber(String cardNumber) {
        Score score = null;
        String sql = "SELECT id,card_number,score_number, amount" +
                "FROM SCORES WHERE card_number = '" + cardNumber + "'  LIMIT 1";
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        score = new Score();
                        score.setId(resultSet.getLong("id"));
                        score.setCardNumber(resultSet.getString("card_number"));
                        score.setScoreNumber(resultSet.getString("score_number"));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Optional.ofNullable(score);
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

    private boolean createTableScores() {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                "SCORES" +
                "( id BIGINT not NULL, " +
                "user_id BIGINT not NULL, " +
                "card_number VARCHAR(255)," +
                "score_number VARCHAR(255) not NULL," +
                "amount DOUBLE PRECISION not NULL, " +
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
        String sql = "SELECT id FROM SCORES ORDER BY id DESC LIMIT 1;";
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
