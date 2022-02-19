import config.ManualConfig;
import domain.Score;
import domain.ScoreRepository;
import domain.User;
import domain.UserRepository;
import org.junit.jupiter.api.*;
import usecase.CreateScore;
import usecase.CreateUser;
import usecase.RemoveScore;
import usecase.RemoveUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ScoreRepositoryTest {
    private static UserRepository userRepository;
    private static ScoreRepository scoreRepository;
    private static ManualConfig manualConfig;

    @BeforeAll
    static void init(){
        manualConfig = ManualConfig.getInstance();
        scoreRepository = manualConfig.getScoreRepository();
        userRepository = manualConfig.getUserRepository();
    }
    @Test
    @DisplayName("Создаем и удаляем - user и счет")
    void createAndRemoveScore(){
        CreateUser createUser = new CreateUser(userRepository);
        RemoveUser removeUser = new RemoveUser(userRepository);

        User user = User.builder()
                .firstName("John")
                .lastName("Baker")
                .build();

        CreateScore createScore = new CreateScore(scoreRepository,userRepository);
        RemoveScore removeScore = new RemoveScore(scoreRepository);

        Score score = new Score();
        score.setScoreNumber("4000");
        score.setAmount(new BigDecimal("100.5"));
        score.setPinCode("5115");
        score.setCardNumber("999");

        Optional<User> createdUser = createUser.create(user);
        Optional<Score> createdScore = createScore.create(score,user);
        boolean isRemoveUser = removeUser.remove(user);
        boolean isRemoveScore = removeScore.remove(score.getCardNumber());

        Assertions.assertAll(
                ()->Assertions.assertTrue(createdUser.isPresent()),
                ()->Assertions.assertTrue(createdScore.isPresent()),
                ()->Assertions.assertTrue(isRemoveUser),
                ()->Assertions.assertTrue(isRemoveScore)
        );

    }

    @Test
    void getAllScoresTest(){
        // create one user and create 3 scores for him
        CreateUser createUser = new CreateUser(userRepository);
        RemoveUser removeUser = new RemoveUser(userRepository);

        User user = User.builder()
                .firstName("John")
                .lastName("Baker")
                .build();

        CreateScore createScore = new CreateScore(scoreRepository,userRepository);
        RemoveScore removeScore = new RemoveScore(scoreRepository);

        Score score1 = new Score();
        score1.setScoreNumber("001");
        score1.setAmount(new BigDecimal("10.5"));
        score1.setPinCode("1111");
        score1.setCardNumber("00009999");

        Score score2 = new Score();
        score2.setScoreNumber("002");
        score2.setAmount(new BigDecimal("100.5"));
        score2.setPinCode("2222");
        score2.setCardNumber("00008888");

        Score score3 = new Score();
        score3.setScoreNumber("003");
        score3.setAmount(new BigDecimal("1000.5"));
        score3.setPinCode("3333");
        score3.setCardNumber("00007777");

        Optional<User> createdUser = createUser.create(user);
        Optional<Score> createdScore1 = createScore.create(score1,user);
        Optional<Score> createdScore2 = createScore.create(score2,user);
        Optional<Score> createdScore3 = createScore.create(score3,user);

        List<Score> scoresFromDB = scoreRepository.getAllScores();

        boolean isRemoveUser = removeUser.remove(user);
        boolean isRemoveScore1 = removeScore.remove(score1.getCardNumber());
        boolean isRemoveScore2 = removeScore.remove(score2.getCardNumber());
        boolean isRemoveScore3 = removeScore.remove(score3.getCardNumber());


        scoresFromDB.forEach(System.out::println);

        Assertions.assertAll(
                ()->Assertions.assertTrue(createdUser.isPresent()),
                ()->Assertions.assertTrue(createdScore1.isPresent()),
                ()->Assertions.assertTrue(createdScore2.isPresent()),
                ()->Assertions.assertTrue(createdScore3.isPresent()),
                ()->Assertions.assertTrue(isRemoveUser),
                ()->Assertions.assertTrue(isRemoveScore1),
                ()->Assertions.assertTrue(isRemoveScore2),
                ()->Assertions.assertTrue(isRemoveScore3)
        );
    }
}
