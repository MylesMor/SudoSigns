package dev.mylesmor.sudosigns;

import dev.mylesmor.sudosigns.config.ConfigManager;
import dev.mylesmor.sudosigns.data.SignEditor;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.listeners.ChatListener;
import dev.mylesmor.sudosigns.listeners.InventoryListener;
import dev.mylesmor.sudosigns.listeners.SignListener;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Main class for SudoSigns.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class SudoSigns extends JavaPlugin {
    public static Map<String, SudoSign> signs = new HashMap<>();
    public static Map<UUID, SudoUser> users = new HashMap<>();
    Map<String, BiConsumer<CommandSender, String[]>> commands = new HashMap<>();

    public static ConfigManager config;

    public static Plugin sudoSignsPlugin;


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
                        if (p.hasPermission(Permissions.HELP)) {
                            help(p);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
                        if (p.hasPermission(Permissions.DELETE)) {
                            deleteSign(p, null);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e")) {
                        if (p.hasPermission(Permissions.EDIT)) {
                            editSign(p, null);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("v")) {
                        if (p.hasPermission(Permissions.VIEW)) {
                            viewSign(p, null);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("run")) {
                        if (p.hasPermission(Permissions.RUN)) {
                            run(p, null);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("near")) {
                        if (p.hasPermission(Permissions.NEAR)) {
                            near(p, null);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("list")) {
                        if (p.hasPermission(Permissions.LIST)) {
                            list(p);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (p.hasPermission(Permissions.RELOAD)) {
                            reloadConfig(p);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    }

                }
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("c")) {
                        if (p.hasPermission(Permissions.CREATE)) {
                            createSign(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
                        if (p.hasPermission(Permissions.DELETE)) {
                            deleteSign(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("edit") || args[0].equalsIgnoreCase("e")) {
                        if (p.hasPermission(Permissions.EDIT)) {
                            editSign(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("v")) {
                        if (p.hasPermission(Permissions.VIEW)) {
                            viewSign(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("confirmdelete")) {
                        if (p.hasPermission(Permissions.DELETE)) {
                            confirmDelete(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("run")) {
                        if (p.hasPermission(Permissions.RUN)) {
                            run(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("near")) {
                        if (p.hasPermission(Permissions.NEAR)) {
                            near(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("tp")) {
                        if (p.hasPermission(Permissions.TP)) {
                            tpTo(p, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    } else if (args[0].equalsIgnoreCase("copy")) {
                        if (p.hasPermission(Permissions.COPY)) {
                            copy(p, null, args[1]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("copy")) {
                        if (p.hasPermission(Permissions.COPY)) {
                            copy(p, args[1], args[2]);
                        } else {
                            Util.sudoSignsMessage(p, ChatColor.RED, "You don't have permission to do this!", null);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sends the player a help message.
     * @param p The player running the command.
     */
    private void help(Player p) {
        p.sendMessage(ChatColor.GRAY + "======" + ChatColor.YELLOW + " SudoSigns Help " + ChatColor.GRAY + "======");
        p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " <>" + ChatColor.GRAY + " indicates a required argument.");
        p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " []" + ChatColor.GRAY + " indicates an optional argument.");
        if (p.hasPermission(Permissions.HELP)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss help" + ChatColor.GRAY + " - Displays this menu.");
        }
        if (p.hasPermission(Permissions.LIST)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss list" + ChatColor.GRAY + " - Lists all SudoSigns.");
        }
        if (p.hasPermission(Permissions.NEAR)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss near [radius]" + ChatColor.GRAY + " - Lists SudoSigns within a specified radius (default 5 blocks).");
        }
        if (p.hasPermission(Permissions.VIEW)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss view [name]" + ChatColor.GRAY + " - Displays the details of a SudoSign.");
        }
        if (p.hasPermission(Permissions.RUN)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss run [name]" + ChatColor.GRAY + " - Runs a SudoSigns commands remotely.");
        }
        if (p.hasPermission(Permissions.TP)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss tp [name]" + ChatColor.GRAY + " - Teleport to a SudoSign.");
        }
        if (p.hasPermission(Permissions.CREATE)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss create <name>" + ChatColor.GRAY + " - Creates a SudoSign with the specified name.");
        }
        if (p.hasPermission(Permissions.EDIT)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss edit [name]" + ChatColor.GRAY + " - Displays the editor for the specified SudoSign.");
        }
        if (p.hasPermission(Permissions.COPY)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss copy [old-sign-name] <new-sign-name>" + ChatColor.GRAY + " - Copies the specified SudoSign to a new sign.");
        }
        if (p.hasPermission(Permissions.DELETE)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss delete [name]" + ChatColor.GRAY + " - Deletes the specified SudoSign.");
        }
        if (p.hasPermission(Permissions.RELOAD)) {
            p.sendMessage(ChatColor.YELLOW + "[!]" + ChatColor.LIGHT_PURPLE + " /ss reload" + ChatColor.GRAY + " - Reloads the configuration file (signs.yml).");
        }

    }

    /**
     * Runs a sign remotely.
     * @param p The player running the command.
     * @param name The name of the sign (or null for click selection).
     */
    private void run(Player p, String name) {
        if (name == null) {
            Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to run!!", null);
            users.get(p.getUniqueId()).setCreate(true);
        } else if (signs.containsKey(name)) {
            signs.get(name).executeCommands(p);
        }
    }

    /**
     * Deletes a sign.
     * @param p The player running the command.
     * @param name The name of the sign (or null for click selection).
     */
    private void deleteSign(Player p, String name) {
        if (name == null) {
            Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to delete!", null);
            users.get(p.getUniqueId()).setDelete(true);
        } else if (signs.containsKey(name)) {
            signs.remove(name);
            config.deleteSign(name);
            Util.sudoSignsMessage(p, ChatColor.GRAY,  "Sign %NAME% successfully deleted!", null);
        }
    }

    /**
     * Lists all signs.
     * @param p The player running the command.
     */
    private void list(Player p) {
        for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
            String message = Util.getSelectString(p, entry.getKey());
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        }
    }

    /**
     * Edits a sign.
     * @param p The player running the command.
     * @param name The name of the sign (or null for click selection).
     */
    private void editSign(Player p, String name) {
        if (name == null) {
            Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to edit!", null);
            users.get(p.getUniqueId()).setEdit(true);
        } else if (signs.containsKey(name)) {
            SignEditor editor = new SignEditor(p, signs.get(name), users.get(p.getUniqueId()));
            users.get(p.getUniqueId()).setEditor(editor);
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "A sign with name %NAME% doesn't exist!", name);
        }
    }

    /**
     * Teleports a player to the sign.
     * @param p The player running the command.
     * @param name The name of the sign.
     */
    private void tpTo(Player p, String name) {
        if (signs.containsKey(name)) {
            Location newLoc = signs.get(name).getSign().getLocation();
            newLoc.setX(newLoc.getX() + 0.5);
            newLoc.setZ(newLoc.getZ() + 0.5);
            p.teleport(newLoc);
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED,  "A sign with name %NAME% doesn't exist!", name);
        }
    }

    /**
     * Views a sign's details, including location and number of permissions and commands assigned to it.
     * @param p The player running the command.
     * @param name The name of the sign (or null for click selection).
     */
    private void viewSign(Player p, String name) {
        if (p.hasPermission(Permissions.VIEW)) {
            SudoSign sign = signs.get(name);
            if (name == null) {
                users.get(p.getUniqueId()).setDelete(true);
            } else if (sign != null) {
                Location signLoc = sign.getSign().getLocation();
                String locString = "x=" + signLoc.getX() + " y=" + signLoc.getY() + " z=" + signLoc.getZ();
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Displaying details for sign %NAME%:", name);
                Util.sudoSignsMessage(p, ChatColor.GRAY,"Location: " + ChatColor.LIGHT_PURPLE + locString, null);
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Permissions: " + ChatColor.LIGHT_PURPLE + sign.getPermissions().size(), null);
                Util.sudoSignsMessage(p, ChatColor.GRAY,"Player Commands: " + ChatColor.LIGHT_PURPLE + sign.getPlayerCommands().size(), null);
                Util.sudoSignsMessage(p, ChatColor.GRAY,"Console Commands: " + ChatColor.LIGHT_PURPLE + sign.getConsoleCommands().size(), null);
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED,  "A sign with name %NAME% doesn't exist!", name);
            }
        }
    }

    /**
     * Creates a sign.
     * @param p The player running the command.
     * @param name The name of the sign (or null for click selection).
     */
    public void createSign(Player p, String name) {
        if (Util.checkName(name)) {
            if (!signs.containsKey(name)) {
                users.get(p.getUniqueId()).setCreate(true);
                users.get(p.getUniqueId()).setPassThru(name);
                signs.put(name, new SudoSign(name));
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to create!", null);
            } else {
                Util.sudoSignsMessage(p, ChatColor.RED, "A sign with name %NAME% already exists!", name);
            }
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED, "The name of a SudoSign must only contain numbers and letters!", null);
        }
    }

    /**
     * Displays signs within a set radius.
     * @param p The player running the command.
     * @param radius The radius to search for signs within (or null for a default radius of 5).
     */
    public void near(Player p, String radius) {
        int r;
        if (radius == null) {
            r = 5;
        } else {
            try {
                r = Integer.parseInt(radius);
            } catch (NumberFormatException e) {
                Util.sudoSignsMessage(p, ChatColor.RED,ChatColor.GOLD + radius + ChatColor.RED + " is not a valid number! Reverting to a radius of 5.", null);
                r = 5;
            }
        }
        Util.sudoSignsMessage(p, ChatColor.GRAY, "Displaying SudoSigns within a radius of %NAME% blocks:", Integer.toString(r));
        for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
            Location signLoc = entry.getValue().getSign().getLocation();
            if (signLoc.distance(p.getLocation()) <= r) {
                if (p.hasPermission(Permissions.SELECT)) {
                    select(p, entry.getKey());
                }
            }
        }
    }

    /**
     * Reloads the config file (signs.yml)
     * @param p The player running the command.
     */
    public void reloadConfig(Player p) {
        Util.sudoSignsMessage(p, ChatColor.GRAY, "Reloading config...", null);

        if (config.loadCustomConfig() && config.loadSigns()) {
            Util.sudoSignsMessage(p, ChatColor.GREEN, "Config successfully reloaded!", null);
        } else {
            Bukkit.getLogger().warning("[SUDOSIGNS] There was an error with the SudoSigns config!");
            Util.sudoSignsMessage(p, ChatColor.RED, "There was an error with the SudoSigns config! Continuing to use old config...", null);
        }

    }

    /**
     * Select a sign (display selection message in chat).
     * @param p The player running the command.
     * @param name The name of the sign.
     */
    public void select(Player p, String name) {
        if (p.hasPermission(Permissions.SELECT)) {
            String message = Util.getSelectString(p, name);
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        } else {
            Util.sudoSignsMessage(p, ChatColor.RED,"You don't have permission to do this!", null);
        }
    }

    /**
     * Copies one sign's data to another.
     * @param p The player running the command.
     * @param oldName The name of the sign (or null for click selection).
     * @param newName The name of the new sign to copy to.
     */
    public void copy(Player p, String oldName, String newName) {
        if (Util.checkName(newName)) {
            users.get(p.getUniqueId()).setPassThru(newName);
            if (oldName == null) {
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to copy from!", null);
                users.get(p.getUniqueId()).setSelectToCopy(true);
            } else {
                SudoSign oldSign = signs.get(oldName);
                if (oldSign != null) {
                    if (!signs.containsKey(newName)) {
                        Util.sudoSignsMessage(p, ChatColor.GRAY, "Please click the sign you'd like to copy to!", null);
                        SudoSign newSign = new SudoSign(newName);
                        newSign.setPermissions(oldSign.getPermissions());
                        newSign.setPlayerCommands(oldSign.getPlayerCommands());
                        newSign.setConsoleCommands(oldSign.getConsoleCommands());
                        users.get(p.getUniqueId()).setCopy(true);
                        signs.put(newName, newSign);
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
    }

    /**
     * Confirms deletion of a sign (only run when [DELETE] is clicked in the selection message).
     * @param p The player running the command.
     * @param name The name of the sign.
     */
    public void confirmDelete(Player p, String name) {
        if (p.hasPermission(Permissions.DELETE)) {
            String message = "[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Are you sure you want to delete sign \",\"color\":\"gray\"},{\"text\":\"" + name + "\",\"color\":\"gold\"},{\"text\":\"? \",\"color\":\"gray\"},{\"text\":\"[YES] \",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss delete " + name + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Yes, delete the sign!\",\"color\":\"green\"}]}}]";
            p.spigot().sendMessage(ComponentSerializer.parse(message));
        }
    }


}
