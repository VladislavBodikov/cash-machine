package usecase;

import domain.Score;
import domain.ScoreRepository;
import domain.User;
import domain.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
public class CreateScore {

    private ScoreRepository scoreRepository;
    private UserRepository userRepository;

    public boolean create(Score newScore, User toUser) {
        FindUser findUser = new FindUser(userRepository);

        Optional<User> userFromDB = findUser.findByLoginAndPassword(toUser.getCardNumber(),toUser.getPinCode());

        if (userFromDB.isPresent()){

            newScore.setUserId(userFromDB.get().getId());

            Optional<Score> optScore = scoreRepository.create(newScore);
            return optScore.isPresent();
        }
        return false;
    }

}
