package br.com.fjsistemas.gamerating.enuns;

public enum Platform {
    PLAYSTATION(1,"Playstation"),
    XBOX(2,"Xbox"),
    SWITCH(3,"Switch"),
    WII(4, "Wii");

    private final String desc;
    private final int i;

     Platform(int i, String desc){
        this.desc= desc;
        this.i = i;
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
