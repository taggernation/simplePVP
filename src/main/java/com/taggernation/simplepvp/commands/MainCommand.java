package com.taggernation.simplepvp.commands;

import com.taggernation.simplepvp.SimplePVP;
import com.taggernation.simplepvp.utils.ConfigManager;
import com.taggernation.simplepvp.utils.PvPManager;
import com.taggernation.simplepvp.utils.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainCommand implements TabExecutor {

    public PvPManager pvpManager;
    private final ConfigManager config;
    private final SimplePVP plugin;
    public MainCommand(SimplePVP plugin, PvPManager manager, ConfigManager config) {
        this.pvpManager = manager;
        this.config = config;
        this.plugin = plugin;
    }
    private static final List<String> baseCommand = Arrays
            .asList("reload", "help", "edit", "enable", "disable");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(args.length > 0)) {
            if (!sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage(plugin.data.format("You do not have permission to use this command"));
                return true;
            }
            if (!pvpManager.getServerPvPStatus()) {
                pvpManager.setServerPvPStatus(true);
                pvpManager.enablePVPForAll();
                sender.sendMessage(plugin.data.format("Enabled PVP for all players"));
                return true;
            }
            pvpManager.setServerPvPStatus(false);
            pvpManager.disablePVPForAll();
            sender.sendMessage(plugin.data.format("Disabled PVP for all players"));
            return false;
        }
        String arg1 = args[0];
        if (arg1.equals("reload")) {
            if (sender.hasPermission("simplepvp.reload") || sender.hasPermission("simplepvp.admin")) {
                try {
                    config.reload();
                    sender.sendMessage(plugin.data.format("Config reloaded"));
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        if (arg1.equals("help")) {
            if (sender.hasPermission("simplepvp.help") || sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage("&f&m                             &d&l Simple-PVP &f&m                             ");
                sender.sendMessage("&f/pvp help &7- &fShows this help menu");
                sender.sendMessage("&f/pvp reload &7- &fReloads the config");
                sender.sendMessage("&f/pvp enable &7- &fEnables PVP for all players");
                sender.sendMessage("&f/pvp disable &7- &fDisables PVP for all players");
                sender.sendMessage("&f/pvp enable <player> &7- &fEnable PVP for given player");
                sender.sendMessage("&f/pvp disable <player> &7- &fDisable PVP for given player");
                sender.sendMessage("&f&m                             &d&l Simple-PVP &f&m                             ");
                return true;
            }
        }
        if (arg1.equals("enable")) {
            if (!sender.hasPermission("simplepvp.enable") || !sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage(plugin.data.format("You do not have permission to use this command"));
                return true;
            }
            if (args.length == 1) {
                pvpManager.enablePVPForAll();
                sender.sendMessage(plugin.data.format("Enabled pvp for all players"));
                return true;
            }
            if (!sender.hasPermission("simplepvp.enable.others")) {
                sender.sendMessage(plugin.data.format( "You do not have permission to edit other players pvp status"));
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(plugin.data.format( "Player not online"));
                return true;
            }
            Type pvp = pvpManager.enablePVP(player);
            if (pvp == Type.SUCCESS) {
                player.sendMessage(plugin.data.format("Enabled pvp for" + player.getName()));
            } else if (pvp == Type.PLAYER_ALREADY_DISABLED) {
                player.sendMessage(plugin.data.format("pvp for" + player.getName() + "is already enabled"));
            }
            return true;
        }
        if (arg1.equals("disable")) {
            if (!sender.hasPermission("simplepvp.disable") || !sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage(plugin.data.format("You do not have permission to use this command"));
                return true;
            }
            if (args.length == 1) {
                pvpManager.disablePVPForAll();
                sender.sendMessage("Disabled pvp for all players");
                return true;
            }
            if (sender.hasPermission("simplepvp.disable.others")) {
                sender.sendMessage(plugin.data.format("You do not have permission to edit other players pvp status"));
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(plugin.data.format( " Player not online"));
                return true;
            }
            Type pvp = pvpManager.disablePVP(player);
            if (pvp == Type.SUCCESS) {
                player.sendMessage(plugin.data.format( "Disabled pvp for" + player.getName()));
            } else if (pvp == Type.PLAYER_ALREADY_DISABLED) {
                player.sendMessage(plugin.data.format("pvp for" + player.getName() + "is already disabled"));
            }
            return true;

        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> players = new ArrayList<>();
        if (args.length == 1) {
            return baseCommand;
        }
        if (args.length == 2) {
            if (args[0].equals("enable")) {
                pvpManager.getPlayers().forEach(player -> {
                  if (!Bukkit.getOnlinePlayers().contains(player)) {
                      players.add(player.getName());
                  }
                });
                return players;
            }
            if (args[0].equals("disable")) {
                pvpManager.getPlayers().forEach(player -> players.add(player.getName()));
                return players;
            }
        }
        return null;
    }
}
