package domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Score {
    private long id;
    private String cardNumber;
    private String scoreNumber;
    private BigDecimal amount;
}
