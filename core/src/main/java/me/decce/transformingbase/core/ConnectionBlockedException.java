package me.decce.transformingbase.core;

import me.decce.transformingbase.constants.Constants;

public class ConnectionBlockedException extends RuntimeException {
    public ConnectionBlockedException() {
        super("Blocked by " + Constants.MOD_NAME);
    }
}
