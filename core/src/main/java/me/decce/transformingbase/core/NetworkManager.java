package me.decce.transformingbase.core;

public class NetworkManager implements INetworkManager {
    public static final String MODID_UNKNOWN = "<unknown>";

    @Override
    public boolean checkConnect(String host, int port) {
        if (host.endsWith("0.0.0.0")) { // Redirected by us
            return true;
        }
        var config = NetworkBlocker.config;
        var info = new ConnectionInfo(host, port, NetworkBlocker.accessor.getCallerMod().orElse(MODID_UNKNOWN));
        if (NetworkBlocker.permitsConnection(info)) {
            if (config.logPermitted) {
                String message = "Permitted connection to " + host + ":" + port + ", attempted by mod " + info.modid();
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
                String message = "Blocked connection to " + host + ":" + port + ", attempted by mod " + info.modid();
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
