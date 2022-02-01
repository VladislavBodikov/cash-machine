package domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Score {
    private String id;
    private String cardNumber;
    private String numberPaymentAccount;
    private BigDecimal amount;
}
