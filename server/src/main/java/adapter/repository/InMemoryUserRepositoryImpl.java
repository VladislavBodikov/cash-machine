package adapter.repository;

import domain.User;
import domain.UserRepository;

import java.util.*;
import java.util.function.Predicate;

public class InMemoryUserRepositoryImpl implements UserRepository {
    // key - id
    private Map<Long, User> userRepository = new HashMap<>();

    @Override
    public Optional<User> create(User user) {
        userRepository.put(user.getId(),user);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findUserById(long id) {
        return Optional.empty();
    }

//    @Override
//    public Optional<User> findUserByCardNumber(String cardNumber) {
//        Predicate<User> pr = (user)->user.getScores().stream().anyMatch(score -> score.getCardNumber().equals(cardNumber));
//        return userRepository
//                .values()
//                .stream()
//                .filter(pr)
//                .findAny();
//    }
//
//    @Override
//    public Optional<User> findUserById(String id) {
//        return Optional.ofNullable(userRepository.get(id));
//    }

    @Override
    public boolean isUserExist(String login, String password) {
        return false;
    }


    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.values());
    }

    @Override
    public Optional<User> findUserByName(String firstName, String lastName) {
        return Optional.empty();
    }

    @Override
    public boolean removeUser(String login, String password) {
        return false;
    }
}
