package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Teleport {

    /**
     * Teleports a player to the sign.
     * @param p The player running the command.
     * @param args 1 argument: The name of the sign.
     */
    public static void tp(Player p, String[] args) {
        if (p.hasPermission(Permissions.TP)) {
            if (args == null || args.length != 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss tp <name>" + ChatColor.GRAY + ".", null);
                return;
            }
            String name = args[0];
            if (SudoSigns.signs.containsKey(name)) {
                Location newLoc = SudoSigns.signs.get(name).getSign().getLocation();
                newLoc.setX(newLoc.getX() + 0.5);
                newLoc.setZ(newLoc.getZ() + 0.5);
                p.teleport(newLoc);
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED, "A sign with name %NAME% doesn't exist!", name);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
