package br.com.fjsistemas.gamerating.enuns;

public enum ContactType {
    EMAIL(1,"Email"),
    PHONE(2,"Phone"),
    SOCIAL_MEDIA(3,"Media social");

    private final int i;
    private final String desc;

    ContactType(int i, String desc) {
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
}
