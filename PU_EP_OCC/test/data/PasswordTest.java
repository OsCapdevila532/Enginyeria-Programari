package data;

import data.exceptions.InvalidPWDFormatException;
import data.exceptions.NullPwdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {
    Password shortPassword;
    Password longPassword;

    @BeforeEach
    void setUp() throws InvalidPWDFormatException, NullPwdException {
        shortPassword = new Password("C-v67*>x");
        longPassword = new Password("Número 420 de 460 en amarillo 308 arroyo lane si.");
    }

    @Test
    void getPasswordPassTest() {
        assertEquals("C-v67*>x",shortPassword.getPassword());
        assertEquals("Número 420 de 460 en amarillo 308 arroyo lane si.",longPassword.getPassword());
    }

    @Test
    public void getPasswordFailTest(){
        assertThrows(NullPwdException.class, () -> new Password(null));
        assertThrows(InvalidPWDFormatException.class, () -> new Password("123"));
        assertThrows(InvalidPWDFormatException.class, () -> new Password("Password<"));
        assertThrows(InvalidPWDFormatException.class, () -> new Password("1aSgT2hP3qR4mN5"));
        assertThrows(InvalidPWDFormatException.class, () -> new Password(""));
    }
}