package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
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
                        Util.sudoSignsMessage(p, ChatColor.GREEN, "Config successfully reloaded!" + ChatColor.RED + " Found " + invalidSigns.size() + " invalid sign(s): ", null);
                        for (String name : invalidSigns) {
                            StringBuilder clickableMessage = new StringBuilder("[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Sign: \",\"color\":\"red\"},{\"text\":\"" + name + " \",\"bold\":true,\"color\":\"gold\"}]");

                            if (p.hasPermission(Permissions.PURGE)) {
                                clickableMessage = new StringBuilder(clickableMessage.substring(0, clickableMessage.length() - 1));
                                clickableMessage.append(",{\"text\":\"[FIX] \",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss fix ").append(name).append("\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Fix ").append(name).append(" by placing a replica of the sign at the location\",\"color\":\"green\"}]}}]");
                            }
                            if (p.hasPermission(Permissions.FIX)) {
                                clickableMessage = new StringBuilder(clickableMessage.substring(0, clickableMessage.length() - 1));
                                clickableMessage.append(",{\"text\":\"[PURGE]\",\"bold\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss confirmpurge ").append(name).append("\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Remove ").append(name).append(" from the config file\",\"color\":\"red\"}]}}]");
                            }
                            p.spigot().sendMessage(ComponentSerializer.parse(clickableMessage.toString()));
                        }
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
