package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankServer {
    private UserRepository userRepository;

    public boolean authUser(String login, String password) {
        return userRepository.isUserExist(login,password);
    }
}
