package net.mcpes.foundhi.core;

public class ServerType {
    public final static int API = 0;
    public final static int LOGIN = 1;
    public final static int HUB = 2;
    public final static int RPG = 3;
    public final static int NORMAL = 4;

    private static int type;

    public static void init(int type) {
        ServerType.type = type;
    }

    public static int getType() {
        return type;
    }
}
