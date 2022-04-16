package com.taggernation.simplepvp.commands;

import com.taggernation.simplepvp.SimplePVP;
import com.taggernation.simplepvp.utils.PvPManager;
import com.taggernation.simplepvp.utils.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainCommand implements TabExecutor {

    public PvPManager pvpManager;
    public MainCommand(SimplePVP plugin, PvPManager manager) {
        this.pvpManager = manager;
    }
    private static final List<String> baseCommand = Arrays
            .asList("reload", "help", "edit", "enable", "disable");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(args.length > 0)) {
            if (sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage("You do not have permission to use this command");
                return true;
            }
            if (!pvpManager.getServerPvPStatus()) {
                pvpManager.enablePVPForAll();
                sender.sendMessage("Enabled PVP for all players");
                return true;
            }
            pvpManager.disablePVPForAll();
            sender.sendMessage("Disabled PVP for all players");
            return false;
        }
        String arg1 = args[0];
        if (arg1.equals("reload")) {
            if (sender.hasPermission("simplepvp.reload") || sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage("Reloading config...");
                return true;
            }
        }
        if (arg1.equals("help")) {
            if (sender.hasPermission("simplepvp.help") || sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage("Help");
                return true;
            }
        }
        if (arg1.equals("enable")) {
            if (!sender.hasPermission("simplepvp.enable") || !sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage("You do not have permission to use this command");
                return true;
            }
            if (args[1] == null) {
                pvpManager.enablePVPForAll();
                sender.sendMessage("Enabled pvp for all players");
                return true;
            }
            if (!sender.hasPermission("simplepvp.enable.others")) {
                sender.sendMessage("You do not have permission to edit other players pvp status");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Player not online");
                return true;
            }
            Type pvp = pvpManager.enablePVP(player);
            if (pvp == Type.SUCCESS) {
                player.sendMessage("Enabled pvp for" + player.getName());
            } else if (pvp == Type.PLAYER_ALREADY_DISABLED) {
                player.sendMessage("Player pvp is already Enabled");
            }
            return true;
        }
        if (arg1.equals("disable")) {
            if (!sender.hasPermission("simplepvp.disable") || !sender.hasPermission("simplepvp.admin")) {
                sender.sendMessage("You do not have permission to use this command");
                return true;
            }
            if (args[1] == null) {
                pvpManager.disablePVPForAll();
                sender.sendMessage("Disabled pvp for all players");
                return true;
            }
            if (sender.hasPermission("simplepvp.disable.others")) {
                sender.sendMessage("You do not have permission to edit other players pvp status");
                return true;
            }
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage("Player not online");
                return true;
            }
            Type pvp = pvpManager.disablePVP(player);
            if (pvp == Type.SUCCESS) {
                player.sendMessage("Disabled pvp for" + player.getName());
            } else if (pvp == Type.PLAYER_ALREADY_DISABLED) {
                player.sendMessage("Player pvp is already disabled");
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
                pvpManager.getPlayers().forEach(player -> {
                    players.add(player.getName());
                });
                return players;
            }
        }
        return null;
    }
}
