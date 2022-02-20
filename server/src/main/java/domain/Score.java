package domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Score {
    private long id;
    private long userId;
    private String cardNumber;
    private String scoreNumber;
    private BigDecimal amount;
    private String pinCode;
}
