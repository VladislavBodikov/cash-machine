import adapter.repository.ScoreRepositoryH2DBImpl;
import adapter.repository.UserRepositoryH2DBImpl;
import domain.BankServer;
import domain.ScoreRepository;
import domain.User;
import domain.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usecase.CreateUser;
import usecase.RemoveUser;

import java.util.List;
import java.util.Optional;

public class UserRepositoryTest {

    private static UserRepository userRepository;
    private static ScoreRepository scoreRepository;

    @BeforeAll
    public static void init() {
        userRepository = new UserRepositoryH2DBImpl();
    }

    @Test
    @DisplayName("Создание и удаление пользователя")
    void createAndRemoveUser() {
        User user = User.builder()
                .firstName("Vlad")
                .lastName("Petrov")
                .build();

        CreateUser createUser = new CreateUser(userRepository);
        RemoveUser removeUser = new RemoveUser(userRepository);

        Optional<User> optUser = createUser.create(user);
        boolean isUserExist = userRepository.isUserExist(user.getFirstName(), user.getLastName());
        boolean isRemove = removeUser.remove(user);

        Assertions.assertAll(
                ()->Assertions.assertTrue(optUser.isPresent()),
                ()->Assertions.assertTrue(isRemove),
                ()->Assertions.assertTrue(isUserExist));
    }

    @Test
    @DisplayName("Нельзя создать одинаковых юзеров")
    void createUserDuplicate() {
        User user1 = User.builder()
                .firstName("Alex")
                .lastName("Blaskovits")
                .build();

        User user2 = User.builder()
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
    @DisplayName("Получить всех пользователей из базы")
    void getAllUsersTest() {
        User user1 = User.builder()
                .firstName("Alex")
                .lastName("Blaskovits")
                .build();

        User user2 = User.builder()
                .firstName("John")
                .lastName("Blaskovits")
                .build();

        CreateUser createUser = new CreateUser(userRepository);

        Optional<User> optUser1 = createUser.create(user1);
        Optional<User> optUser2 = createUser.create(user2);


        List<User> usersFromDB = userRepository.getAllUsers();
        usersFromDB.forEach(System.out::println);

        RemoveUser removeUser = new RemoveUser(userRepository);
        boolean remove1 = removeUser.remove(user1);
        boolean remove2 = removeUser.remove(user2);

        Assertions.assertAll(
                ()->Assertions.assertEquals(usersFromDB.get(0).getFirstName(),"Alex"),
                ()->Assertions.assertEquals(usersFromDB.get(1).getFirstName(),"John"),
                () -> Assertions.assertEquals(2, usersFromDB.size()),
                () -> Assertions.assertTrue(remove1),
                () -> Assertions.assertTrue(remove2)
        );

    }

}
