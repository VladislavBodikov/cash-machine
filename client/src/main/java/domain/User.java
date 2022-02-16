package domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Builder
@Jacksonized
public class User implements Serializable {
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
