package domain.entity.cash;

import lombok.Data;

@Data
public class Banknote {
    private int nominal;

    public Banknote(int nominal) {
        this.nominal = nominal;
    }
}
