package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SignEditor;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Edit {

    /**
     * Edits a sign.
     * @param p The player running the command.
     * @param args 0 or 1 arguments: The name of the sign (or null for click selection).
     */
    public static void edit(Player p, String[] args) {
        if (p.hasPermission(Permissions.EDIT)) {
            String name = null;
            if (args.length > 1) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Invalid syntax! " + ChatColor.GRAY + "Correct syntax: " + ChatColor.LIGHT_PURPLE + "/ss edit [name]" + ChatColor.GRAY + ".", null);
                return;
            }
            if (args.length == 1) name = args[0];
            if (name == null) {
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to edit!", null);
                SudoSigns.users.get(p.getUniqueId()).setEdit(true);
            } else if (SudoSigns.signs.containsKey(name)) {
                SignEditor editor = new SignEditor(p, SudoSigns.signs.get(name), SudoSigns.users.get(p.getUniqueId()));
                SudoSigns.users.get(p.getUniqueId()).setEditor(editor);
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED, "A sign with name %NAME% doesn't exist!", name);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
        }
    }
}
