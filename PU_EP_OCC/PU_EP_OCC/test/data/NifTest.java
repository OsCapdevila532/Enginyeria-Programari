package data;

import data.exceptions.InvalidNifException;
import data.exceptions.NullNifException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NifTest {
    Nif dni;
    Nif nie;

    @BeforeEach
    public void setUp() throws InvalidNifException, NullNifException {
        dni = new Nif("00000000X");
        nie = new Nif("X0000000X");
    }

    @Test
    public void getNIFPassTest() {
        assertEquals("00000000X",dni.getNif());
        assertEquals("X0000000X",nie.getNif());
    }

    @Test
    public void getNIFFailTest(){
        assertThrows(NullNifException.class, () -> new Nif(null));
        assertThrows(InvalidNifException.class, () -> new Nif("000000000000X"));
        assertThrows(InvalidNifException.class, () -> new Nif("X"));
        assertThrows(InvalidNifException.class, () -> new Nif("1A3A5A79A"));
        assertThrows(InvalidNifException.class, () -> new Nif("XXX00X00X"));
    }
}