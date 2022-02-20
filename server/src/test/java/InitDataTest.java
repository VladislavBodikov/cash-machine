import config.ManualConfig;
import domain.Score;
import domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

public class InitDataTest {

    private static ManualConfig manualConfig;

    @BeforeAll
    static void init(){
        manualConfig = ManualConfig.getInstance();
    }

    @Test
    void getAllScores(){
        List<Score> scores = manualConfig.findScore().findAll();
        scores.forEach(System.out::println);
    }
    @Test
    void getAllUsers(){
        List<User> users = manualConfig.findUser().findAll();
        users.forEach(System.out::println);
    }
    @Test
    void findScoreByCardNumber(){
        Optional<Score> score = manualConfig.findScore().findScoreByCardNumber("2202000000000011");
        System.out.println(score);
    }

}
