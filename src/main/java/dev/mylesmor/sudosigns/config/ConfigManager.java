package dev.mylesmor.sudosigns.config;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.commands.SignCommand;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The class for managing the plugin's config.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class ConfigManager {

    private FileConfiguration signConfig;
    private File signConfigFile;

    public ConfigManager() {
        createCustomConfig();
        loadCustomConfig();
        loadSigns();
    }

    private void createCustomConfig() {
        if (!SudoSigns.sudoSignsPlugin.getDataFolder().exists()) {
            SudoSigns.sudoSignsPlugin.getDataFolder().mkdir();
        }
        signConfigFile = new File(SudoSigns.sudoSignsPlugin.getDataFolder(), "signs.yml");
        if (!signConfigFile.exists()) {
            try {
                signConfigFile.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().warning("[SUDOSIGNS] Failed to create config!");
                e.printStackTrace();
            }
        }
    }

    private void loadCustomConfig() {
        signConfig = new YamlConfiguration();
        try {
            signConfig.load(signConfigFile);
            if (!signConfig.isConfigurationSection("signs")) {
                signConfig.createSection("signs");
                save();
            }
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to initialise signs.yml!");
            e.printStackTrace();
        }
    }

    public void loadSigns() {
        SudoSigns.signs.clear();
        Set<String> signSection = signConfig.getConfigurationSection("signs").getKeys(false);
        String name;
        for (String key : signSection) {
            name = key;
            try {
                ConfigurationSection locSec = signConfig.getConfigurationSection("signs." + key + ".location");
                String world = locSec.getString("world");
                double x = locSec.getDouble("x");
                double y = locSec.getDouble("y");
                double z = locSec.getDouble("z");
                Location loc = new Location(Bukkit.getServer().getWorld(world), x, y, z);
                Sign sign = (Sign) loc.getBlock().getState();
                SudoSign ss = new SudoSign(key);
                ss.setSign(sign);
                List<String> pCommands = signConfig.getStringList("signs." + key + ".player-commands");
                List<String> cCommands = signConfig.getStringList("signs." + key + ".console-commands");
                List<String> permissions = signConfig.getStringList("signs." + key + ".permissions");
                for (String cmd : pCommands) {
                    ss.addPlayerCommand(new SignCommand(cmd, PlayerInput.PLAYER_COMMAND));
                }
                for (String cmd : cCommands) {
                    ss.addConsoleCommand(new SignCommand(cmd, PlayerInput.CONSOLE_COMMAND));
                }
                for (String perm : permissions) {
                    ss.addPermission(perm);
                }
                SudoSigns.signs.put(key, ss);
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getLogger().warning("[SUDOSIGNS] Failed to initialise SudoSign " + name + "!");
            }
        }
    }

    private void save() {
        try {
            signConfig.save(signConfigFile);
        } catch (IOException e) {
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to save to signs.yml!");
            e.printStackTrace();
        }

    }


    public void addPermissionToConfig(SudoSign s, String perm) {
        if (!signConfig.isConfigurationSection("signs." + s.getName() + ".permissions")) {
            signConfig.set("signs." + s.getName() + ".permissions", new ArrayList<>());
        }
        List<String> perms = signConfig.getStringList("signs." + s.getName() + ".permissions");
        perms.add(perm);
        signConfig.set("signs." + s.getName() + ".permissions", perms);
        save();
    }

    public void deletePermissionFromConfig(SudoSign s, String perm) {
        if (!signConfig.isConfigurationSection("signs." + s.getName() + ".permissions")) {
            signConfig.set("signs." + s.getName() + ".permissions", new ArrayList<>());
        }
        List<String> perms = signConfig.getStringList("signs." + s.getName() + ".permissions");
        perms.remove(perm);
        signConfig.set("signs." + s.getName() + ".permissions", perms);
        save();
    }

    public void addCommandToConfig(SudoSign s, SignCommand cmd, PlayerInput type) {
        if (signConfig.isConfigurationSection("signs." + s.getName())) {
            if (type.equals(PlayerInput.PLAYER_COMMAND)) {
                if (!signConfig.isConfigurationSection("signs." + s.getName() + ".player-commands")) {
                    signConfig.set("signs." + s.getName() + ".player-commands", new ArrayList<>());
                }
                List<String> cmds = signConfig.getStringList("signs." + s.getName() + ".player-commands");
                cmds.add(cmd.getCommand());
                signConfig.set("signs." + s.getName() + ".player-commands", cmds);
            } else if (type.equals(PlayerInput.CONSOLE_COMMAND)) {
                if (!signConfig.isConfigurationSection("signs." + s.getName() + ".console-commands")) {
                    signConfig.set("signs." + s.getName() + ".console-commands", new ArrayList<>());
                }
                List<String> cmds = signConfig.getStringList("signs." + s.getName() + ".console-commands");
                cmds.add(cmd.getCommand());
                signConfig.set("signs." + s.getName() + ".console-commands", cmds);

            }
        }
        save();
    }

    public void deleteCommandFromConfig(SudoSign s, SignCommand cmd, PlayerInput type) {
        if (signConfig.isConfigurationSection("signs." + s.getName())) {
            Bukkit.getLogger().warning("HERE");
            if (type.equals(PlayerInput.PLAYER_COMMAND)) {
                Bukkit.getLogger().warning("HERE1");
                if (signConfig.isList("signs." + s.getName() + ".player-commands")) {
                    Bukkit.getLogger().warning("HERE2");
                    List<String> cmds = signConfig.getStringList("signs." + s.getName() + ".player-commands");
                    Bukkit.getLogger().warning("BEFORE: " + cmds.toString());
                    cmds.remove(cmd.getCommand());
                    Bukkit.getLogger().warning("AFTER: " + cmds.toString());
                    signConfig.set("signs." + s.getName() + ".player-commands", cmds);
                }
            } else if (type.equals(PlayerInput.CONSOLE_COMMAND)) {
                if (signConfig.isList("signs." + s.getName() + ".console-commands")) {
                    List<String> cmds = signConfig.getStringList("signs." + s.getName() + ".console-commands");
                    cmds.remove(cmd.getCommand());
                    signConfig.set("signs." + s.getName() + ".console-commands", cmds);
                }

            }
        }
        save();
    }

    public void deleteSign(String name) {
        if (signConfig.isConfigurationSection("signs." + name)) {
            signConfig.set("signs." + name, null);
        }
        save();
    }

    public void saveToFile(SudoSign s, Boolean singular, Player p) {
        if (singular) {
            saveSign(s, p);
        } else {
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                saveSign(entry.getValue(), p);
            }
        }
        save();
    }

    public void saveSign(SudoSign s, Player p) {
        String name = s.getName();
        try {
            if (!signConfig.isConfigurationSection("signs." + name)) {
                signConfig.createSection("signs." + name);
                ConfigurationSection locSec = signConfig.createSection("signs." + name + ".location");
                String world = s.getSign().getWorld().getName();
                Double x = s.getSign().getLocation().getX();
                Double y = s.getSign().getLocation().getY();
                Double z = s.getSign().getLocation().getZ();
                locSec.set("world", world);
                locSec.set("x", x);
                locSec.set("y", y);
                locSec.set("z", z);
                signConfig.createSection("signs." + name + ".permissions");
                signConfig.createSection("signs." + name + ".player-commands");
                signConfig.createSection("signs." + name + ".console-commands");
            }
        } catch (Exception e) {
            if (p != null) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Failed to save sign %NAME% to the config!", name);
            }
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to save " + name + " to signs.yml!");
            e.printStackTrace();
        }
    }

}
