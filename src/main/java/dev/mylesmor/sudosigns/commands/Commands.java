package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Commands implements CommandExecutor {

    Map<String, BiConsumer<Player, String[]>> commands = new HashMap<>();

    public Commands() {
        commands.put("help", Help::help);
        commands.put("near", Near::near);
        commands.put("list", List::list);
        commands.put("delete", Delete::delete);
        commands.put("edit", Edit::edit);
        commands.put("view", View::view);
        commands.put("run", Run::run);
        commands.put("confirmdelete", Delete::confirmDelete);
        commands.put("reload", Reload::reload);
        commands.put("create", Create::create);
        commands.put("tp", Teleport::tp);
        commands.put("copy", Copy::copy);
        commands.put("select", Select::select);
        commands.put("purge", Purge::purge);
        commands.put("confirmpurge", Purge::confirmPurge);
        commands.put("fix", Fix::fix);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            final Player p = (Player) sender;
                SudoSigns.users.computeIfAbsent(p.getUniqueId(), k -> new SudoUser(p));
                try {
                    BiConsumer<Player, String[]> command = commands.get(args[0].toLowerCase());
                    if (args.length > 1) {
                        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
                        command.accept(p, newArgs);
                    } else {
                        command.accept(p, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.sudoSignsMessage(p, ChatColor.RED, "Invalid command!" + ChatColor.GRAY + " Type " + ChatColor.LIGHT_PURPLE + "/ss help " + ChatColor.GRAY + "for a list of commands.", null);
                }
        }
        return true;
    }
}
