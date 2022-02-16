package usecase;

import domain.CashMachine;
import domain.cards.Card;
import domain.cards.CardType;
import lombok.Getter;
import lombok.Setter;
import usecase.exeptions.NoAvailableCardsToExtract;

public final class ExtractCard {



    @Setter
    @Getter
    private CashMachine cashMachine;

    public ExtractCard(CashMachine cashMachine){
        this.cashMachine = cashMachine;
    }

    public Card extract() throws NoAvailableCardsToExtract {
        return cashMachine.extractCard();
    }

}
