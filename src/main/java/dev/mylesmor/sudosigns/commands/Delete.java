package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Delete {
    /**
     * Deletes a sign.
     * @param p The player running the command.
     * @param args 1 or 2 arguments: The name of the sign (or null for click selection).
     */
    public static void delete(Player p, String[] args) {
        if (p.hasPermission(Permissions.DELETE)) {
            String name = null;
            if (args.length > 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss delete [name]" + ChatColor.GRAY + ".", null);
                return;
            }
            if (args.length == 1) name = args[0];
            if (name == null) {
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to delete!", null);
                SudoSigns.users.get(p.getUniqueId()).setDelete(true);
            } else if (SudoSigns.signs.containsKey(name)) {
                SudoSigns.signs.remove(name);
                SudoSigns.config.deleteSign(name);
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Sign %NAME% successfully deleted!", name);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }

    /**
     * Confirms deletion of a sign (only run when [DELETE] is clicked in the selection message).
     * @param p The player running the command.
     * @param args 1 argument: The name of the sign.
     */
    public static void confirmDelete(Player p, String[] args) {
        if (p.hasPermission(Permissions.DELETE)) {
            if (args.length != 1) return;
            String message = "[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Are you sure you want to delete sign \",\"color\":\"gray\"},{\"text\":\"" + args[0] + "\",\"color\":\"gold\"},{\"text\":\"? \",\"color\":\"gray\"},{\"text\":\"[YES] \",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss delete " + args[0] + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Yes, delete the sign!\",\"color\":\"green\"}]}}]";
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        }
    }
}
