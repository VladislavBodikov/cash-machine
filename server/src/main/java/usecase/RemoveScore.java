package usecase;

import domain.Score;
import domain.ScoreRepository;
import domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoveScore {

    private ScoreRepository scoreRepository;

    public boolean remove(String cardNumber){
        return scoreRepository.removeScore(cardNumber);
    }

}
