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
     * @param name The name of the sign.
     */
    public static void select(Player p, String name) {
        if (p.hasPermission(Permissions.SELECT)) {
            String message = Util.getSelectString(p, name);
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED,"You don't have permission to do this!", null);
        }
    }
}
