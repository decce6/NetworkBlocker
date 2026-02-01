package me.decce.transformingbase.core;

public interface INetworkManager {
    /**
     * @return false if the connection should be blocked, true otherwise
     * */
    boolean checkConnect(String host, int port);
}