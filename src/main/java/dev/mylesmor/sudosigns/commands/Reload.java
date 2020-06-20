package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Reload {

    /**
     * Reloads the config file (signs.yml)
     * @param p The player running the command.
     * @param args Not required
     */
    public static void reload(Player p, String[] args) {
        if (p.hasPermission(Permissions.RELOAD)) {
            Util.sudoSignsMessage(p, ChatColor.GRAY, "Reloading config...", null);

            if (SudoSigns.config.loadCustomConfig() && SudoSigns.config.loadSigns()) {
                Util.sudoSignsMessage(p, ChatColor.GREEN, "Config successfully reloaded!", null);
            } else {
                Bukkit.getLogger().warning("[SUDOSIGNS] There was an error with the SudoSigns config!");
                Util.sudoSignsMessage(p, ChatColor.RED, "There was an error with the SudoSigns config! Continuing to use old config...", null);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }

    }
}
