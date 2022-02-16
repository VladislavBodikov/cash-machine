package usecase;

import domain.Score;
import domain.ScoreRepository;
import domain.User;
import domain.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class CreateScore {

    private ScoreRepository scoreRepository;
    private UserRepository userRepository;

    public Optional<Score> create(Score newScore, User toUser) {
        FindUser findUser = new FindUser(userRepository);

        Optional<User> userFromDB = findUser.findByName(toUser.getFirstName(),toUser.getLastName());

        if (userFromDB.isPresent()){
            newScore.setUserId(userFromDB.get().getId());
            return scoreRepository.create(newScore);
        }

        return Optional.empty();
    }

}
