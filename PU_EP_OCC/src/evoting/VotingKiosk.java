package evoting;

import data.*;
import data.exceptions.NullVotingOptionException;
import dobles.*;
import evoting.exceptions.InvalidDNIDocumException;
import evoting.exceptions.ProceduralException;
import services.exceptions.*;

/**
 * Internal classes involved in the exercise of the vote
 */
public class VotingKiosk {
   //  The class members
   Nif nif;
   String login;
   Password pwd;
   VotingOption vopt;
   VotingOption final_vopt;
   char doc_opt;

   ElectoralOrganismClass elect_org;
   LocalServiceClass loc_serv;
   ScrutinyClass scrutiny;

   VotingKioskState kioskState = VotingKioskState.STARTUP;
   //  The constructor/s
   public VotingKiosk()  {
   }

   // Input events
   public void initVoting () throws ProceduralException{
      if (kioskState != VotingKioskState.STARTUP) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      System.out.println("eVoting seleccionat");
      kioskState = VotingKioskState.EVOTESELECTED;
   }
   public void setDocument (char opt) throws ProceduralException{ //opt = 'd' o 'p'
      if (kioskState != VotingKioskState.EVOTESELECTED) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      if (opt == 'd') {
         System.out.println("DNI seleccionat");
         this.doc_opt = opt;
         kioskState = VotingKioskState.DOCUMSELECTED;
      }
      else if (opt == 'p') {
         System.out.println("Passaport seleccionat");
         this.doc_opt = opt;
         kioskState = VotingKioskState.DOCUMSELECTED;
      }
      else {
         throw new ProceduralException("Error en el tipus de document introduit. Ús: 'd' o 'p'");
      }
   }
   public void enterAccount (String login, Password pssw) throws InvalidAccountException, ProceduralException{
      if (kioskState != VotingKioskState.DOCUMSELECTED) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      loc_serv.verifyAccount(login, pssw);
      System.out.println("Compte d'accés válid");
      kioskState = VotingKioskState.SUPPORTVERIFIED;
   }
   public void confirmIdentif (char conf) throws InvalidDNIDocumException, ProceduralException {
      if (kioskState != VotingKioskState.SUPPORTVERIFIED) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      if (conf == 'y') {
         System.out.println("El DNI és válid");
         kioskState = VotingKioskState.IDCONFIRMED;
      }
      else if (conf == 'n') {
         System.out.println("El DNI no és válid");
         throw new InvalidDNIDocumException("El document està caducat, no és valid o no correspon al votant");
      }
      else {
         throw new ProceduralException("Error caracter de confirmacio introduit. Ús: 'y' o 'n'");
      }
   }
   public void enterNif (Nif nif) throws NotEnabledException, ConnectException, ProceduralException {
      if (kioskState != VotingKioskState.IDCONFIRMED) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      elect_org.canVote(nif);
      System.out.println("El NIF introduït és válid i pot votar");
      this.nif = nif;
      kioskState = VotingKioskState.NIFCANVOTE;
   }
   public void initOptionsNavigation() throws ProceduralException {
      if (kioskState != VotingKioskState.NIFCANVOTE) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      System.out.println("Mostrant menús i opcions de vot");
      kioskState = VotingKioskState.VOTINGOPTIONS;
   }
   public void consultVotingOption (VotingOption vopt) throws ProceduralException{
      if (kioskState != VotingKioskState.VOTINGOPTIONS) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      System.out.println("Mostrant informació detallada de la opció de vot");
      this.vopt = vopt;
      kioskState = VotingKioskState.VOPTINFO;
   }
   public void vote () throws ProceduralException, NullVotingOptionException {
      if (kioskState != VotingKioskState.VOPTINFO) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      System.out.println("Opció de vot seleccionada");
      this.final_vopt = new VotingOption(this.vopt.getParty());
      kioskState = VotingKioskState.VOTESELECTED;
   }
   public void confirmVotingOption (char conf) throws ConnectException, ProceduralException {
      if (kioskState != VotingKioskState.VOTESELECTED) {
         throw new ProceduralException("Error en la secüencia del sistema");
      }
      if (conf == 'y') {
         System.out.println("Opció de vot confirmada");
         kioskState = VotingKioskState.VOTECONFIRMED;
         scrutiny.scrutinize(this.final_vopt);     //Escrutinem el vot
         elect_org.disableVoter(this.nif);         //Ja ha votat, treiem dret a vot
         kioskState = VotingKioskState.VOTESENT;
      }
      else if (conf == 'n') {
         this.final_vopt = null;
      }
      else {
         throw new ProceduralException("Error en caracter de confirmacio. Ús: 'y' o 'n'");
      }
   }

   // Internal operation, not required
   private void finalizeSession () {
      this.nif = null;
      this.login = null;
      this.pwd = null;
      this.vopt = null;
      this.final_vopt = null;
      kioskState = VotingKioskState.SESSIONFIN;
   }

   // Setter methods for injecting dependences and additional methods
   //Injecting Setters
   public void setElectoralOrganism(ElectoralOrganismClass org) {
      this.elect_org = org;
   }
   public void setLocalService(LocalServiceClass service) {
      this.loc_serv = service;
   }
   public void setScrutiny(ScrutinyClass scrutiny) {
      this.scrutiny = scrutiny;
   }

   //Additional methods consulta valores Scrutiny
   public int getVotesFor(VotingOption vopt) {
      return scrutiny.getVotesFor(vopt);
   }
   public int getTotalVotes() {
      return scrutiny.getTotal();
   }
   public int getNullVotes() {
      return scrutiny.getNulls();
   }
   public int getBlankVotes() {
      return scrutiny.getBlanks();
   }
   public void getScrutinyResults() {
      scrutiny.getScrutinyResults();
   }
}
