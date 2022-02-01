package domain.entity.cards;

import domain.entity.Score;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {
    private final String cardHolderName;
    private final String number;
    private final String cvv;
    private final String endMonth;
    private final String endYear;
    private final Score score;
    private final String paymentSystem;
    private final CardType cardType;
}
