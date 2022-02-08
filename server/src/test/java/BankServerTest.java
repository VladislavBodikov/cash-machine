import adapter.repository.UserRepositoryH2DBImpl;
import domain.BankServer;
import domain.User;
import domain.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class BankServerTest {
    private static UserRepository userRepository;
    private static BankServer bankServer;

    @BeforeAll
    public static void init(){
        userRepository = new UserRepositoryH2DBImpl();
        bankServer = new BankServer(userRepository);
//        bankServer.setUserRepository(new UserRepositoryH2DBImpl());
    }
    @Test
    void authorizeUser() {

        boolean isAuth = bankServer.authUser("login", "password");

        Assertions.assertTrue(isAuth);
    }
    @Test
    void createUser(){
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        Optional<User> optUser =  userRepository.create(user);

        Assertions.assertTrue(optUser.isPresent());
    }
    @Test
    void createUserDuplicate(){
        User user = new User();
        user.setLogin("login");
        user.setPassword("password");
        Optional<User> optUser =  userRepository.create(user);

        Assertions.assertFalse(optUser.isPresent());
    }
    @Test
    void getAllUsersTest(){
        List<User> users = userRepository.getAllUsers();
        users.forEach(System.out::println);
        Assertions.assertTrue(users.size() > 0);
    }
}
