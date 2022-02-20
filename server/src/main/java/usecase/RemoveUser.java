package usecase;

import domain.User;
import domain.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RemoveUser {

    private UserRepository userRepository;

    public boolean remove(User user){
        return userRepository.removeUser(user.getFirstName(), user.getLastName());
    }
}
