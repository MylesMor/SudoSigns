package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Purge {

    /**
     * Purge an invalid sign's config entry.
     * @param p The player running the command.
     * @param args 1 argument: The name of the sign.
     */
    public static void purge(Player p, String[] args) {
        if (p.hasPermission(Permissions.PURGE)) {
            if (args.length != 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss purge <name>" + ChatColor.GRAY + ".", null);
            } else {
                if (SudoSigns.config.purgeInvalidEntry(args[0], false)) {
                    Util.sudoSignsMessage(p, ChatColor.GREEN, "Sign " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " successfully purged from the config!", null);
                } else {
                    Util.sudoSignsMessage(p, ChatColor.RED, "Nothing purged! That entry is not invalid.", null);
                }
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }

    /**
     * Purge all invalid sign entries in the config.
     * @param p The player running the command.
     * @param args 0 arguments.
     */
    public static void purgeAll(Player p, String[] args) {
        if (p.hasPermission(Permissions.PURGE)) {
            if (SudoSigns.config.purgeInvalidEntry(null, true)) {
                Util.sudoSignsMessage(p, ChatColor.GREEN, "All invalid entries successfully purged from the config!", null);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
