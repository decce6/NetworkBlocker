package me.decce.transformingbase.core;

import java.util.Set;
import java.util.regex.Pattern;

public class NetworkBlocker {
    public static final Set<String> checkedClasses = Set.of(
            "sun.net.www.http.HttpClient",
            "jdk.internal.net.http.HttpClientImpl",
            "java.net.Socket"
    );

    public static NetworkBlockerConfig config;
    private static final INetworkManager manager = new NetworkManager();
    public static NetworkBlockerAccessor accessor;

    // https://stackoverflow.com/a/36760050
    private static final Pattern IP = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])\\.){3}(?:25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[1-9][0-9]|[0-9])$");

    public static INetworkManager getManager() {
        return manager;
    }

    public static boolean isIP(String host) {
        return IP.matcher(host).matches();
    }

    // Walk the stack to find if we came from a checked class, e.g. HttpClient
    // This is used in SocketTransformer. If we are able to come here, it means the domain is permitted,
    // And we should not block the IP connection here even if allowIP=false
    public static boolean checked() {
        return StackWalker.getInstance().walk(s ->
                s.anyMatch(f ->
                        NetworkBlocker.checkedClasses.contains(f.getClassName())
                )
        );
    }

    public static boolean permitsConnection(ConnectionInfo info) {
        if (config.allowIP && isIP(info.host())) {
            return true;
        }
        var matched = matchDomain(info) || matchModId(info);

        if (config.blacklistMode) {
            return !matched;
        }
        else {
            return matched;
        }
    }

    private static boolean matchDomain(ConnectionInfo info) {
        for (String whitelistedDomain : config.whitelistedDomains) {
            if (info.host().endsWith(whitelistedDomain)) {
                return true;
            }
        }
        return false;
    }

    private static boolean matchModId(ConnectionInfo info) {
        for (String whitelistedModId : config.whitelistedMods) {
            if (info.modid().equals(whitelistedModId)) {
                return true;
            }
        }
        return false;
    }
}
