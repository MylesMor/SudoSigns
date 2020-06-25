package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.StringJoiner;

public class Reload {

    /**
     * Reloads the config file (signs.yml)
     * @param p The player running the command.
     * @param args Not required
     */
    public static void reload(Player p, String[] args) {
        if (p.hasPermission(Permissions.RELOAD)) {
            Util.sudoSignsMessage(p, ChatColor.GRAY, "Reloading config...", null);

            if (SudoSigns.config.loadCustomConfig()) {
                ArrayList<String> invalidSigns = SudoSigns.config.loadSigns();
                if (invalidSigns != null) {
                    if (invalidSigns.size() == 0) {
                        Util.sudoSignsMessage(p, ChatColor.GREEN, "Config successfully reloaded! Found " + invalidSigns.size() + " invalid signs! ", null);
                    } else {
                        StringBuilder message = new StringBuilder("Config successfully reloaded!" + ChatColor.RED + " Found " + invalidSigns.size() + " invalid sign(s): ");
                        StringJoiner joiner = new StringJoiner("" + ChatColor.RED + ", ", "", ChatColor.RED + ". ");
                        for (String name : invalidSigns) {
                            joiner.add(ChatColor.GOLD + name);
                        }
                        message.append(joiner.toString());
                        message.append(ChatColor.GRAY + "Use " + ChatColor.LIGHT_PURPLE + "/ss fix <name>" + ChatColor.GRAY + " to fix or " + ChatColor.LIGHT_PURPLE + "/ss purge <name>" + ChatColor.GRAY + " to remove.");
                        Util.sudoSignsMessage(p, ChatColor.GREEN, message.toString(), null);
                    }
                    return;
                }
                Bukkit.getLogger().warning("[SUDOSIGNS] There was an error with the SudoSigns config!");
                Util.sudoSignsMessage(p, ChatColor.RED, "There was an error with the SudoSigns config! Continuing to use old config...", null);
            } else {
                Bukkit.getLogger().warning("[SUDOSIGNS] There was an error with the SudoSigns config!");
                Util.sudoSignsMessage(p, ChatColor.RED, "There was an error with the SudoSigns config! Please attempt to fix before the next server reload/restart.", null);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }

    }
}
