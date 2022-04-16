package com.taggernation.simplepvp.utils;


import org.bukkit.ChatColor;

import java.util.List;

public class Data {

    private final ConfigManager config;
    private final PvPManager pvPManager;
    public Data(ConfigManager config, PvPManager pvpManager) {
        this.config = config;
        this.pvPManager = pvpManager;
    }

    public String getPrefix() {
        return config.getString("prefix");
    }
    public List<String> getPvPDisabledWorlds() {
        return config.getStringList("pvp-disable-worlds");
    }
    public boolean getBroadcastStatus() {
        return config.getBoolean("broadcast-status");
    }
    public String getBroadcastMessage() {
        String message = config.getString("broadcast-message");
        message = message.replace("%status%", String.valueOf(pvPManager.getServerPvPStatus()));
        return message;
    }
    public String format(String message) {
        return ChatColor.translateAlternateColorCodes('&',getPrefix() + message);
    }


}
