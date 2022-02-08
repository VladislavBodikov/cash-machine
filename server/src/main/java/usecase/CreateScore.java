package usecase;

import domain.Score;
import domain.ScoreRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class CreateScore {

    private ScoreRepository scoreRepository;

    public boolean create(Score score) {
        Optional<Score> optScore = scoreRepository.createScore(score);
        return optScore.isPresent();
    }

}
