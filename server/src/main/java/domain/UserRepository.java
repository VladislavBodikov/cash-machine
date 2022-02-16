package domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> create(User user);

//    Optional<User> findUserByCardNumber(String cardNumber);

//    Optional<User> findUserById(String id);

    Optional<User> findUserById(long id);

    boolean isUserExist(String firstName,String lastName);

    List<User> getAllUsers();

    Optional<User> findUserByName(String firstName, String lastName);

    boolean removeUser(String login, String password);



}
