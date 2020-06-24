package dev.mylesmor.sudosigns;

import dev.mylesmor.sudosigns.commands.Commands;
import dev.mylesmor.sudosigns.config.ConfigManager;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.listeners.ChatListener;
import dev.mylesmor.sudosigns.listeners.InventoryListener;
import dev.mylesmor.sudosigns.listeners.PlayerListener;
import dev.mylesmor.sudosigns.listeners.SignListener;
import org.bukkit.command.CommandSender;
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

    public static ConfigManager config;

    public static Plugin sudoSignsPlugin;


    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        sudoSignsPlugin = this;
        config = new ConfigManager();
        this.getCommand("sudosigns").setExecutor(new Commands());

    }


    @Override
    public void onDisable() {

    }
}
