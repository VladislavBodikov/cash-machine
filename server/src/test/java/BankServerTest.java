import adapter.repository.UserRepositoryH2DBImpl;
import domain.BankServer;
import domain.User;
import domain.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import usecase.CreateUser;
import usecase.RemoveUser;

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
                ()->Assertions.assertTrue(isAuth),
                ()->Assertions.assertTrue(removed)
        );
    }
    @Test
    void createUser(){
        User user = User.builder()
                .cardNumber("22222222")
                .pinCode("1111")
                .firstName("Vlad")
                .lastName("Petrov")
                .build();

        CreateUser createUser = new CreateUser(userRepository);

        Optional<User> optUser =  createUser.create(user);

        Assertions.assertTrue(optUser.isPresent());
    }
    @Test
    void createUserDuplicate(){
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

        Optional<User> optUser1 =  createUser.create(user1);
        Optional<User> optUser2 =  createUser.create(user2);

        RemoveUser removeUser = new RemoveUser(userRepository);
        boolean remove1 = removeUser.remove(user1);
        boolean remove2 = removeUser.remove(user2);

        Assertions.assertAll(
                ()->Assertions.assertTrue(optUser1.isPresent()),
                ()->Assertions.assertFalse(optUser2.isPresent()),
                ()->Assertions.assertTrue(remove1),
                ()->Assertions.assertFalse(remove2)
        );

    }
    @Test
    void getAllUsersTest(){
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

        Optional<User> optUser1 =  createUser.create(user1);
        Optional<User> optUser2 =  createUser.create(user2);



        List<User> users = userRepository.getAllUsers();
        users.forEach(System.out::println);

        RemoveUser removeUser = new RemoveUser(userRepository);
        boolean remove1 = removeUser.remove(user1);
        boolean remove2 = removeUser.remove(user2);

        Assertions.assertAll(
                ()->Assertions.assertTrue(users.size() > 0),
                ()->Assertions.assertTrue(remove1),
                ()->Assertions.assertTrue(remove2)
        );

    }
}
