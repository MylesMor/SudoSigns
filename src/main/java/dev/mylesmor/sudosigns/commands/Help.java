package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help {

    /**
     * Sends the player a help message.
     * @param p The player running the command.
     * @param args Not required.
     */
    public static void help(Player p, String[] args) {
        if (p.hasPermission(Permissions.HELP)) {
            p.sendMessage(ChatColor.GRAY + "======" + ChatColor.YELLOW + " SudoSigns Help " + ChatColor.GRAY + "======");
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " <>" + ChatColor.GRAY + " indicates a required argument.");
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " []" + ChatColor.GRAY + " indicates an optional argument.");
            if (p.hasPermission(Permissions.HELP)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss help" + ChatColor.GRAY + " - Displays this menu.");
            }
            if (p.hasPermission(Permissions.LIST)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss list" + ChatColor.GRAY + " - Lists all SudoSigns.");
            }
            if (p.hasPermission(Permissions.NEAR)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss near [radius]" + ChatColor.GRAY + " - Lists SudoSigns within a specified radius (default 5 blocks).");
            }
            if (p.hasPermission(Permissions.VIEW)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss view [name]" + ChatColor.GRAY + " - Displays the details of a SudoSign.");
            }
            if (p.hasPermission(Permissions.RUN)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss run [name]" + ChatColor.GRAY + " - Runs a SudoSigns commands remotely.");
            }
            if (p.hasPermission(Permissions.TP)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss tp [name]" + ChatColor.GRAY + " - Teleport to a SudoSign.");
            }
            if (p.hasPermission(Permissions.CREATE)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss create <name>" + ChatColor.GRAY + " - Creates a SudoSign with the specified name.");
            }
            if (p.hasPermission(Permissions.EDIT)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss edit [name]" + ChatColor.GRAY + " - Displays the editor for the specified SudoSign.");
            }
            if (p.hasPermission(Permissions.COPY)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss copy [old-sign-name] <new-sign-name>" + ChatColor.GRAY + " - Copies the specified SudoSign to a new sign.");
            }
            if (p.hasPermission(Permissions.DELETE)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss delete [name]" + ChatColor.GRAY + " - Deletes the specified SudoSign.");
            }
            if (p.hasPermission(Permissions.RELOAD)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss reload" + ChatColor.GRAY + " - Reloads the configuration file (signs.yml).");
            }
            if (p.hasPermission(Permissions.PURGE)) {
                p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss purge [name]" + ChatColor.GRAY + " - Removes a specified invalid entry from the config (leave name black to purge all invalid entries).");
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
