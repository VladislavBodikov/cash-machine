package usecase;

import domain.Score;
import domain.ScoreRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
public class FindScore {

    private ScoreRepository scoreRepository;

    public Optional<Score> findScoreByCardNumber(String cardNumber){
        return scoreRepository.findScoreByCardNumber(cardNumber);
    }
}
