package dev.mylesmor.sudosigns;

import dev.mylesmor.sudosigns.commands.Commands;
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
        this.getCommand("sudosigns").setExecutor(new Commands());

    }


    @Override
    public void onDisable() {

    }
}
