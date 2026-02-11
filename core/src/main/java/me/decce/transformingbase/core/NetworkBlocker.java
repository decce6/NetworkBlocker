package me.decce.transformingbase.core;

import java.util.Set;
import java.util.regex.Pattern;

public class NetworkBlocker {
    public static final Set<String> checkedClasses = Set.of(
            "sun.net.www.http.HttpClient",
            "jdk.internal.net.http.HttpClientImpl"
    );

    public static NetworkBlockerConfig config;
    private static final INetworkManager manager = new NetworkManager();

    // https://stackoverflow.com/a/36760050
    private static final Pattern IP = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");

    public static INetworkManager getManager() {
        return manager;
    }

    public static boolean isIP(String host) {
        return IP.matcher(host).matches();
    }

    // TODO: port
    public static boolean permitsConnection(String host, int port) {
        if (config.allowIP && isIP(host)) {
            return true;
        }
        for (String allowed : config.whitelist) {
            if (host.endsWith(allowed)) {
                return true;
            }
        }
        return false;
    }
}
