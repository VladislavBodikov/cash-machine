import adapter.repository.InMemoryUserRepository;
import domain.BankServer;
import domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankServerTest {
    private static BankServer bankServer = null;
    private static UserRepository userRepository = null;
    @Test
    @BeforeEach
    void initBankServer(){
        userRepository = new InMemoryUserRepository();

        bankServer = new BankServer();
        bankServer.setUserRepository(userRepository);
    }
}
