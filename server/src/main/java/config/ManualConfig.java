package config;

import adapter.repository.ScoreRepositoryH2DBImpl;
import adapter.repository.UserRepositoryH2DBImpl;
import domain.ScoreRepository;
import domain.UserRepository;
import usecase.FindScore;
import usecase.FindUser;

public class ManualConfig {
    private final UserRepository userRepository = new UserRepositoryH2DBImpl();
    private final ScoreRepository scoreRepository = new ScoreRepositoryH2DBImpl();

    public FindUser findUser() {
        return new FindUser(userRepository);
    }

    public FindScore findScore() {
        return new FindScore(scoreRepository);
    }

}
