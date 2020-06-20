package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Map;

public class List {

    /**
     * Lists all signs.
     * @param p The player running the command.
     * @param args Not required
     */
    public static void list(Player p, String[] args) {
        if (p.hasPermission(Permissions.LIST)) {
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                String message = Util.getSelectString(p, entry.getKey());
                p.spigot().sendMessage(ComponentSerializer.parse(message));
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
