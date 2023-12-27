package data;

import data.exceptions.InvalidNifException;
import data.exceptions.NullNifException;

/**
 * Essential data classes
 */

final public class Nif {

    private final String nif;
    public Nif (String nif1) throws InvalidNifException, NullNifException {
        checkNif(nif1);
        this.nif = nif1;
    }

    private void checkNif(String nif) throws InvalidNifException, NullNifException {
        //Nif es Null
        if (nif == null) throw new NullNifException("El NIF introduit es Null");

        //Longitud Nif incorrecta
        int valid_length = 9;
        if (nif.length() != valid_length) throw new InvalidNifException("Longitud del NIF es incorrecta");

        //Valid = 8 nums + 1 char || 7 nums + 2 chars
        boolean valid = true;

        //Caracters 2->8 han de ser numeros
        for (int i = 1; i < nif.length()-2; i++) {
            char charAtIndex = nif.charAt(i);
            if (!Character.isDigit(charAtIndex)) valid = false;
        }

        //Últim caracter no es una lletra
        if (!Character.isAlphabetic(nif.charAt(nif.length()-1))) valid = false;

        //Primer caracter no es numero (DNI) ni lletra (NIE)
        if (!(Character.isAlphabetic(nif.charAt(0)) || Character.isDigit(nif.charAt(0)))) valid = false;

        //Finalment comprobar si cal llençar expcecio
        if (!valid) throw new InvalidNifException("El format del NIF no és correcte");
    }

    public String getNif () { return nif; }

    @Override
    public boolean equals (Object n2) {
        if (this == n2) return true;
        if (n2 == null || getClass() != n2.getClass()) return false;
        Nif nif2 = (Nif) n2;
        return nif.equals(nif2.nif);
    }
    @Override
    public int hashCode () { return nif.hashCode(); }
    @Override
    public String toString () {
        return "Nif {" + "nif votant='" + nif + '\'' + '}';
    }
}