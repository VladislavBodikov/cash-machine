package domain;

import lombok.Data;

import java.util.Set;

@Data
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private Set<Score> scores;
}
