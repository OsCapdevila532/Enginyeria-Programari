package dobles;


import data.Password;
import data.exceptions.InvalidNifException;
import services.LocalService;
import services.exceptions.InvalidAccountException;

public class LocalServiceClass implements LocalService{
    String login;
    Password pass;

    public LocalServiceClass(String login, Password pwd) {
        this.login  = login;
        this.pass = pwd;
    }

    @Override
    public void verifyAccount(String login, Password pwd) throws InvalidAccountException {
        if (!this.login.equals(login) || !this.pass.equals(pwd)) throw new InvalidAccountException("Compte d'acces invalid");
    }
}
