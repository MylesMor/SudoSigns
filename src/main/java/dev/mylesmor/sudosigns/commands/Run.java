package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Run {
    /**
     * Runs a sign remotely.
     * @param p The player running the command.
     * @param args 0 or 1 arguments: The name of the sign (or null for click selection).
     */
    public static void run(Player p, String[] args) {
        if (p.hasPermission(Permissions.RUN)) {
            String name;
            if (args == null) {
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to run!", null);
                SudoSigns.users.get(p.getUniqueId()).setRun(true);
                return;
            }
            if (args.length > 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss run [name]" + ChatColor.GRAY + ".", null);
                return;
            }
            name = args[0];
            if (SudoSigns.signs.containsKey(name)) {
                SudoSign sign = SudoSigns.signs.get(name);
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Executing " + ChatColor.LIGHT_PURPLE + sign.getMessages().size() + ChatColor.GRAY + " messages and " + ChatColor.LIGHT_PURPLE + (sign.getPlayerCommands().size() + sign.getConsoleCommands().size()) + ChatColor.GRAY + " commands...", null);
                SudoSigns.signs.get(name).executeCommands(p);
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED,  "A sign with name %NAME% doesn't exist!", name);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
