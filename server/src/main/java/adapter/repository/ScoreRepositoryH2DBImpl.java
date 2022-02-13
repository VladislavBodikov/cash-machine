package adapter.repository;

import domain.Score;
import domain.ScoreRepository;
import domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreRepositoryH2DBImpl implements ScoreRepository {

    private final String DB_URL = "jdbc:h2:~/cash_machine";

    private final String USER = "sa";
    private final String PASS = "";

    private long nextAvailableId;

    public ScoreRepositoryH2DBImpl() {
        dropTableIfExist("SCORES");
        createTableScores();
        nextAvailableId = getNextId();
    }

    @Override
    public Optional<Score> create(Score score) {
        Optional<Score> toReturn = Optional.empty();
        if (isScoreExist(score.getCardNumber())) {
            return toReturn;
        }

        String sql = "INSERT INTO SCORES VALUES(?,?,?,?,?);";
        int rows = 0;
        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, nextAvailableId);
                preparedStatement.setLong(2, score.getUserId());
                preparedStatement.setString(3, score.getCardNumber());
                preparedStatement.setString(4, score.getScoreNumber());
                preparedStatement.setString(5, score.getAmount().toString());

                rows = preparedStatement.executeUpdate();

                connection.commit();

                if (rows > 0) {
                    toReturn = Optional.of(score);
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
    public Optional<Score> findScoreByCardNumber(String cardNumber) {
        Score score = null;
        String sql = "SELECT id,card_number,score_number, amount " +
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

    @Override
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT id,user_id, card_number,score_number,amount FROM SCORES;";

        try (Connection connection = getConnection().orElseThrow(SQLException::new)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Score score = new Score();
                        score.setId(resultSet.getLong("id"));
                        score.setUserId(resultSet.getLong("user_id"));
                        score.setCardNumber(resultSet.getString("card_number"));
                        score.setScoreNumber(resultSet.getString("score_number"));
                        score.setAmount(resultSet.getBigDecimal("amount"));

                        scores.add(score);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return scores;
    }

    @Override
    public boolean isScoreExist(String cardNumber) {
        return findScoreByCardNumber(cardNumber).isPresent();
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
