package dev.mylesmor.sudosigns;

import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class SudoSigns extends JavaPlugin {

    public static final String createPerm = "sudosigns.create";
    public static final String editPerm = "sudosigns.edit";
    public static final String deletePerm = "sudosigns.delete";
    public static final String viewPerm = "sudosigns.view";
    public static final String helpPerm = "sudosigns.help";
    public static final String selectPerm = "sudosigns.select";
    public static final String runPerm = "sudosigns.run";

    public static final String prefix = ChatColor.YELLOW + "[SUDOSIGNS]";

    public static Map<String, SudoSign> signs = new HashMap<>();

    public static Map<Player, String> playersToClick = new HashMap<>();
    public static Map<Player, String> playersToCreate = new HashMap<>();

    public static Map<Player, SignEditor> editors = new HashMap<>();

    public static Map<Player, PlayerInput> textInput = new HashMap<>();

    public static Plugin sudoSignsPlugin;



    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        sudoSignsPlugin = this;
    }

    @Override
    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            final Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("ss") || cmd.getName().equalsIgnoreCase("sudosigns") || cmd.getName().equalsIgnoreCase("sudosign")) {
                if (args.length == 0) {
                    help(p);
                    return true;
                }
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
                        if (p.hasPermission(helpPerm)) {
                            help(p);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
                        if (p.hasPermission(deletePerm)) {
                            deleteSign(p, null);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e")) {
                        if (p.hasPermission(editPerm)) {
                            editSign(p, null);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("v")) {
                        if (p.hasPermission(viewPerm)) {
                            viewSign(null);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("run")) {
                        if (p.hasPermission(runPerm)) {
                            run(p, null);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    }
                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
                        if (p.hasPermission(createPerm)) {
                            createSign(p, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
                        if (p.hasPermission(deletePerm)) {
                            deleteSign(p, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e")) {
                        if (p.hasPermission(editPerm)) {
                            editSign(p, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("v")) {
                        if (p.hasPermission(viewPerm)) {
                            viewSign(args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("confirmdelete")) {
                        if (p.hasPermission(deletePerm)) {
                            confirmDelete(p, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("run")) {
                        if (p.hasPermission(runPerm)) {
                            run(p, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    }
                }
            }
        }
        return true;
    }

    private void help(Player p) {
    }

    private void run(Player p, String name) {
        if (name == null) {
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to run!");
            playersToClick.put(p, "RUN");
        } else if (signs.containsKey(name)) {
            signs.get(name).executeCommands(p);
        }
    }

    private void deleteSign(Player p, String name) {
        if (name == null) {
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to delete!");
            playersToClick.put(p, "DELETE");
        } else if (signs.containsKey(name)) {
            signs.remove(name);
            p.sendMessage(prefix + ChatColor.GRAY + " Sign " + ChatColor.GOLD + name + ChatColor.GRAY + " successfully deleted!");
        }
    }

    private void editSign(Player p, String name) {
        if (name == null) {
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to edit!");
            playersToClick.put(p, "EDIT");
        } else if (signs.containsKey(name)) {
            SignEditor editor = new SignEditor(p, signs.get(name));
            editors.put(p, editor);
        }
    }

    private void viewSign(String arg) {
    }
    

    public void createSign(Player p, String name) {
        if (!signs.containsKey(name)) {
            playersToCreate.put(p, name);
            signs.put(name, new SudoSign(name));
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to create!");
        } else {
            p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + name + ChatColor.RED + " already exists!");
        }
    }

    public void confirmDelete(Player p, String name) {
        if (p.hasPermission(deletePerm)) {
            if (signs.containsKey(name)) {
                signs.remove(name);
                p.sendMessage(prefix + ChatColor.GRAY + " Sign " + ChatColor.GOLD + name + ChatColor.GRAY + " successfully deleted!");

            } else {
                p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exists!");
            }
        }
    }
}
