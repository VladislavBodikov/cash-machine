package usecase;

import domain.CashMachine;

import java.io.IOException;

public final class AuthUser {

    private CashMachine cashMachine;

    public AuthUser(CashMachine cashMachine) {
        this.cashMachine = cashMachine;
    }

    public boolean auth() {
        boolean successAuth = false;

        try {
            successAuth = cashMachine.authUser();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return successAuth;
    }
}
