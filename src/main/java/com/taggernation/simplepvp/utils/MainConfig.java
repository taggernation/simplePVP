package com.taggernation.simplepvp.utils;


import com.taggernation.simplepvp.SimplePVP;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainConfig extends ConfigManager{


    private SimplePVP plugin;
    public static MainConfig mainConfig;
    public MainConfig(SimplePVP simplePVP) throws IOException {
        super(simplePVP, "config.yml",  false, true);
        this.plugin = simplePVP;
        mainConfig = this;
    }

    public MainConfig setVersion(String version) {
        this.setConfigVersion(version);
        return this;
    }
    public void updateConfig() throws IOException {
        this.updateConfig("version");
        this.reload();

    }

    public enum Options {
        PVP_DISABLED_WHOLE_SERVER(mainConfig.getBoolean("pvp-disabled-whole-server")),
        PVP_DISABLED_PLAYERS(mainConfig.getStringList("pvp-disabled-players")),
        PVP_BYPASS_PERMISSION(mainConfig.getString("pvp-bypass-permission")),
        PVP_NOTIFY_PLAYERS(mainConfig.getBoolean("pvp-notify-players")),
        PVP_DISABLED_WORLDS(mainConfig.getStringList("pvp-disabled-worlds"));
        public @NotNull String option = "";
        public @NotNull List<String> ListOption = new ArrayList<>();
        public @NotNull Boolean booleanOption = false;
        Options(@NotNull String option) {
            this.option = option;
        }
        Options(@NotNull List<String> options) {
            this.ListOption = options;
        }
        Options(@NotNull Boolean options) {
            this.booleanOption = options;
        }
    }

}
