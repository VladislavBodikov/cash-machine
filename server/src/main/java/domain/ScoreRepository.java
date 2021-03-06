package domain;

import java.util.List;
import java.util.Optional;

public interface ScoreRepository {

    Optional<Score> create(Score score);

    Optional<Score> findScoreByCardNumber(String cardNumber);

    List<Score> getAllScores();

    boolean removeScore(String cardNumber);

    boolean isScoreExist(String cardNumber);
}
