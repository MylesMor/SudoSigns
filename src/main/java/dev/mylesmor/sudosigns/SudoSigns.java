package dev.mylesmor.sudosigns;

import dev.mylesmor.sudosigns.commands.Commands;
import dev.mylesmor.sudosigns.commands.SudoSignsTabCompleter;
import dev.mylesmor.sudosigns.config.ConfigManager;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.listeners.ChatListener;
import dev.mylesmor.sudosigns.listeners.InventoryListener;
import dev.mylesmor.sudosigns.listeners.PlayerListener;
import dev.mylesmor.sudosigns.listeners.SignListener;
import dev.mylesmor.sudosigns.util.UpdateChecker;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    public static ArrayList<String> invalidSigns = new ArrayList<>();

    public static Map<UUID, SudoUser> users = new HashMap<>();

    public static ConfigManager config;

    public static Plugin sudoSignsPlugin;

    public static String version;

    public static Economy econ = null;


    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            Bukkit.getLogger().warning("[SUDOSIGNS] Vault not found, sign prices disabled...");
        }
        new UpdateChecker(this, 80758).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getLogger().info("[SUDOSIGNS] SudoSigns is up-to-date.");
            } else {
                Bukkit.getLogger().warning("[SUDOSIGNS] There is a new update available (" + version + ")! Please update for all the latest features, bug fixes and improvements! https://www.spigotmc.org/resources/sudosigns-commands-messages-on-signs.80758/");
            }
        });
        String version = getServer().getVersion().split("MC: ")[1];
        SudoSigns.version = version.substring(0, version.length()-1);
        getServer().getPluginManager().registerEvents(new SignListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        sudoSignsPlugin = this;
        config = new ConfigManager();
        config.loadModules();
        this.getCommand("sudosigns").setExecutor(new Commands());
        this.getCommand("sudosigns").setTabCompleter(new SudoSignsTabCompleter());
    }

    public boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }


    @Override
    public void onDisable() {

    }
}
