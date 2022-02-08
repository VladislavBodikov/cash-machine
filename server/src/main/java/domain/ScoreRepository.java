package domain;

import java.util.Optional;

public interface ScoreRepository {

    Optional<Score> findScoreByCardNumber();


}
