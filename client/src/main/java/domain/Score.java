package domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Score implements Serializable {

    private long id;
    private long userId;
    private String cardNumber;
    private String scoreNumber;
    private BigDecimal amount;

    public int compareToByAmount(double amountToCompare) {
        BigDecimal toCompare = new BigDecimal(amountToCompare);
        return amount.compareTo(toCompare);
    }
}
