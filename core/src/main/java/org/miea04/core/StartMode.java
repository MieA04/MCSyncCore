package org.miea04.core;

/**
 * StartMode
 *
 * @author MieMie
 */
public enum StartMode {
    CLIENT("CLIENT"),
    SERVER("SERVER"),
    NONE("NONE");

    public final String value;

    StartMode(String value) {
        this.value = value;
    }

    public static StartMode mode(String mode){
        String upperCase = mode.toUpperCase();
        if (upperCase.equals("CLIENT")) return CLIENT;
        else if (upperCase.equals("SERVER")) return SERVER;
        else return NONE;
    }
}
