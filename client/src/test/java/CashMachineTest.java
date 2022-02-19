import com.fasterxml.jackson.databind.ObjectMapper;
import domain.CashMachine;
import domain.Score;
import domain.User;
import domain.cards.Card;
import domain.cards.CardType;
import org.junit.jupiter.api.*;
import usecase.AuthUser;
import usecase.ExtractCard;
import usecase.InsertCard;
import usecase.exeptions.AlreadyHasCardInCardReader;
import usecase.exeptions.NoAvailableCardsToExtract;
import utils.JSONUtils;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CashMachineTest {
    private static final String SERVER_URL = "http://localhost:8080";
    private static CashMachine cashMachine = null;
    private static InsertCard insertCard = null;
    private static ExtractCard extractCard = null;
    private static AuthUser authUser = null;
    private static Card card = null;

    @Test
    @BeforeEach
    void initOptions() {
        cashMachine = new CashMachine("1");
        cashMachine.setServerURL(SERVER_URL);

        insertCard = new InsertCard(cashMachine);

        extractCard = new ExtractCard(cashMachine);

        authUser = new AuthUser(cashMachine);

        card = Card.builder()
                .firstName("VLADISLAV")
                .lastName("BODIKOV")
                .cardNumber("2202000000000011")
                .cvv("444")
                .endMonth("5")
                .endYear("2022")
                .paymentSystem("MIR")
                .cardType(CardType.DEBIT)
                .build();
    }

    @Test
    @DisplayName("Вставка одной карты в картоприемник")
    void insertOneCardInCardReader() {
        try {
            insertCard.insert(card);
            boolean isReadUserData  ="VLADISLAV".equals(cashMachine.getUser().getFirstName());
            boolean isReadScoreData = "2202000000000011".equals(cashMachine.getScore().getCardNumber());
            extractCard.extract();
            Assertions.assertAll(
                    ()->Assertions.assertTrue(isReadUserData),
                    ()->Assertions.assertTrue(isReadScoreData)
            );
        } catch (AlreadyHasCardInCardReader | NoAvailableCardsToExtract e) {
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Вставка второй карты без извлечения предыдущей")
    void insertTwoCardInCardReader() {
        try {
            insertCard.insert(card);
        } catch (AlreadyHasCardInCardReader e) {
            e.printStackTrace();
        }
        Assertions.assertThrows(AlreadyHasCardInCardReader.class, () -> insertCard.insert(card));
    }

    @Test
    @DisplayName("Вставка - Извлечение карты")
    void extractOneCardFromCardReader() {
        try {
            insertCard.insert(card);
            extractCard.extract();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(cashMachine.getCard().getCardType(), CardType.EMPTY);
    }

    @Test
    @DisplayName("Попытка извлечения двух карт")
    void extractTwoCardFromCardReader() {
        try {
            insertCard.insert(card);
            extractCard.extract();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertThrows(NoAvailableCardsToExtract.class, () -> extractCard.extract());
    }

    @Test
    @DisplayName("Успешная авторизация")
    void authUserSuccess() throws AlreadyHasCardInCardReader, IOException, NoAvailableCardsToExtract {
        insertCard.insert(card);

        cashMachine.getUser().getScore().setPinCode("5115");

        boolean isAuth = authUser.auth();

        Assertions.assertTrue(isAuth);
    }

    @Test
    @DisplayName("Неверный пин код")
    void authUserFailure() throws AlreadyHasCardInCardReader, IOException, NoAvailableCardsToExtract {
        insertCard.insert(card);

        cashMachine.getUser().getScore().setPinCode("0000");

        boolean isAuth = authUser.auth();

        Assertions.assertFalse(isAuth);
    }

    @Test
    @DisplayName("Получить баланс пользователя VLADISLAV - карта 1")
    void checkBalanceVladislav1() throws AlreadyHasCardInCardReader, NoAvailableCardsToExtract {
        insertCard.insert(card);

        cashMachine.getUser().getScore().setPinCode("5115");

        authUser.auth();

        String name = cashMachine.getUser().getFirstName();
        String balance = cashMachine.getUser().getScore().getAmount().toString();

        extractCard.extract();
        Assertions.assertAll(
                ()->Assertions.assertEquals(name,"VLADISLAV"),
                ()->Assertions.assertEquals(balance,"100.5"));
    }

    @Test
    @DisplayName("Получить баланс пользователя VLADISLAV - карта 2")
    void checkBalanceVladislav2() throws AlreadyHasCardInCardReader, NoAvailableCardsToExtract {
        Card card = Card.builder()
                .cardType(CardType.DEBIT)
                .cardNumber("2202000000000022")
                .build();
        insertCard.insert(card);

        cashMachine.getUser().getScore().setPinCode("1221");

        authUser.auth();

        String name = cashMachine.getUser().getFirstName();
        String balance = cashMachine.getUser().getScore().getAmount().toString();

        extractCard.extract();

        Assertions.assertAll(
                ()->Assertions.assertEquals(name,"VLADISLAV"),
                ()->Assertions.assertEquals(balance,"150000.5"));
    }
    @Test
    @DisplayName("Получить баланс пользователя ALEXANDRA - карта 1")
    void checkBalanceAlexandra() throws AlreadyHasCardInCardReader, NoAvailableCardsToExtract {
        Card card = Card.builder()
                .cardType(CardType.DEBIT)
                .cardNumber("2202000000000099")
                .build();
        insertCard.insert(card);

        cashMachine.getUser().getScore().setPinCode("4554");

        authUser.auth();

        String name = cashMachine.getUser().getFirstName();
        String balance = cashMachine.getUser().getScore().getAmount().toString();

        extractCard.extract();

        Assertions.assertAll(
                ()->Assertions.assertEquals(name,"ALEXANDRA"),
                ()->Assertions.assertEquals(balance,"333.5"));
    }

    @Test
    @Disabled
    void webCurlTest() throws IOException {
        URL url = new URL("http://localhost:8080/cash-machine/test");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setDoInput(true);
        http.setRequestProperty("Content-Type", "text/plain");

        //String data = "{\"cardNumber\":\"2202000000000011\",\"pinCode\":\"5115\"}";
        // send REQUEST
        User user = User.builder().build();
        ObjectMapper om = new ObjectMapper();
        String data = om.writeValueAsString(user);
        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);

        // read RESPONSE
        BufferedInputStream bsr = new BufferedInputStream(http.getInputStream());
        int c;
        StringBuilder sb = new StringBuilder();
        while ( (c = bsr.read()) != -1){
            sb.append((char) c);
        }

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        System.out.println(sb.toString());
        User userFromServer = JSONUtils.JSONtoUser(sb.toString());
        http.disconnect();

        Assertions.assertEquals(userFromServer.getScore().getAmount(),new BigDecimal("100.5"));
    }
}
