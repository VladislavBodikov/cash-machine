package domain;

import java.util.Optional;

public interface ScoreRepository {

    Optional<Score> createScore(Score score);

    Optional<Score> findScoreByCardNumber(String cardNumber);


}
