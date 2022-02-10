package domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class User {
    private long id;
    private String firstName;
    private String lastName;
    private Set<Score> scores;
    // login
    private String cardNumber;
    // password
    private String pinCode;
}
