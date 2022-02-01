package adapter.repository;

import domain.User;
import domain.UserRepository;

import java.util.*;
import java.util.function.Predicate;

public class InMemoryUserRepository implements UserRepository {
    // key - id
    private Map<String, User> userRepository = new HashMap<>();

    @Override
    public User create(User user) {
        userRepository.put(user.getId(),user);
        return user;
    }

    @Override
    public Optional<User> findUserByCardNumber(String cardNumber) {
        Predicate<User> pr = (user)->user.getScores().stream().anyMatch(score -> score.getCardNumber().equals(cardNumber));
        return userRepository
                .values()
                .stream()
                .filter(pr)
                .findAny();
    }


    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userRepository.values());
    }
}
