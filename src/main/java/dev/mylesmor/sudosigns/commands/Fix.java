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
            String name;
            if (args == null) {
                int size = SudoSigns.config.getInvalidEntriesManager().getInvalidEntries().size();
                if (SudoSigns.config.getInvalidEntriesManager().fixInvalidEntry(null, true)) {
                    SudoSigns.config.loadSigns();
                    if (SudoSigns.config.getInvalidEntriesManager().getInvalidEntries().size() == 0) {
                        Util.sudoSignsMessage(p, ChatColor.GREEN, "All invalid entries successfully purged from the config!", null);
                    } else if (SudoSigns.config.getInvalidEntriesManager().getInvalidEntries().size() == size) {
                        Util.sudoSignsMessage(p, ChatColor.RED, "No invalid entries were able to be fixed automatically!", null);
                    } else if (SudoSigns.config.getInvalidEntriesManager().getInvalidEntries().size() < size) {
                        Util.sudoSignsMessage(p, ChatColor.GREEN, "" + ChatColor.GOLD + (SudoSigns.config.getInvalidEntriesManager().getInvalidEntries().size() - size) + " invalid entries were able to be fixed automatically!" + ChatColor.GOLD + Integer.toString(SudoSigns.config.getInvalidEntriesManager().getInvalidEntries().size()) + ChatColor.RED + " were unable to be fixed!", null);
                    }
                }
                return;
            }
            if (args.length > 2) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss fix [name]" + ChatColor.GRAY + ".", null);
                return;
            }
            name = args[0];
            if (SudoSigns.config.getInvalidEntriesManager().fixInvalidEntry(name, false)) {
                SudoSigns.config.loadSigns();
                if (SudoSigns.signs.containsKey(name)) {
                    Util.sudoSignsMessage(p, ChatColor.GREEN, "Sign " + ChatColor.GOLD + name + ChatColor.GREEN + " successfully fixed!", null);
                } else {
                    Util.sudoSignsMessage(p, ChatColor.RED, "Sign " + ChatColor.GOLD + name + ChatColor.RED + " was unable to be fixed!", null);
                }
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED, "Nothing fixed! That entry is not invalid.", null);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
