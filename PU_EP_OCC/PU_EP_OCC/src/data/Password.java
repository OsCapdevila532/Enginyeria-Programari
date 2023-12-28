package data;

import data.exceptions.InvalidPWDFormatException;
import data.exceptions.NullPwdException;

/**
 * Essential data classes
 */

final public class Password {

    private final String pwd;
    public Password (String pass) throws InvalidPWDFormatException, NullPwdException {
        checkPwd(pass);
        this.pwd = pass;
    }

    public void checkPwd(String pwd) throws InvalidPWDFormatException, NullPwdException{
        //Contrasenya es Null
        if (pwd == null) throw new NullPwdException("La contrasenya introduida es Null");

        //Longitud de la contrasenya es incorrecta
        if (pwd.length() < 8) throw new InvalidPWDFormatException("La contrasenya ha de tindre un mínim de 8 caracters");

        //Requisits de la contrasenya
        boolean valid = false;
        boolean num = false;
        boolean mayus = false;
        boolean minus = false;
        boolean special = false;

        char[] password = pwd.toCharArray();

        for (int i = 0; i <= password.length-1; i++){
            if (Character.isDigit(password[i])){
                num = true;
            } else if (Character.isUpperCase(password[i])){
                mayus = true;
            } else if (Character.isLowerCase(password[i])){
                minus = true;
            } else if (isSpecialChar(password[i])){
                special = true;
            }
            if(num && mayus && minus && special){ valid = true; }
        }
        if (!valid) throw new InvalidPWDFormatException("El format de la contrasenya es incorrecte. " +
                "La contrasenya ha de incloure al menys un numero, una majuscula, una minuscula i " +
                "un caracter especial");
    }

    public boolean isSpecialChar(char caracter)  {
        String charactersSpecial = "!¡¿?*+-=<>:#./%$&@[\\()];,";
        char[] specialChar = charactersSpecial.toCharArray();
        for (char special : specialChar) {
            if (caracter == special) {
                return true;
            }
        }
        return false;
    }

    public String getPassword () { return pwd; }
    @Override
    public boolean equals (Object pass) {
        if (this == pass) return true;
        if (pass == null || getClass() != pass.getClass()) return false;
        Password password1 = (Password) pass;
        return pwd.equals(password1.pwd);
    }
    @Override
    public int hashCode () { return pwd.hashCode(); }
    @Override
    public String toString () {
        return "Password {" + "contrasenya='" + pwd + '\'' + '}';
    }
}