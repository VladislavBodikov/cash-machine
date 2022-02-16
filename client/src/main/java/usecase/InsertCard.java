package usecase;

import domain.CashMachine;
import domain.cards.Card;
import domain.cards.CardType;
import usecase.exeptions.AlreadyHasCardInCardReader;

public final class InsertCard {

    private CashMachine cashMachine;

    public InsertCard(CashMachine cashMachine){
        this.cashMachine = cashMachine;
    }

    public Card insert(Card card) throws AlreadyHasCardInCardReader {
        return cashMachine.insertCard(card);
    }
}
