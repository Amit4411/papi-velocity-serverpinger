package com.amit4411.serverpinger;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ServerPingerExpansion extends PlaceholderExpansion {
    private final Map<String, CachedServerInfo> cache = new HashMap<>();
    private static final long CACHE_TIME = 5000; // 5 seconds

    @Override
    public String getIdentifier() {
        return "serverpinger";
    }

    @Override
    public String getAuthor() {
        return "Amit4411";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (params == null || params.isEmpty()) {
            return "No server specified";
        }

        // Parse: ip:port_type
        String[] parts = params.split("_");
        if (parts.length < 2) {
            return "Invalid format. Use: %serverpinger_IP:PORT_type%";
        }

        String serverAddress = parts[0];
        String type = parts[1];

        try {
            ServerInfo info = getServerInfo(serverAddress);
            if (info == null) {
                return "Offline";
            }

            switch (type.toLowerCase()) {
                case "online":
                    return String.valueOf(info.getOnlinePlayers());
                case "max":
                    return String.valueOf(info.getMaxPlayers());
                case "motd":
                    return info.getMotd();
                case "version":
                    return info.getVersion();
                case "status":
                    return info.isOnline() ? "Online" : "Offline";
                default:
                    return "Unknown type: " + type;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private ServerInfo getServerInfo(String address) {
        // Check cache
        if (cache.containsKey(address)) {
            CachedServerInfo cached = cache.get(address);
            if (System.currentTimeMillis() - cached.timestamp < CACHE_TIME) {
                return cached.info;
            }
        }

        try {
            ServerInfo info = ServerPinger.ping(address);
            cache.put(address, new CachedServerInfo(info, System.currentTimeMillis()));
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    private static class CachedServerInfo {
        ServerInfo info;
        long timestamp;

        CachedServerInfo(ServerInfo info, long timestamp) {
            this.info = info;
            this.timestamp = timestamp;
        }
    }
}
