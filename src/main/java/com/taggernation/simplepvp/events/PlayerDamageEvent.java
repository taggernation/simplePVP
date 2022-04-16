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
        if (!(event.getDamager() instanceof Player) && !(event.getEntity() instanceof Player)) return;
        Player damaged = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        if (damaged.hasPermission("simplepvp.bypass")) {
            damager.sendMessage("§cYou can't damage this player!");
            return;
        }
        if (pvpManager.getServerPvPStatus()) {
            event.setCancelled(true);
        }
        if (pvpManager.getPlayerPvPStatus(damaged)) {
            event.setCancelled(true);
        }
    }
}
