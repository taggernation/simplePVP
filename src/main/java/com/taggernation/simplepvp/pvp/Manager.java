package com.taggernation.simplepvp.pvp;

import com.taggernation.simplepvp.SimplePVP;
import com.taggernation.simplepvp.utils.MainConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Manager implements Listener {

    private final SimplePVP plugin;
    private Map<Player,PVPPlayer> players = new HashMap<>();

    public Manager(SimplePVP plugin) {
        this.plugin = plugin;
        this.initialize();
    }

    public void initialize() {
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    try {
                        PVPPlayer pvpPlayer = new PVPPlayer(plugin, player);
                        if (!players.containsKey(player)) players.put(player, pvpPlayer);
                        else players.replace(player, pvpPlayer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        if (MainConfig.Options.PVP_DISABLED_WHOLE_SERVER.booleanOption) {
            event.setCancelled(true);
            return;
        }
        PVPPlayer damaged = null;
        if (event.getEntity() instanceof Player player) {
            damaged = players.get(player);
        }
        PVPPlayer damager = null;
        if (event.getDamager() instanceof Player player) {
            damager = players.get(player);
        }
        if (damaged == null || damager == null) return;
        if (damager.player.hasPermission(MainConfig.Options.PVP_BYPASS_PERMISSION.option)) {
            return;
        }
        if (!damager.canPVP(damaged.player.getWorld())) {
            event.setCancelled(true);
        }
    }
    public void check() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (MainConfig.Options.PVP_DISABLED_WHOLE_SERVER.booleanOption) {
                    plugin.getServer().getOnlinePlayers().forEach(player -> {
                        if (player.hasPermission("simplepvp.bypass")) return;
                        player.setNoDamageTicks(0);
                        player.setHealth(0);
                    });
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

}
