package br.com.fjsistemas.gamerating.enuns;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADM(1, "Administrador"),
    EDITOR(2,"Editor");

    private final int i;
    private final String desc;

    Role(int i, String desc){
        this.i = i;
        this.desc = desc;
    }

    public int getI() {
        return i;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return desc;
    }

    @Override
    public String getAuthority() {
        return  name();
    }
}
