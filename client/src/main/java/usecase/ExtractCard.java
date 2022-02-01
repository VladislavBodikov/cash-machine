package usecase;

import domain.entity.CashMachine;
import domain.entity.cards.Card;
import domain.entity.cards.CardType;
import lombok.Getter;
import lombok.Setter;
import usecase.exeptions.NoAvailableCardsToExtract;

public final class ExtractCard {

    private final Card emptyCard = Card.builder().cardType(CardType.EMPTY).build();

    @Setter
    @Getter
    private CashMachine cashMachine;

    public ExtractCard(CashMachine cashMachine){
        this.cashMachine = cashMachine;
    }

    public Card extract() throws NoAvailableCardsToExtract {

        Card card = cashMachine.getCard();

        if (card.getCardType() == CardType.EMPTY) {

            throw new NoAvailableCardsToExtract("Картоприемник пуст!");
        } else {

            cashMachine.setCard(emptyCard);
            return card;
        }
    }

}
