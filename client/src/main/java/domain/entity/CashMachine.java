package domain.entity;

import domain.entity.cards.Card;
import domain.entity.cards.CardType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import usecase.exeptions.AlreadyHasCardInCardReader;

@Data
public class CashMachine {


    private final String id;
    private Card card;

    public CashMachine(String id){
        this.id = id;
        card = Card.builder().cardType(CardType.EMPTY).build();
    }


}
