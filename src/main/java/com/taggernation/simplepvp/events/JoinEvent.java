package com.taggernation.simplepvp.events;

import com.taggernation.simplepvp.utils.PvPManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private final PvPManager pvpManager;
    public JoinEvent(PvPManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!pvpManager.getServerPvPStatus()) {
            pvpManager.disablePVP(player);
            player.sendMessage("§cPvP is disabled on this server.");
        }
        if (pvpManager.getPlayerPvPStatus(player)) {
            player.sendMessage("Your PvP is currently Disabled");
            return;
        }
        player.sendMessage("Your PvP is currently Enabled");
    }
}
