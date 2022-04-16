package com.taggernation.simplepvp.utils;

import com.taggernation.simplepvp.SimplePVP;

public class Logger {

    private final SimplePVP plugin;
    private final ConfigManager config;
    public Logger(SimplePVP plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
    }

    public void log(String message) {
        plugin.getLogger().info(message);
    }
    public void Error(String message) {
        plugin.getLogger().severe(message);
    }
    public void Debug(String message) {
        if (config.getBoolean("debug")) {
            plugin.getLogger().info(message);
        }
    }
    public void Warning(String message) {
        plugin.getLogger().warning(message);
    }

}

