package com.taggernation.simplepvp.utils;

import com.taggernation.simplepvp.SimplePVP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PvPManager {

    private final List<Player> players = new ArrayList<>( );
    private final List<String> disabledWorlds;

    private boolean PVP;

    public PvPManager(SimplePVP plugin, List<String> disabledWorlds) {
        this.disabledWorlds = disabledWorlds;
    }
    private void addPlayers(Player player) {
        players.add(player);
    }
    private void removeWorld(String world) {
        disabledWorlds.remove(world);
    }
    private void removePlayers(Player player) {
        players.remove(player);
    }
    /**
     * Get the list of players that are currently in PvP disable mode.
     * @return List of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Set the PvP status for the server.
     * @param PVP true to enable PvP, false to disable PvP
     */
    public void setServerPvPStatus(boolean PVP) {
        this.PVP = PVP;
    }

    /**
     * Get the PvP status for the server.
     * @return true if PvP is enabled, false if PvP is disabled
     */
    public boolean getServerPvPStatus() {
        return PVP;
    }

    /**
     * Get the PvP status for a player.
     * @param player Player to check
     * @return true if PvP is enabled, false if PvP is disabled
     */
    public boolean getPlayerPvPStatus(Player player) {
        return !players.contains(player);
    }

    /**
     * Enable PvP for all players in the server.
     */
    public void disablePVPForAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (getPlayerPvPStatus(player)) {
                continue;
            }
            addPlayers(player);
        }
    }

    /**
     * Disable PvP for all players in the server.
     */
    public void enablePVPForAll() {
        players.clear();
    }

    /**
     * Enable PvP for a player.
     * @param player Player to enable PvP for
     */
    public Type enablePVP(Player player) {
        if (getPlayerPvPStatus(player)) {
            removePlayers(player);
            return Type.SUCCESS;
        }
        return Type.PLAYER_ALREADY_ENABLED;
    }

    /**
     * Disable PvP for a player.
     * @param player Player to disable PvP for
     */
    public Type disablePVP(Player player) {
        if (!getPlayerPvPStatus(player)) {
            addPlayers(player);
            return Type.SUCCESS;
        }
        return Type.PLAYER_ALREADY_DISABLED;

    }
}
