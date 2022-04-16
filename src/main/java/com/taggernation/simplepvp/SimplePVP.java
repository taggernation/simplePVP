package com.taggernation.simplepvp;

import com.taggernation.simplepvp.commands.MainCommand;
import com.taggernation.simplepvp.events.JoinEvent;
import com.taggernation.simplepvp.events.PlayerDamageEvent;
import com.taggernation.simplepvp.utils.ConfigManager;
import com.taggernation.simplepvp.utils.PvPManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SimplePVP extends JavaPlugin {

    private ConfigManager config;
    @Override
    public void onEnable() {
        SimplePVP plugin = this;
        try {
            config = new ConfigManager(plugin, "config.yml", false, true);
        } catch (IOException e) {
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin(this);
        }
        PvPManager pvpManager = new PvPManager(plugin, config.getStringList("pvp-disable-worlds"));
        pvpManager.setServerPvPStatus(true);
        this.getServer().getPluginManager().registerEvents(new JoinEvent(pvpManager), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDamageEvent(pvpManager), this);
        this.getCommand("simplepvp").setExecutor(new MainCommand(plugin, pvpManager));
        this.getCommand("simplepvp").setTabCompleter(new MainCommand(plugin, pvpManager));
        this.getLogger().info("Enabled " + plugin.getName() + " " + plugin.getDescription().getVersion() + "by Edward#1234 Successfully");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
