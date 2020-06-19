package dev.mylesmor.sudosigns.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Util {

    public static String getSelectString(Player p, String name) {
        String message = "[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Sign: \",\"color\":\"gray\"},{\"text\":\"" + name + " \",\"bold\":true,\"color\":\"gold\"}]";
        if (p.hasPermission(Permissions.VIEW)) {
            message = message.substring(0, message.length() - 1);
            message += ",{\"text\":\"[VIEW] \",\"bold\":true,\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss view " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"View " + name + "'s details\",\"color\":\"blue\"}]}}]";
        }
        if (p.hasPermission(Permissions.RUN)) {
            message = message.substring(0, message.length() - 1);
            message += ",{\"text\":\"[RUN] \",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss run " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Run " + name + "'s commands\",\"color\":\"green\"}]}}]";
        }
        if (p.hasPermission(Permissions.TP)) {
            message = message.substring(0, message.length() - 1);
            message += ",{\"text\":\"[TP] \",\"bold\":true,\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss tp " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Teleport to " + name + "\",\"color\":\"aqua\"}]}}]";

        }
        if (p.hasPermission(Permissions.EDIT)) {
            message = message.substring(0, message.length() - 1);
            message += ",{\"text\":\"[EDIT] \",\"bold\":true,\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss edit " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Edit "+ name +"\",\"color\":\"light_purple\"}]}}]";
        }

        if (p.hasPermission(Permissions.DELETE)) {
            message = message.substring(0, message.length() - 1);
            message += ",{\"text\":\"[DELETE]\",\"bold\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss confirmdelete " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Delete " + name + "\",\"color\":\"red\"}]}}]";
        }
        return message;
    }
}
