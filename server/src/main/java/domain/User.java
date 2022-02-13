package domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private long id;
    private String firstName;
    private String lastName;
//    private Set<Score> scores;
    private Score score;
    // login
    private String cardNumber;
    // password
    private String pinCode;
}
