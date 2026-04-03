package me.decce.transformingbase.core;

import me.decce.transformingbase.constants.Constants;

import java.net.ConnectException;

public class ConnectionBlockedException extends ConnectException {
    public ConnectionBlockedException() {
        super("Blocked by " + Constants.MOD_NAME);
    }
}
