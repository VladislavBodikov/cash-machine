package usecase;

import domain.User;
import domain.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class CreateUser {

    private UserRepository userRepository;

    public Optional<User> create(User user){
        return userRepository.create(user);
    }
}
