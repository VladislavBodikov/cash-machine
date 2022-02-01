import domain.entity.CashMachine;
import domain.entity.Score;
import domain.entity.cards.Card;
import domain.entity.cards.CardType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import usecase.ExtractCard;
import usecase.InsertCard;
import usecase.exeptions.AlreadyHasCardInCardReader;
import usecase.exeptions.NoAvailableCardsToExtract;

import java.math.BigDecimal;

public class CashMachineTest {

    private static CashMachine cashMachine = null;
    private static InsertCard insertCard = null;
    private static ExtractCard extractCard = null;
    private static Score score = null;
    private static Card card = null;

    @Test
    @BeforeEach
    void initOptions() {
        cashMachine = new CashMachine("1");

        insertCard = new InsertCard(cashMachine);

        extractCard = new ExtractCard(cashMachine);

        score = new Score();
        score.setCardNumber("3333" + "3333" + "3333" + "3333");
        score.setNumberPaymentAccount("40000000056786");
        score.setAmount(new BigDecimal(2_500_000));

        card = Card.builder()
                .cardHolderName("ALEXANDR GRADSKIY")
                .number("3333" + "3333" + "3333" + "3333")
                .cvv("444")
                .endMonth("5")
                .endYear("2022")
                .score(score)
                .paymentSystem("MIR")
                .cardType(CardType.DEBIT)
                .build();
    }

    @Test
    @DisplayName("Вставка одной карты в картоприемник")
    void insertOneCardInCardReader() {
        try {
            insertCard.insert(card);
        } catch (AlreadyHasCardInCardReader e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(cashMachine.getCard().getScore().getAmount(), new BigDecimal(2_500_000));
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
}
