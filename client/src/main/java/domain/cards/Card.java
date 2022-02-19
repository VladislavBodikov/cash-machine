package domain.cards;

import domain.Score;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {
    private final String firstName;
    private final String lastName;
    private final String cardNumber;
    private final String cvv;
    private final String endMonth;
    private final String endYear;
    private final String paymentSystem;
    private final CardType cardType;
}
