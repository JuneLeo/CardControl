package com.poseidon.control.debug;

public final class DebugOption {

    private DebugOption(){

    }

    private static boolean debug = true;

    public static void enableDebug(){
        debug = true;
    }

    public static void disableDebug(){
        debug = false;
    }

    public static boolean isDebug(){
        return debug;
    }
}
