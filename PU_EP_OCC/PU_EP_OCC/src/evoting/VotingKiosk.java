package evoting;

import data.*;
import dobles.*;
import evoting.exceptions.InvalidDNIDocumException;
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
   char doc_opt;

   ElectoralOrganismClass elect_org;
   LocalServiceClass loc_serv;
   ScrutinyClass scrutiny;

   VotingKioskState kioskState = VotingKioskState.STARTUP;
   //  The constructor/s
   public VotingKiosk()  {
   }

   // Input events
   public void initVoting () {
      kioskState = VotingKioskState.EVOTESELECTED;
   }
   public void setDocument (char opt) {
      this.doc_opt = opt;
      kioskState = VotingKioskState.DOCUMSELECTED;
   }
   public void enterAccount (String login, Password pssw) throws InvalidAccountException {
      loc_serv.verifyAccount(login, pssw);
      kioskState = VotingKioskState.SUPPORTVERIFIED;
   }
   public void confirmIdentif (char conf) throws InvalidDNIDocumException {
      if (conf == 'y') {
         kioskState = VotingKioskState.IDCONFIRMED;
      }
      if (conf == 'n') {
         //*
      }
   }
   public void enterNif (Nif nif) throws NotEnabledException, ConnectException {
      elect_org.canVote(nif);
      this.nif = nif;
      kioskState = VotingKioskState.NIFCANVOTE;
   }
   public void initOptionsNavigation () {
      kioskState = VotingKioskState.VOTINGOPTIONS;
   }
   public void consultVotingOption (VotingOption vopt) {
      this.vopt = vopt;
      kioskState = VotingKioskState.VOPTINFO;
   }
   public void vote () {
      kioskState = VotingKioskState.VOTESELECTED;
   }
   public void confirmVotingOption (char conf) throws ConnectException {
      if (conf == 'y') {
         kioskState = VotingKioskState.VOTECONFIRMED;
         scrutiny.scrutinize(this.vopt);
         kioskState = VotingKioskState.VOTESENT;
      }
      if (conf == 'n') {
         //*
      }
   }

   // Internal operation, not required
   private void finalizeSession () {
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
