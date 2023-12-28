package dobles;

import data.VotingOption;
import services.Scrutiny;

import java.util.List;
import java.util.Map;

public class ScrutinyClass implements Scrutiny{
    Map<VotingOption,Integer> partiesvotes;
    int totalVotes;
    int totalNulls;
    int totalBlanks;

    public ScrutinyClass(int totalNulls, int totalBlanks)  {
        this.totalNulls = totalNulls;
        this.totalBlanks = totalBlanks;
    }

    @Override
    public void initVoteCount(List<VotingOption> validParties) {
        this.totalVotes = 0;
        this.totalNulls = 0;
        this.totalBlanks = 0;
        for (VotingOption vopt : validParties) {
            this.partiesvotes.put(vopt, 0);
        }
    }

    @Override
    public void scrutinize(VotingOption vopt) {
        int votes = partiesvotes.get(vopt);
        this.partiesvotes.put(vopt, votes + 1);
        this.totalVotes++;
    }

    @Override
    public int getVotesFor(VotingOption vopt) {
        return partiesvotes.get(vopt);
    }

    @Override
    public int getTotal() {
        return this.totalVotes;
    }

    @Override
    public int getNulls() {
        return this.totalNulls;
    }

    @Override
    public int getBlanks() {
        return this.totalBlanks;
    }

    @Override
    public void getScrutinyResults() {
        System.out.println("Resultats de la votació:");
        partiesvotes.forEach((option, votes) -> System.out.println(option + ": " + votes));
    }
}
