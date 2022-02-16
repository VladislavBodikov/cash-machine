package domain;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class User {
    private long id;
    private String firstName;
    private String lastName;
//    private Set<Score> scores;
    private Score score;
    // login
   // private String cardNumber;
    // password
//    private String pinCode;
}
