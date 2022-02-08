package domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> create(User user);

//    Optional<User> findUserByCardNumber(String cardNumber);

//    Optional<User> findUserById(String id);

    boolean isUserExist(String login,String password);

    List<User> getAllUsers();



}
