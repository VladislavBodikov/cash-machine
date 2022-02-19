import adapter.repository.ScoreRepositoryH2DBImpl;
import adapter.repository.UserRepositoryH2DBImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ManualConfig;
import domain.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import usecase.CreateScore;
import usecase.CreateUser;
import usecase.RemoveUser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class BankServerTest {
    private static UserRepository userRepository;
    private static ScoreRepository scoreRepository;
    private static ManualConfig manualConfig;

    @BeforeAll
    public static void init() {
        manualConfig = ManualConfig.getInstance();
        scoreRepository = manualConfig.getScoreRepository();
        userRepository = manualConfig.getUserRepository();
    }

    @Test
    void getAllScores(){
        List<Score> scores = scoreRepository.getAllScores();
        scores.forEach(System.out::println);
    }
    @Test
    void getAllUsers(){
        List<User> users = userRepository.getAllUsers();
        users.forEach(System.out::println);
    }
}
