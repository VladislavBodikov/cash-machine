package usecase;

import domain.User;
import domain.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
public class FindUser {

    private UserRepository userRepository;

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userRepository.findUser(login, password);
    }

    public List<User> findAll() {
        return userRepository.getAllUsers();
    }

}
