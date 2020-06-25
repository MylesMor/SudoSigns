package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.Map;

public class Near {

    /**
     * Displays signs within a set radius.
     * @param p The player running the command.
     * @param args 0 or 1 arguments, with the only argument being the radius to search in (default is 5 blocks).
     */
    public static void near(Player p, String[] args) {
        if (p.hasPermission(Permissions.NEAR)) {
            int r;
            String radius = null;
            if (args != null) {
                if (args.length > 1) {
                    Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss near [radius]" + ChatColor.GRAY + ".", null);
                    return;
                } else if (args.length == 1) {
                    radius = args[0];
                }
            }
            if (radius == null) {
                r = 5;
            } else {
                try {
                    r = Integer.parseInt(radius);
                } catch (NumberFormatException e) {
                    Util.sudoSignsMessage(p, ChatColor.RED, ChatColor.GOLD + radius + ChatColor.RED + " is not a valid number! Reverting to a radius of 5.", null);
                    r = 5;
                }
            }
            Util.sudoSignsMessage(p, ChatColor.GRAY, "Displaying SudoSigns within a radius of %NAME% blocks:", Integer.toString(r));
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                Location signLoc = entry.getValue().getSign().getLocation();
                if (signLoc.distance(p.getLocation()) <= r) {
                    String message = Util.getSelectString(p, entry.getKey());
                    p.spigot().sendMessage(ComponentSerializer.parse(message));
                }
            }
        } else {
        Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
