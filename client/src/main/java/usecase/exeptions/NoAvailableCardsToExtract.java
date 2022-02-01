package usecase.exeptions;

public class NoAvailableCardsToExtract extends Exception{
    public NoAvailableCardsToExtract(String message){
        super(message);
    }
}
