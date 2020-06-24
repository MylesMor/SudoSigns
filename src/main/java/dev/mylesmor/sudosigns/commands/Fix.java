package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Fix {
    /**
     * Attempt to fix an invalid entry by placing a sign in the config location.
     * @param p    The player running the command.
     * @param args 0 or 1 arguments: The name of the sign.
     */
    public static void fix(Player p, String[] args) {
        if (p.hasPermission(Permissions.FIX)) {
            String name = null;
            if (args.length > 2) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss fix [name]" + ChatColor.GRAY + ".", null);
                return;
            }
            if (args.length == 1) name = args[0];
            if (name != null) {
                if (SudoSigns.config.fixInvalidEntry(args[0], false)) {
                    SudoSigns.config.loadSigns();
                    if (SudoSigns.signs.containsKey(args[0])) {
                        Util.sudoSignsMessage(p, ChatColor.GREEN, "Sign " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " successfully fixed!", null);
                    } else {
                        Util.sudoSignsMessage(p, ChatColor.RED, "Sign " + ChatColor.GOLD + args[0] + ChatColor.RED + " was unable to be fixed!", null);
                    }
                } else {
                    Util.sudoSignsMessage(p, ChatColor.RED, "Nothing fixed! That entry is not invalid.", null);
                }
            } else {
                int size = SudoSigns.config.getInvalidEntries().size();
                if (SudoSigns.config.fixInvalidEntry(null, true)) {
                    SudoSigns.config.loadSigns();
                    if (SudoSigns.config.getInvalidEntries().size() == 0) {
                        Util.sudoSignsMessage(p, ChatColor.GREEN, "All invalid entries successfully purged from the config!", null);
                    } else if (SudoSigns.config.getInvalidEntries().size() == size) {
                        Util.sudoSignsMessage(p, ChatColor.RED, "No invalid entries were able to be fixed automatically!", null);
                    } else if (SudoSigns.config.getInvalidEntries().size() < size) {
                        Util.sudoSignsMessage(p, ChatColor.GREEN, "" + ChatColor.GOLD + (SudoSigns.config.getInvalidEntries().size() - size) + " invalid entries were able to be fixed automatically!" + ChatColor.GOLD + Integer.toString(SudoSigns.config.getInvalidEntries().size()) + ChatColor.RED + " were unable to be fixed!", null);
                    }
                }
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
