package usecase.exeptions;

public class AlreadyHasCardInCardReader extends Exception{
    public AlreadyHasCardInCardReader(String message){
        super(message);
    }
}
