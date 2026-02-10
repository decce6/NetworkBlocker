package me.decce.transformingbase.core.util;

import me.decce.transformingbase.core.ConnectionBlockedException;

public class Thrower {
    public static void throwBlockedException() {
        throw new ConnectionBlockedException();
    }
}
