package evoting;

import data.Nif;
import data.exceptions.*;
import services.exceptions.*;
import evoting.exceptions.*;

import data.*;
import dobles.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class VotingKioskTest {

    Nif nif;
    String login;
    Password pwd;
    VotingOption vopt;
    VotingOption vopt2;

    List<VotingOption> parties;

    ElectoralOrganismClass elect_org;
    LocalServiceClass loc_serv;
    ScrutinyClass scrutiny;

    VotingKiosk votingKiosk;

    @BeforeEach
    void setUp() throws NullNifException, InvalidNifException, InvalidPWDFormatException, NullPwdException, NullVotingOptionException {
        nif = new Nif("12345678F");
        login = "Antonio";
        pwd = new Password("Ontinia26!");
        vopt = new VotingOption("Partit 1");
        vopt2 = new VotingOption("Partit 2");

        //Llista amb els partits per a les actuals votacions
        parties = new ArrayList<>();
        parties.add(vopt);
        parties.add(vopt2);

        elect_org = new ElectoralOrganismClass(nif, true, false);   //Nif pot votar i encara no ha votat
        loc_serv = new LocalServiceClass(login, pwd);
        scrutiny = new ScrutinyClass();
        scrutiny.initVoteCount(parties);    //Inicialitzem el conteig a 0 i indiquem el llistat de partits disponibles

        votingKiosk = new VotingKiosk();
        votingKiosk.setElectoralOrganism(elect_org);
        votingKiosk.setLocalService(loc_serv);
        votingKiosk.setScrutiny(scrutiny);
    }

    @Test
    void SystemSequence(){
        //Intenar fer passos sense estar en la secció de eVoting (saltant pas initVoting())
        assertThrows(ProceduralException.class, () -> votingKiosk.setDocument('d'));
        assertThrows(ProceduralException.class, () -> votingKiosk.enterAccount(login, pwd));
        assertThrows(ProceduralException.class, () -> votingKiosk.confirmIdentif('y'));
        assertThrows(ProceduralException.class, () -> votingKiosk.enterNif(nif));
        assertThrows(ProceduralException.class, () -> votingKiosk.initOptionsNavigation());
        assertThrows(ProceduralException.class, () -> votingKiosk.consultVotingOption(vopt));
        assertThrows(ProceduralException.class, () -> votingKiosk.vote());
        assertThrows(ProceduralException.class, () -> votingKiosk.confirmVotingOption('y'));
    }

    @Test
    void setDocument() throws ProceduralException {
        votingKiosk.initVoting();   //Seleccionem eVoting

        assertThrows(ProceduralException.class, () -> votingKiosk.setDocument('f'));    //Tipus document no es 'd'(DNI) ni 'p'(Passaport)
        //Intenar saltar pas setDocument
        assertThrows(ProceduralException.class, () -> votingKiosk.enterAccount(login, pwd));
    }

    @Test
    void enterAccount() throws ProceduralException, InvalidPWDFormatException, NullPwdException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('d');   //Document tipus DNI

        assertThrows(InvalidAccountException.class, () -> votingKiosk.enterAccount("Manolo", pwd));         //Login incorrecte
        Password wrong_pwd = new Password("Manolo123?");                //Creem contrasenya vàlida pero no corresponent al login
        assertThrows(InvalidAccountException.class, () -> votingKiosk.enterAccount(login, wrong_pwd));            //Contrasenya incorrecta
        assertThrows(InvalidAccountException.class, () -> votingKiosk.enterAccount("Manolo", wrong_pwd));   //Login i contrasenya incorrecta
        //Intentar saltar el pas enterAccount
        assertThrows(ProceduralException.class, () -> votingKiosk.confirmIdentif('y'));
    }

    @Test
    void confirmIdentif() throws ProceduralException, InvalidAccountException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('d');
        votingKiosk.enterAccount(login, pwd);   //Autentificació del personal de suport

        assertThrows(ProceduralException.class, () -> votingKiosk.confirmIdentif('f'));         //Caracter de confimacio no es 'y'(Yes) ni 'n'(No)
        assertThrows(InvalidDNIDocumException.class, () -> votingKiosk.confirmIdentif('n'));    //Confirmacio manual insatisfactoria DNI invalid
        //Intentar saltar el pas confirmIdentif
        assertThrows(ProceduralException.class, () -> votingKiosk.enterNif(nif));
    }

    @Test
    void enterNif_cantVote() throws ProceduralException, InvalidAccountException, InvalidDNIDocumException, NullNifException, InvalidNifException {
        ElectoralOrganismClass noVote_org = new ElectoralOrganismClass(nif, false, false);  //Nif no pot votar
        votingKiosk.setElectoralOrganism(noVote_org);   //Fem servir aquest nou ElectoralOrganism que acabem de crear

        votingKiosk.initVoting();
        votingKiosk.setDocument('d');
        votingKiosk.enterAccount(login, pwd);
        votingKiosk.confirmIdentif('y');    //Personal de suport dona per vàlid el DNI

        assertThrows(NotEnabledException.class, () -> votingKiosk.enterNif(nif));   //Intentem accedir amb un NIF sense tindre dret a vot
        Nif wrong_nif = new Nif("87654321C");   //Creem NIF vàlid però no corresponent al votant actual
        assertThrows(ConnectException.class, () -> votingKiosk.enterNif(wrong_nif));   //Intentem accedir amb el NIF incorrecte
        //Intentar saltar el pas enterNif
        assertThrows(ProceduralException.class, () -> votingKiosk.initOptionsNavigation());
    }

    @Test
    void enterNif_alrVoted() throws ProceduralException, InvalidAccountException, InvalidDNIDocumException {
        ElectoralOrganismClass voted_org = new ElectoralOrganismClass(nif, true, true);     //NIF ja ha votat
        votingKiosk.setElectoralOrganism(voted_org);    //Fem servir aquest nou ElectoralOrganism que acabem de crear

        votingKiosk.initVoting();
        votingKiosk.setDocument('d');
        votingKiosk.enterAccount(login, pwd);
        votingKiosk.confirmIdentif('y');    //Personal de suport dona per vàlid el DNI

        assertThrows(NotEnabledException.class, () -> votingKiosk.enterNif(nif));   //Intentem accedir ja havent votat
        //Intentar saltar el pas enterNif
        assertThrows(ProceduralException.class, () -> votingKiosk.initOptionsNavigation());
    }

    @Test
    void initOptionsNavigation() throws ProceduralException, InvalidAccountException, InvalidDNIDocumException, NotEnabledException, ConnectException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('d');
        votingKiosk.enterAccount(login, pwd);
        votingKiosk.confirmIdentif('y');
        votingKiosk.enterNif(nif);      //Accedim amb el NIF del votant
        //Intentar saltar el pas initOptionsNavigation
        assertThrows(ProceduralException.class, () -> votingKiosk.consultVotingOption(vopt));
    }

    @Test
    void consultVotingOption() throws ProceduralException, InvalidAccountException, InvalidDNIDocumException, NotEnabledException, ConnectException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('d');
        votingKiosk.enterAccount(login, pwd);
        votingKiosk.confirmIdentif('y');
        votingKiosk.enterNif(nif);
        votingKiosk.initOptionsNavigation();    //Seleccionem visualitzar les opcions de vot
        //Intentar saltar el pas consultVotingOption
        assertThrows(ProceduralException.class, () -> votingKiosk.vote());
    }

    @Test
    void vote() throws ProceduralException, InvalidAccountException, InvalidDNIDocumException, NotEnabledException, ConnectException, NullVotingOptionException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('d');
        votingKiosk.enterAccount(login, pwd);
        votingKiosk.confirmIdentif('y');
        votingKiosk.enterNif(nif);
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(vopt);  //Seleccionem una opcio de vot per a veure'n més informació

        //Intentar saltar el pas vote
        assertThrows(ProceduralException.class, () -> votingKiosk.confirmVotingOption('y'));
    }

    @Test
    void confirmVotingOption() throws ProceduralException, InvalidAccountException, InvalidDNIDocumException, NotEnabledException, ConnectException, NullVotingOptionException {
        votingKiosk.initVoting();
        votingKiosk.setDocument('d');
        votingKiosk.enterAccount(login, pwd);
        votingKiosk.confirmIdentif('y');
        votingKiosk.enterNif(nif);
        votingKiosk.initOptionsNavigation();
        votingKiosk.consultVotingOption(vopt);
        votingKiosk.vote();     //Seleccionem la opció de vot que estem visualitzant en aquest moment com a decisio de vot

        assertThrows(ProceduralException.class, () -> votingKiosk.confirmVotingOption('f'));    //Caracter de confimacio no es 'y'(Yes) ni 'n'(No)
        //Comprobem que confirmVotingOption funciona
        votingKiosk.confirmVotingOption('y');
    }

    @Test
    void resultsTest() throws ProceduralException, InvalidAccountException, InvalidDNIDocumException, NotEnabledException, ConnectException, NullVotingOptionException {
        votingKiosk.initVoting();               //Seleccionem eVoting
        votingKiosk.setDocument('d');           //Document tipus DNI
        votingKiosk.enterAccount(login, pwd);   //Autentificació del personal de suport
        votingKiosk.confirmIdentif('y');        //Personal de suport dona per vàlid el DNI
        votingKiosk.enterNif(nif);              //Accedim amb el NIF del votant
        votingKiosk.initOptionsNavigation();    //Seleccionem visualitzar les opcions de vot
        votingKiosk.consultVotingOption(vopt);  //Seleccionem visualitzar les opcions de vot
        votingKiosk.vote(); //Seleccionem la opció de vot que estem visualitzant en aquest moment com a decisio de vot

        //Mostrem informacio previa a la votació
        System.out.println("");
        System.out.println("INFORMACIO PREVIA A VOTACIO");
        System.out.println("Vots totals: " + votingKiosk.getTotalVotes());
        System.out.println("Vots nuls: " + votingKiosk.getNullVotes());
        System.out.println("Vots en blanc: " + votingKiosk.getBlankVotes());
        System.out.println("----RESULTATS----");
        votingKiosk.getScrutinyResults();
        System.out.println("");


        //Realitzem una votació
        System.out.println("");
        System.out.println("######################");
        votingKiosk.confirmVotingOption('y');
        System.out.println("VOT A " + vopt.getParty() +" REALITZAT");
        System.out.println("######################");
        System.out.println("");

        //Mostrem informacio després de la votació
        System.out.println("");
        System.out.println("INFORMACIO DESRPÉS DE LA VOTACIÓ");
        System.out.println("Vots totals: " + votingKiosk.getTotalVotes());
        System.out.println("Vots nuls: " + votingKiosk.getNullVotes());
        System.out.println("Vots en blanc: " + votingKiosk.getBlankVotes());
        System.out.println("----RESULTATS----");
        votingKiosk.getScrutinyResults();
        System.out.println("");
    }
}
