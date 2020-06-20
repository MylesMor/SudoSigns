package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Create {

    /**
     * Creates a sign.
     * @param p The player running the command.
     * @param args 0 or 1 arguments: The name of the sign (or null for click selection).
     */
    public static void create(Player p, String[] args) {
        if (p.hasPermission(Permissions.CREATE)) {
            if (args.length > 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss create <name>" + ChatColor.GRAY + ".", null);
                return;
            }
            String name = args[0];
            if (Util.checkName(name)) {
                if (!SudoSigns.signs.containsKey(name)) {
                    SudoSigns.users.get(p.getUniqueId()).setCreate(true);
                    SudoSigns.users.get(p.getUniqueId()).setPassThru(name);
                    SudoSigns.signs.put(name, new SudoSign(name));
                    Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to create!", null);
                } else {
                    Util.sudoSignsMessage(p, ChatColor.RED, "A sign with name %NAME% already exists!", name);
                }
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED, "The name of a SudoSign must only contain numbers and letters!", null);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
