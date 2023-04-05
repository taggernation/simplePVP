package com.taggernation.simplepvp.pvp;

import com.taggernation.simplepvp.SimplePVP;
import com.taggernation.simplepvp.utils.ConfigManager;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PVPPlayer extends ConfigManager {

    private final SimplePVP plugin;
    public final Player player;
    public Set<World> pvpDisabledWorlds = new HashSet<>();
    public boolean pvpEnabled = true;

    public PVPPlayer(SimplePVP plugin, Player player) throws IOException {
        super(plugin, player.getUniqueId() +".yml","players", false, true);
        this.plugin = plugin;
        this.player = player;
    }

    public void setPvPEnabled(boolean pvpEnabled) {
        this.set("pvp-enabled", pvpEnabled).save();
        this.pvpEnabled = pvpEnabled;
    }

    public boolean isPvPEnabled() {
        return pvpEnabled;
    }

    public boolean addPvPDisabledWorld(World world) {
        if (pvpDisabledWorlds.add(world)) {
            this.set("pvp-disabled-worlds", pvpDisabledWorlds).save();
            return true;
        }
        return false;
    }

    public boolean canPVP(World world) {
        return pvpEnabled && !pvpDisabledWorlds.contains(world);
    }
    public boolean canPVP(String worldName) {
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) return false;
        return pvpEnabled && !pvpDisabledWorlds.contains(world);
    }


}
