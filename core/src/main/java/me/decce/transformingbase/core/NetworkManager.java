package me.decce.transformingbase.core;

public class NetworkManager implements INetworkManager {
    @Override
    public boolean checkConnect(String host, int port) {
        if (host.endsWith("0.0.0.0")) { // Redirected by us
            return true;
        }
        var config = NetworkBlocker.config;
        if (NetworkBlocker.permitsConnection(host, port)) {
            if (config.logPermitted) {
                String message = "Permitted: " + host + ":" + port;
                if (config.logStacktrace) {
                    LibraryAccessor.info(message, new ConnectionStacktrace());
                }
                else {
                    LibraryAccessor.info(message);
                }
            }
            return true;
        }
        else {
            if (config.logBlocked) {
                String message = "Blocked: " + host + ":" + port;
                if (config.logStacktrace) {
                    LibraryAccessor.info(message, new ConnectionStacktrace());
                }
                else {
                    LibraryAccessor.info(message);
                }
            }
            return false;
        }
    }
}
