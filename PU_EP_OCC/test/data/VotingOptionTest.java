package data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import data.exceptions.NullVotingOptionException;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VotingOptionTest {
    VotingOption pp;
    VotingOption vox;

    @BeforeEach
    void setUp() throws NullVotingOptionException {
        pp = new VotingOption("Partido de Payasos");
        vox = new VotingOption("Vox Independentista");
    }

    @Test
    void getVotingOptionPassTest() {
        assertEquals("Partido de Payasos",pp.getParty());
        assertEquals("Vox Independentista",vox.getParty());
    }

    @Test
    public void getVotingOptionFailTest(){
        assertThrows(NullVotingOptionException.class, () -> new VotingOption( null));
    }
}

