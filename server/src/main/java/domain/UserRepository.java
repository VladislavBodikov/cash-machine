package domain;

import domain.Score;
import domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    Optional<User> findUserByCardNumber(String cardNumber);

    List<User> findAllUsers();

}
