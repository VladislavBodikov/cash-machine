package domain.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Score implements Serializable {
    // расчетный счет
    private String numberPaymentAccount;
    // номер пластиковой карты соответствующей счету
    private String cardNumber;
    // баланс счета
    private BigDecimal amount;

    public int compareToByAmount(double amountToCompare){
        BigDecimal toCompare = new BigDecimal(amountToCompare);
        return amount.compareTo(toCompare);
    }
}
