package dev.mylesmor.sudosigns.util;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static final String prefix = ChatColor.YELLOW + "[SUDOSIGNS]";

    public static String getSelectString(Player p, String name) {
        String message = "[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Sign: \",\"color\":\"gray\"},{\"text\":\"" + name + " \",\"bold\":true,\"color\":\"gold\"}]";
        if (p.hasPermission(Permissions.VIEW)) {
            message = message.substring(0, message.length() - 1);
            message += ",{\"text\":\"[V] \",\"bold\":true,\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss view " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"View " + name + "'s details\",\"color\":\"blue\"}]}}]";
        }
        if (p.hasPermission(Permissions.RUN)) {
            message = message.substring(0, message.length() - 1);
            message += ",{\"text\":\"[R] \",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss run " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Run " + name + "'s commands\",\"color\":\"green\"}]}}]";
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
            message += ",{\"text\":\"[DEL]\",\"bold\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss confirmdelete " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Delete " + name + "\",\"color\":\"red\"}]}}]";
        }
        return message;
    }

    public static void sudoSignsMessage(Player p, ChatColor color, String message, String name) {
        if (message.contains("%NAME%")) {
             message = message.replace("%NAME%", ChatColor.GOLD + name + color);
        }
        p.sendMessage(prefix + color + " " + message);
    }

    public static boolean checkName(String name) {
        String regex = "^[A-z0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static String findSign(Sign s) {
        String found = null;
        for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
            Sign sign = entry.getValue().getSign();
            if (sign != null && sign.equals(s)) {
                found = entry.getKey();
            }
        }
        return found;
    }
}
