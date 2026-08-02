package org.miea04.core.platForm;

/**
 * PlatForm
 *
 * @author MieMie
 */
public enum PlatForm {
    WIN("win"),
    LINUX("linux");

    public final String value;

    PlatForm(String value){
        this.value = value;
    }
}
