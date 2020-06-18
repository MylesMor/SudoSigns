package dev.mylesmor.sudosigns;

import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SudoSigns extends JavaPlugin {

    //TODO: Make permissions class
    public static final String createPerm = "sudosigns.admin.create";
    public static final String editPerm = "sudosigns.admin.edit";
    public static final String deletePerm = "sudosigns.admin.delete";
    public static final String runPerm = "sudosigns.admin.run";
    public static final String tpPerm = "sudosigns.admin.tp";
    public static final String copyPerm = "sudosigns.admin.copy";

    public static final String viewPerm = "sudosigns.view";
    public static final String helpPerm = "sudosigns.help";
    public static final String selectPerm = "sudosigns.select";
    public static final String nearPerm = "sudosigns.near";
    public static final String listPerm = "sudosigns.list";


    public static final String prefix = ChatColor.YELLOW + "[SUDOSIGNS]";

    public static Map<String, SudoSign> signs = new HashMap<>();
    public static Map<UUID, SudoUser> users = new HashMap<>();

    public static ConfigManager config;

    public static Plugin sudoSignsPlugin;

    //TODO: Remove permissions.
    //TODO: Save permissions.
    //TODO: Load in permissions.

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        sudoSignsPlugin = this;
        config = new ConfigManager();
    }


    @Override
    public void onDisable() {
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            final Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("ss") || cmd.getName().equalsIgnoreCase("sudosigns") || cmd.getName().equalsIgnoreCase("sudosign")) {
                users.put(p.getUniqueId(), new SudoUser(p));
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
                            viewSign(p, null);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("run")) {
                        if (p.hasPermission(runPerm)) {
                            run(p, null);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("near")) {
                        if (p.hasPermission(nearPerm)) {
                            near(p, null);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (p.hasPermission(listPerm)) {
                            list(p);
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
                            viewSign(p, args[1]);
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
                    } else if (args[0].equalsIgnoreCase("near")) {
                        if (p.hasPermission(nearPerm)) {
                            near(p, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("tp")) {
                        if (p.hasPermission(tpPerm)) {
                            tpTo(p, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    } else if (args[0].equalsIgnoreCase("copy")) {
                        if (p.hasPermission(copyPerm)) {
                            copy(p, null, args[1]);
                        } else {
                            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
                        }
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("copy")) {
                        if (p.hasPermission(copyPerm)) {
                            copy(p, args[1], args[2]);
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
        p.sendMessage(ChatColor.GRAY + "======" + ChatColor.YELLOW + " SudoSigns Help " + ChatColor.GRAY + "======");
        p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " <>" + ChatColor.GRAY + " indicates a required argument.");
        p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " []" + ChatColor.GRAY + " indicates an optional argument.");
        if (p.hasPermission(helpPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss help" + ChatColor.GRAY + " - Displays this menu.");
        }
        if (p.hasPermission(listPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss list" + ChatColor.GRAY + " - Lists all SudoSigns.");
        }
        if (p.hasPermission(nearPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss near [radius]" + ChatColor.GRAY + " - Lists SudoSigns within a specified radius (default 5 blocks).");
        }
        if (p.hasPermission(viewPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss view [name]" + ChatColor.GRAY + " - Displays the details of a SudoSign.");
        }
        if (p.hasPermission(runPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss run [name]" + ChatColor.GRAY + " - Runs a SudoSigns commands remotely.");
        }
        if (p.hasPermission(tpPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss tp [name]" + ChatColor.GRAY + " - Teleport to a SudoSign.");
        }
        if (p.hasPermission(createPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss create <name>" + ChatColor.GRAY + " - Creates a SudoSign with the specified name.");
        }
        if (p.hasPermission(editPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss edit [name]" + ChatColor.GRAY + " - Displays the editor for the specified SudoSign.");
        }
        if (p.hasPermission(copyPerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss copy [old-sign-name] <new-sign-name>" + ChatColor.GRAY + " - Copies the specified SudoSign to a new sign.");
        }
        if (p.hasPermission(deletePerm)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss delete [name]" + ChatColor.GRAY + " - Deletes the specified SudoSign.");
        }

    }

    private void run(Player p, String name) {
        if (name == null) {
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to run!");
            users.get(p.getUniqueId()).setCreate(true);
        } else if (signs.containsKey(name)) {
            signs.get(name).executeCommands(p);
        }
    }

    private void deleteSign(Player p, String name) {
        if (name == null) {
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to delete!");
            users.get(p.getUniqueId()).setDelete(true);
        } else if (signs.containsKey(name)) {
            signs.remove(name);
            config.deleteSign(name);
            p.sendMessage(prefix + ChatColor.GRAY + " Sign " + ChatColor.GOLD + name + ChatColor.GRAY + " successfully deleted!");
        }
    }

    private void list(Player p) {
        for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
            String message = Util.getSelectString(p, entry.getKey());
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        }
    }

    private void editSign(Player p, String name) {
        if (name == null) {
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to edit!");
            users.get(p.getUniqueId()).setEdit(true);
        } else if (signs.containsKey(name)) {
            SignEditor editor = new SignEditor(p, signs.get(name), users.get(p.getUniqueId()));
            users.get(p.getUniqueId()).setEditor(editor);
        } else {
            p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
        }
    }

    private void tpTo(Player p, String name) {
        if (signs.containsKey(name)) {
            Location newLoc = signs.get(name).getSign().getLocation();
            newLoc.setX(newLoc.getX() + 0.5);
            newLoc.setZ(newLoc.getZ() + 0.5);
            p.teleport(newLoc);
        } else {
            p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
        }
    }

    private void viewSign(Player p, String name) {
        if (p.hasPermission(viewPerm)) {
            if (name == null) {
                users.get(p.getUniqueId()).setDelete(true);
            } else if (signs.containsKey(name)) {
                SudoSign sign = signs.get(name);
                Location signLoc = sign.getSign().getLocation();
                String locString = "x=" + signLoc.getX() + " y=" + signLoc.getY() + " z=" + signLoc.getZ();
                p.sendMessage(prefix + ChatColor.GRAY + " Displaying details for sign " + ChatColor.GOLD + name + ChatColor.GRAY + ":");
                p.sendMessage(prefix + ChatColor.GRAY + " Location: " + ChatColor.LIGHT_PURPLE + locString);
                p.sendMessage(prefix + ChatColor.GRAY + " Player Commands: " + ChatColor.LIGHT_PURPLE + sign.getPlayerCommands().size());
                p.sendMessage(prefix + ChatColor.GRAY + " Console Commands: " + ChatColor.LIGHT_PURPLE + sign.getConsoleCommands().size());
            } else {
                p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + name + ChatColor.RED + " doesn't exist!");
            }
        }
    }
    

    public void createSign(Player p, String name) {
        if (!signs.containsKey(name)) {
            users.get(p.getUniqueId()).setCreate(true);
            users.get(p.getUniqueId()).setPassThru(name);
            signs.put(name, new SudoSign(name));
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to create!");
        } else {
            p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + name + ChatColor.RED + " already exists!");
        }
    }

    public void near(Player p, String radius) {
        int r;
        if (radius == null) {
            r = 5;
        } else {
            try {
                r = Integer.parseInt(radius);
            } catch (NumberFormatException e) {
                p.sendMessage(prefix + ChatColor.GOLD + radius + ChatColor.RED + " is not a valid number! Reverting to a radius of 5.");
                r = 5;
            }
        }
        p.sendMessage(prefix + ChatColor.GRAY + " Displaying SudoSigns within a radius of " + ChatColor.GOLD + r + ChatColor.GRAY + " blocks:");
        for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
            Location signLoc = entry.getValue().getSign().getLocation();
            if (signLoc.distance(p.getLocation()) <= r) {
                if (p.hasPermission(selectPerm)) {
                    select(p, entry.getKey());
                }
            }
        }
    }

    public void select(Player p, String name) {
        if (p.hasPermission(selectPerm)) {
            String message = Util.getSelectString(p, name);
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        } else {
            p.sendMessage(prefix + ChatColor.RED + " You don't have permission to do this!");
        }
    }

    public void copy(Player p, String oldName, String newName) {
        users.get(p.getUniqueId()).setPassThru(newName);
        if (oldName == null) {
            p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to copy from!");
            users.get(p.getUniqueId()).setSelectToCopy(true);
        } else {
            if (signs.containsKey(oldName)) {
                if (!signs.containsKey(newName)) {
                    p.sendMessage(prefix + ChatColor.GRAY + " Please click the sign you'd like to copy to!");
                    SudoSign oldSign = signs.get(oldName);
                    SudoSign newSign = new SudoSign(newName);
                    newSign.setPermissions(oldSign.getPermissions());
                    newSign.setPlayerCommands(oldSign.getPlayerCommands());
                    newSign.setConsoleCommands(oldSign.getConsoleCommands());
                    users.get(p.getUniqueId()).setCopy(true);
                    signs.put(newName, newSign);
                } else {
                    p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + newName + ChatColor.RED + " already exists!");
                }
            } else {
                p.sendMessage(prefix + ChatColor.RED + " A sign with name " + ChatColor.GOLD + oldName + ChatColor.RED + " doesn't exist!");
            }
        }
    }


    public void confirmDelete(Player p, String name) {
        if (p.hasPermission(deletePerm)) {
            String message = "[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Are you sure you want to delete sign \",\"color\":\"gray\"},{\"text\":\"" + name + "\",\"color\":\"gold\"},{\"text\":\"? \",\"color\":\"gray\"},{\"text\":\"[YES] \",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss delete " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Yes, delete the sign!\",\"color\":\"green\"}]}}]";
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        }
    }


}
