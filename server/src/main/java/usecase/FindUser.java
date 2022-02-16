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

    public Optional<User> findById(long id) {
        return userRepository.findUserById(id);
    }
    public Optional<User> findByName(String firstName, String lastName){
        return userRepository.findUserByName(firstName,lastName);
    }

    public List<User> findAll() {
        return userRepository.getAllUsers();
    }

}
