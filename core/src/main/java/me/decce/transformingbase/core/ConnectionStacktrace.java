package me.decce.transformingbase.core;

// Used only to print the stacktrace; never thrown
public class ConnectionStacktrace extends Exception {
    public ConnectionStacktrace() {
        super("Stacktrace");
    }
}
