package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Select {

    /**
     * Select a sign (display selection message in chat).
     * @param p The player running the command.
     * @param args 1 argument: The name of the sign.
     */
    public static void select(Player p, String[] args) {
        if (p.hasPermission(Permissions.SELECT)) {
            if (args.length != 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss select <name>" + ChatColor.GRAY + ".", null);
            } else {
                String message = Util.getSelectString(p, args[0]);
                p.spigot().sendMessage(ComponentSerializer.parse(message));
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
