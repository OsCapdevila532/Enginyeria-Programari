package dobles;

import data.Nif;
import services.ElectoralOrganism;
import services.exceptions.ConnectException;
import services.exceptions.NotEnabledException;

public class ElectoralOrganismClass implements ElectoralOrganism{
    private Nif nif;
    private boolean canvote;
    private boolean voted;

    public ElectoralOrganismClass(Nif nif, boolean canvote, boolean voted) {    //Classe doble (dummy) per tests
        this.nif = nif;
        this.canvote = canvote;
        this.voted = voted;
    }

    public ElectoralOrganismClass(Nif nif) {    //Classe doble (dummy) per tests
        this.nif = nif;
        this.canvote = true;
        this.voted = false;
    }

    @Override
    public void canVote(Nif nif) throws NotEnabledException, ConnectException {
        if (this.nif != nif) throw new ConnectException("No s'ha pogut connectar amb el Nif introduit");
        if (!canvote) throw new NotEnabledException("El votantnt no està al col·legi electoral pertinent");
        if (voted) throw new NotEnabledException("El votant ja ha excercit el dret a vot");
    }

    @Override
    public void disableVoter(Nif nif) throws ConnectException {
        if (this.nif != nif) throw new ConnectException("No s'ha pogut connectar amb el Nif introduit");
        this.voted = true;
    }
}
