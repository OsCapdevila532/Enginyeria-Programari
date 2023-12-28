package services;

import data.Password;

import services.exceptions.InvalidAccountException;

public interface LocalService { // Solves the login step for the support staff
    void verifyAccount (String login, Password pssw)
            throws InvalidAccountException;
}
