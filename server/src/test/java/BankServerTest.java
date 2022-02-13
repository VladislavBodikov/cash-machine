import adapter.repository.ScoreRepositoryH2DBImpl;
import adapter.repository.UserRepositoryH2DBImpl;
import domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import usecase.CreateScore;
import usecase.CreateUser;
import usecase.RemoveUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BankServerTest {
    private static UserRepository userRepository;
    private static ScoreRepository scoreRepository;
    private static BankServer bankServer;

    @BeforeAll
    public static void init() {
        userRepository = new UserRepositoryH2DBImpl();
        scoreRepository = new ScoreRepositoryH2DBImpl();
        bankServer = new BankServer(userRepository);
    }

    @Test
    void authorizeUser() {
        User user = User.builder()
                .cardNumber("22222222")
                .pinCode("1111")
                .firstName("Vlad")
                .lastName("Petrov")
                .build();

        CreateUser createUser = new CreateUser(userRepository);
        Optional<User> optUser = createUser.create(user);

        boolean isAuth = bankServer.authUser("22222222", "1111");

        RemoveUser removeUser = new RemoveUser(userRepository);
        boolean removed = removeUser.remove(user);

        Assertions.assertAll(
                () -> Assertions.assertTrue(isAuth),
                () -> Assertions.assertTrue(removed)
        );
    }

    @Test
    void createUser() {
        User user = User.builder()
                .cardNumber("22222222")
                .pinCode("1111")
                .firstName("Vlad")
                .lastName("Petrov")
                .build();

        CreateUser createUser = new CreateUser(userRepository);

        Optional<User> optUser = createUser.create(user);

        Assertions.assertTrue(optUser.isPresent());
    }

    @Test
    void createUserDuplicate() {
        User user1 = User.builder()
                .cardNumber("111")
                .pinCode("222")
                .firstName("Alex")
                .lastName("Blaskovits")
                .build();

        User user2 = User.builder()
                .cardNumber("111")
                .pinCode("222")
                .firstName("Alex")
                .lastName("Blaskovits")
                .build();

        CreateUser createUser = new CreateUser(userRepository);

        Optional<User> optUser1 = createUser.create(user1);
        Optional<User> optUser2 = createUser.create(user2);

        RemoveUser removeUser = new RemoveUser(userRepository);
        boolean remove1 = removeUser.remove(user1);
        boolean remove2 = removeUser.remove(user2);

        Assertions.assertAll(
                () -> Assertions.assertTrue(optUser1.isPresent()),
                () -> Assertions.assertFalse(optUser2.isPresent()),
                () -> Assertions.assertTrue(remove1),
                () -> Assertions.assertFalse(remove2)
        );

    }

    @Test
    void getAllUsersTest() {
        User user1 = User.builder()
                .cardNumber("222")
                .pinCode("333")
                .firstName("Alex")
                .lastName("Blaskovits")
                .build();

        User user2 = User.builder()
                .cardNumber("111")
                .pinCode("222")
                .firstName("John")
                .lastName("Blaskovits")
                .build();

        CreateUser createUser = new CreateUser(userRepository);

        Optional<User> optUser1 = createUser.create(user1);
        Optional<User> optUser2 = createUser.create(user2);


        List<User> users = userRepository.getAllUsers();
        users.forEach(System.out::println);

        RemoveUser removeUser = new RemoveUser(userRepository);
        boolean remove1 = removeUser.remove(user1);
        boolean remove2 = removeUser.remove(user2);

        Assertions.assertAll(
                () -> Assertions.assertTrue(users.size() > 0),
                () -> Assertions.assertTrue(remove1),
                () -> Assertions.assertTrue(remove2)
        );

    }

    @Test
    void getAllScoresTest(){
        scoreRepository.getAllScores().forEach(System.out::println);
    }

    @Test
    void initUserAndScoresDB(){
        CreateUser createUser = new CreateUser(userRepository);
        User user1 = User.builder()
                .firstName("VLADISLAV")
                .lastName("BODIKOV")
                .cardNumber("2202000000000011")
                .pinCode("5115")
                .build();
        User user2 = User.builder()
                .firstName("ALEXANDRA")
                .lastName("SEMENOVA")
                .cardNumber("2202000000000099")
                .pinCode("1551")
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

        boolean createUser1Score1 = createScore.create(user1score1, user1);
        boolean createUser1Score2 = createScore.create(user1score2, user1);

        boolean createUser2Score1 = createScore.create(user2score1, user2);

        Assertions.assertAll(
                ()->Assertions.assertTrue(createUser1Score1),
                ()->Assertions.assertTrue(createUser1Score2),
                ()->Assertions.assertTrue(createUser2Score1));

    }
}
