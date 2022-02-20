import config.ManualConfig;
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
import java.util.stream.Collectors;

public class UserRepositoryTest {

    private static UserRepository userRepository;
    private static ManualConfig manualConfig;

    @BeforeAll
    public static void init() {
        manualConfig = ManualConfig.getInstance();
        userRepository = manualConfig.getUserRepository();
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
                () -> Assertions.assertTrue(optUser.isPresent()),
                () -> Assertions.assertTrue(isRemove),
                () -> Assertions.assertTrue(isUserExist));
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

        List<String> namesOfUsersFromDB = usersFromDB.stream().map(User::getFirstName).collect(Collectors.toList());
        Assertions.assertAll(
                () -> Assertions.assertTrue(namesOfUsersFromDB.contains("Alex")),
                () -> Assertions.assertTrue(namesOfUsersFromDB.contains("John")),
                () -> Assertions.assertTrue(remove1),
                () -> Assertions.assertTrue(remove2)
        );

    }

}
