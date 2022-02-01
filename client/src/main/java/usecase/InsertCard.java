package usecase;

import domain.entity.CashMachine;
import domain.entity.cards.Card;
import domain.entity.cards.CardType;
import usecase.exeptions.AlreadyHasCardInCardReader;

public final class InsertCard {

    private CashMachine cashMachine;

    public InsertCard(CashMachine cashMachine){
        this.cashMachine = cashMachine;
    }

    public Card insert(Card card) throws AlreadyHasCardInCardReader {
        if (cashMachine.getCard().getCardType() == CardType.EMPTY) {

            cashMachine.setCard(card);
            return card;
        } else {
            throw new AlreadyHasCardInCardReader("В картоприемнике уже есть карта");
        }
    }
}
