import adapter.repository.ScoreRepositoryH2DBImpl;
import adapter.repository.UserRepositoryH2DBImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import usecase.CreateScore;
import usecase.CreateUser;
import usecase.RemoveUser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BankServerTest {
    private static UserRepository userRepository;
    private static ScoreRepository scoreRepository;
    private static BankServer bankServer;

    @BeforeAll
    public static void init() {
        // variable init
        userRepository = new UserRepositoryH2DBImpl();
        scoreRepository = new ScoreRepositoryH2DBImpl();

    }

    @Test
    void initUserAndScoresDB(){
        CreateUser createUser = new CreateUser(userRepository);
        User user1 = User.builder()
                .firstName("VLADISLAV")
                .lastName("BODIKOV")
                .build();
        User user2 = User.builder()
                .firstName("ALEXANDRA")
                .lastName("SEMENOVA")
                .build();

        createUser.create(user1);
        createUser.create(user2);

        CreateScore createScore = new CreateScore(scoreRepository,userRepository);

        Score user1score1 = new Score();
        user1score1.setScoreNumber("40800000000000000011");
        user1score1.setCardNumber("2202000000000011");
        user1score1.setAmount(new BigDecimal("100.50"));

        Score user1score2 = new Score();
        user1score2.setScoreNumber("40800000000000000022");
        user1score2.setCardNumber("2202000000000022");
        user1score2.setAmount(new BigDecimal("150000.50"));

        Score user2score1 = new Score();
        user2score1.setScoreNumber("40800000000000000099");
        user2score1.setCardNumber("2202000000000099");
        user2score1.setAmount(new BigDecimal("333.50"));

        Optional<Score> createUser1Score1 = createScore.create(user1score1, user1);
        Optional<Score> createUser1Score2 = createScore.create(user1score2, user1);

        Optional<Score> createUser2Score1 = createScore.create(user2score1, user2);

        Assertions.assertAll(
                ()->Assertions.assertTrue(createUser1Score1.isPresent()),
                ()->Assertions.assertTrue(createUser1Score2.isPresent()),
                ()->Assertions.assertTrue(createUser2Score1.isPresent()));

    }
//    @Test
//    void readInputUserJackson() throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        User inputUser = objectMapper.readValue(new File("C:\\SberJava\\cash-machine\\server\\inputUser.json"),User.class);
//        System.out.println(inputUser.getCardNumber());
//        System.out.println(inputUser.getPinCode());
//    }
}
