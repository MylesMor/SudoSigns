package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Copy {
    /**
     * Copies one sign's data to another.
     * @param p The player running the command.
     * @param args 1 or 2 arguments: If 1, the name of the new sign. If 2, the name of the old sign in index 0 and name of new one in index 1.
     */
    public static void copy(Player p, String[] args) {
        if (p.hasPermission(Permissions.COPY)) {
            String oldName = null;
            String newName = null;
            if (args.length < 1 || args.length > 2) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss copy [old-sign-name] <new-sign-name>" + ChatColor.GRAY + ".", null);
                return;
            }
            if (args.length == 1) {
                newName = args[0];
            } else {
                newName = args[1];
                oldName = args[0];
            }

            if (Util.checkName(newName)) {
                SudoSigns.users.get(p.getUniqueId()).setPassThru(newName);
                if (oldName == null) {
                    Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to copy from!", null);
                    SudoSigns.users.get(p.getUniqueId()).setSelectToCopy(true);
                } else {
                    SudoSign oldSign = SudoSigns.signs.get(oldName);
                    if (oldSign != null) {
                        if (!SudoSigns.signs.containsKey(newName)) {
                            Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to copy to!", null);
                            SudoSign newSign = new SudoSign(newName);
                            newSign.setPermissions(oldSign.getPermissions());
                            newSign.setPlayerCommands(oldSign.getPlayerCommands());
                            newSign.setConsoleCommands(oldSign.getConsoleCommands());
                            SudoSigns.users.get(p.getUniqueId()).setCopy(true);
                            SudoSigns.signs.put(newName, newSign);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "A sign with name %NAME% already exists!", newName);
                        }
                    } else {
                        Util.sudoSignsMessage(p, ChatColor.GRAY, "A sign with name %NAME% doesn't exist!", oldName);
                    }
                }
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED, "The name of a SudoSign must only contain numbers and letters!", null);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
