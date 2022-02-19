package domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.cards.Card;
import domain.cards.CardType;
import lombok.Data;
import usecase.exeptions.AlreadyHasCardInCardReader;
import usecase.exeptions.NoAvailableCardsToExtract;
import utils.JSONUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Data
public class CashMachine {

    private final String id;

    private Card card;
    private User user;
    private Score score;
    private String serverURL;

    public CashMachine(String id) {
        this.id = id;
        card = Card.builder().cardType(CardType.EMPTY).build();
    }

    public Card insertCard(Card card) throws AlreadyHasCardInCardReader {
        if (this.card.getCardType() == CardType.EMPTY) {
            this.card = card;
            readScoreFromCard();
            readUserFromCard();
            //authUser();
            return this.card;
        } else {
            throw new AlreadyHasCardInCardReader("В картоприемнике уже есть карта");
        }
    }

    public Card extractCard() throws NoAvailableCardsToExtract {

        final Card emptyCard = Card.builder().cardType(CardType.EMPTY).build();

        if (card.getCardType() == CardType.EMPTY) {
            throw new NoAvailableCardsToExtract("Картоприемник пуст!");
        } else {
            Card outputCard = card;
            card = emptyCard;
            return outputCard;
        }
    }

    public boolean authUser() throws IOException {
        // init connection
        URL url = new URL(serverURL + "/cash-machine/auth");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setDoInput(true);
        http.setRequestProperty("Content-Type", "text/plain");

        // send request
        String data = JSONUtils.toJSON(user);
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
        http.disconnect();

        // analysis response
        if ("User not found".equals(sb.toString())){
            return false;
        }
        try {
            user = JSONUtils.JSONtoUser(sb.toString());
            return true;
        }
        catch (JsonProcessingException e){
            e.printStackTrace();
            return false;
        }
    }

    private void readScoreFromCard() {
        this.score = new Score();
        score.setCardNumber(card.getCardNumber());
    }

    private void readUserFromCard() {
        this.user = User.builder()
                .firstName(card.getFirstName())
                .lastName(card.getLastName())
                .score(score)
                .build();
    }


}
