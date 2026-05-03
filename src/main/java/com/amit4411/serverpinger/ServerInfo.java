package com.amit4411.serverpinger;

public class ServerInfo {
    private int onlinePlayers;
    private int maxPlayers;
    private String motd;
    private String version;
    private boolean online;

    public ServerInfo(int onlinePlayers, int maxPlayers, String motd, String version) {
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.motd = motd;
        this.version = version;
        this.online = true;
    }

    public ServerInfo() {
        this.online = false;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public String getMotd() {
        return motd;
    }

    public String getVersion() {
        return version;
    }

    public boolean isOnline() {
        return online;
    }
}