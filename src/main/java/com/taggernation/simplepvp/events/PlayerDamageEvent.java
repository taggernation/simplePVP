package com.taggernation.simplepvp.events;

import com.taggernation.simplepvp.utils.PvPManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageEvent implements Listener {

    private final PvPManager pvpManager;

    public PlayerDamageEvent(PvPManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        Player damaged = null;
        if (event.getEntity() instanceof Player) {
            damaged = (Player) event.getEntity();
        }
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        }
        if (damaged == null || damager == null) return;
        if (damager.hasPermission("simplepvp.bypass")) {
            return;
        }
        if (pvpManager.getServerPvPStatus()) {
            event.setCancelled(true);
            return;
        }
        if (pvpManager.getPlayerPvPStatus(damaged)) {
            event.setCancelled(true);
        }
    }
}
