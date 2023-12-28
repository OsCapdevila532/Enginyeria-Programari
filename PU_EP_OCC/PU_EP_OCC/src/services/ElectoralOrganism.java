package services;

import data.Nif;

import services.exceptions.ConnectException;
import services.exceptions.NotEnabledException;
/**
 * External services involved in managing the electoral roll
 */
public interface ElectoralOrganism { // External service for the Electoral Organism
    void canVote(Nif nif) throws NotEnabledException, ConnectException;
    void disableVoter(Nif nif) throws ConnectException;
}
