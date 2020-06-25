package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class View {

    /**
     * Views a sign's details, including location and number of permissions and commands assigned to it.
     * @param p The player running the command.
     * @param args 0 or 1 arguments: The name of the sign (or null for click selection).
     */
    public static void view(Player p, String[] args) {
        if (p.hasPermission(Permissions.VIEW)) {
            String name = null;
            if (args == null) {
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to view!", null);
                SudoSigns.users.get(p.getUniqueId()).setView(true);
                return;
            }
            if (args.length > 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss view [name]" + ChatColor.GRAY + ".", null);
                return;
            }
            name = args[0];
            SudoSign sign = SudoSigns.signs.get(name);
            if (sign != null) {
                Location signLoc = sign.getSign().getLocation();
                String locString = "x=" + signLoc.getX() + " y=" + signLoc.getY() + " z=" + signLoc.getZ();
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Displaying details for sign %NAME%:", name);
                if (p.hasPermission(Permissions.TP)) {
                    String locMessage = "[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Location: \",\"color\":\"gray\"},{\"text\":\"" + locString + " \",\"color\":\"light_purple\"},{\"text\":\"[TP]\",\"bold\":true,\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss tp " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Teleport to " + name + "\",\"color\":\"aqua\"}]}},{\"text\":\"\",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss delete NAME\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Yes, delete the sign!\",\"color\":\"green\"}]}}]";
                    p.spigot().sendMessage(ComponentSerializer.parse(locMessage));
                } else {
                    Util.sudoSignsMessage(p, ChatColor.GRAY,"Location: " + ChatColor.LIGHT_PURPLE + locString, null);
                }
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Messages: " + ChatColor.LIGHT_PURPLE + sign.getMessages().size(), null);
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Permissions: " + ChatColor.LIGHT_PURPLE + sign.getPermissions().size(), null);
                Util.sudoSignsMessage(p, ChatColor.GRAY,"Player Commands: " + ChatColor.LIGHT_PURPLE + sign.getPlayerCommands().size(), null);
                Util.sudoSignsMessage(p, ChatColor.GRAY,"Console Commands: " + ChatColor.LIGHT_PURPLE + sign.getConsoleCommands().size(), null);
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED,  "A sign with name %NAME% doesn't exist!", name);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
