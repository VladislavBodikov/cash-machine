package domain.entity.cash;

import lombok.Data;

import java.util.List;
@Data
public class Cassette<T extends Banknote> {
    List<T> banknotes;
    public Cassette(List<T> banknotes){
        this.banknotes = banknotes;
    }
}
