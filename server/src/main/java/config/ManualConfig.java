package config;

import adapter.repository.ScoreRepositoryH2DBImpl;
import adapter.repository.UserRepositoryH2DBImpl;
import domain.Score;
import domain.ScoreRepository;
import domain.User;
import domain.UserRepository;
import lombok.Getter;
import usecase.CreateScore;
import usecase.CreateUser;
import usecase.FindScore;
import usecase.FindUser;

import java.math.BigDecimal;
import java.util.Optional;

public class ManualConfig {
    @Getter
    private final UserRepository userRepository = new UserRepositoryH2DBImpl();
    @Getter
    private final ScoreRepository scoreRepository = new ScoreRepositoryH2DBImpl();

    private static ManualConfig manualConfig;

    private ManualConfig(){
        initDataH2DB();
    }

    public FindUser findUser() {
        return new FindUser(userRepository);
    }

    public FindScore findScore() {
        return new FindScore(scoreRepository);
    }

    private void initDataH2DB(){
        CreateUser createUser = new CreateUser(userRepository);
        User user1 = User.builder()
                .firstName("VLADISLAV")
                .lastName("BODIKOV")
                .build();
        User user2 = User.builder()
                .firstName("ALEXANDRA")
                .lastName("SEMENOVA")
                .build();

        Optional<User> createUser1 = createUser.create(user1);
        Optional<User> createUser2 = createUser.create(user2);

        CreateScore createScore = new CreateScore(scoreRepository,userRepository);

        Score user1score1 = new Score();
        user1score1.setScoreNumber("40800000000000000011");
        user1score1.setCardNumber("2202000000000011");
        user1score1.setPinCode("5115");
        user1score1.setAmount(new BigDecimal("100.50"));

        Score user1score2 = new Score();
        user1score2.setScoreNumber("40800000000000000022");
        user1score2.setCardNumber("2202000000000022");
        user1score2.setPinCode("1221");
        user1score2.setAmount(new BigDecimal("150000.50"));

        Score user2score1 = new Score();
        user2score1.setScoreNumber("40800000000000000099");
        user2score1.setCardNumber("2202000000000099");
        user2score1.setPinCode("4554");
        user2score1.setAmount(new BigDecimal("333.50"));

        Optional<Score> createUser1Score1 = createScore.create(user1score1, user1);
        Optional<Score> createUser1Score2 = createScore.create(user1score2, user1);
        Optional<Score> createUser2Score1 = createScore.create(user2score1, user2);
    }

    synchronized public static ManualConfig getInstance(){
        if (manualConfig == null){
            return new ManualConfig();
        }
        return manualConfig;
    }
}
