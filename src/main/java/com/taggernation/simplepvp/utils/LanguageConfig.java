package com.taggernation.simplepvp.utils;

import com.taggernation.simplepvp.SimplePVP;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import java.io.IOException;

public class LanguageConfig extends ConfigManager{

    private static SimplePVP plugin = null;
    public static LanguageConfig languageConfig;

    public LanguageConfig(SimplePVP simplePVP) throws IOException {
        super(simplePVP, "Language.yml", false, true);
        plugin = simplePVP;
        languageConfig = this;
    }
    public LanguageConfig setVersion(String version) {
        this.setConfigVersion(version);
        return this;
    }
    public void updateConfig() throws IOException {
        this.updateConfig("version");
        this.reload();
    }

    public enum Message {
        PREFIX(languageConfig.getString("message.Prefix")),
        NO_PERMISSION(languageConfig.getString("message.No_Permission")),
        SERVER_PVP_ENABLED(languageConfig.getString("message.Server_PvP_Enabled")),
        SERVER_PVP_DISABLED(languageConfig.getString("message.Server_PvP_Disabled")),
        WORLD_PVP_ENABLED(languageConfig.getString("message.World_PvP_Enabled")),
        WORLD_PVP_DISABLED(languageConfig.getString("message.World_PvP_Disabled")),
        WORLD_PVP_ENABLED_OTHER(languageConfig.getString("message.World_PvP_Enabled_Other")),
        WORLD_PVP_DISABLED_OTHER(languageConfig.getString("message.World_PvP_Disabled_Other")),
        PLAYER_PVP_ENABLED(languageConfig.getString("message.Player_PvP_Enabled")),
        PLAYER_PVP_DISABLED(languageConfig.getString("message.Player_PvP_Disabled")),
        PLAYER_PVP_ENABLED_OTHER(languageConfig.getString("message.Player_PvP_Enabled_Other")),
        PLAYER_PVP_DISABLED_OTHER(languageConfig.getString("message.Player_PvP_Disabled_Other")),

        TITLE_PVP_ON_DISABLE_TITLE(languageConfig.getString("message.Title_Message.On_Disabled.title")),
        TITLE_PVP_ON_DISABLE_SUBTITLE(languageConfig.getString("message.Title_Message.On_Disabled.subtitle"));
        private Component message;

        Message(String message) {
            this.message = plugin.mm.deserialize(message);
        }

        public Component getMessage() {
            return message;
        }
        public Component replace(String value) {
            message = message.replaceText(TextReplacementConfig.builder().match("%player%").replacement(value).build());
            return message.replaceText(TextReplacementConfig.builder().match("%world%").replacement(value).build());
        }
    }

}
