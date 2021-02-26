package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SudoSignsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player p = (Player) commandSender;
            if (args.length == 1) {
                ArrayList<String> commands = new ArrayList<>();
                if (p.hasPermission(Permissions.HELP)) {
                    commands.add("help");
                }
                if (p.hasPermission(Permissions.RELOAD)) {
                    commands.add("reload");
                }
                if (p.hasPermission(Permissions.CREATE)) {
                    commands.add("create");
                }
                if (p.hasPermission(Permissions.LIST)) {
                    commands.add("list");
                }
                if (p.hasPermission(Permissions.NEAR)) {
                    commands.add("near");
                }
                if (p.hasPermission(Permissions.DELETE)) {
                    commands.add("delete");
                }
                if (p.hasPermission(Permissions.VIEW)) {
                    commands.add("view");
                }
                if (p.hasPermission(Permissions.RUN)) {
                    commands.add("run");
                }
                if (p.hasPermission(Permissions.RELOAD)) {
                    commands.add("reload");
                }
                if (p.hasPermission(Permissions.TP)) {
                    commands.add("tp");
                }
                if (p.hasPermission(Permissions.COPY)) {
                    commands.add("copy");
                }
                if (p.hasPermission(Permissions.SELECT)) {
                    commands.add("select");
                }
                if (p.hasPermission(Permissions.PURGE)) {
                    commands.add("purge");
                }
                if (p.hasPermission(Permissions.FIX)) {
                    commands.add("fix");
                }
                if (p.hasPermission(Permissions.EDIT)) {
                    commands.add("edit");
                }
                return commands;
            }
            if (args.length == 2) {
                ArrayList<String> suggestions = new ArrayList<>();
                switch (args[0]) {
                    case "create":
                        if (p.hasPermission(Permissions.CREATE)) {
                            suggestions.add("<sign-name>");
                        }
                        return suggestions;
                    case "delete":
                    case "view":
                    case "run":
                    case "tp":
                    case "copy":
                    case "select":
                    case "edit":
                        if (p.hasPermission(Permissions.SELECT) || p.hasPermission(Permissions.VIEW)
                                || p.hasPermission(Permissions.RUN) || p.hasPermission(Permissions.TP)
                                || p.hasPermission(Permissions.COPY) || p.hasPermission(Permissions.SELECT)
                                || p.hasPermission(Permissions.EDIT)) {
                            for (SudoSign s : SudoSigns.signs.values()) {
                                suggestions.add(s.getName());
                            }
                        }
                        return suggestions;
                    case "fix":
                    case "purge":
                        if (p.hasPermission(Permissions.PURGE)) {
                            suggestions.addAll(SudoSigns.invalidSigns);
                        }
                        return suggestions;
                    case "near":
                        if (p.hasPermission(Permissions.NEAR)) {
                            suggestions.add("2");
                            suggestions.add("5");
                            suggestions.add("10");
                            suggestions.add("25");
                            suggestions.add("50");
                        }
                        return suggestions;
                }

            }
            if (args.length == 3) {
                ArrayList<String> suggestions = new ArrayList<>();
                switch (args[0]) {
                    case "copy":
                        suggestions.add("<new-sign-name>");
                        return suggestions;
                }
            }
        }


        return null;
    }
}
