package dev.mylesmor.sudosigns;

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
import java.util.List;
import java.util.Map;
import java.util.Set;


import static dev.mylesmor.sudosigns.SudoSigns.*;

public class ConfigManager {

    private FileConfiguration signConfig;
    private File signConfigFile;

    ConfigManager() {
        createCustomConfig();
        loadCustomConfig();
        loadSigns();
    }

    private void createCustomConfig() {
        if (!sudoSignsPlugin.getDataFolder().exists()) {
            sudoSignsPlugin.getDataFolder().mkdir();
        }
        signConfigFile = new File(sudoSignsPlugin.getDataFolder(), "signs.yml");
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
        signs.clear();
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
                for (String cmd : pCommands) {
                    ss.addPlayerCommand(new SignCommand(cmd, PlayerInput.PLAYER_COMMAND));
                }
                for (String cmd : cCommands) {
                    ss.addPlayerCommand(new SignCommand(cmd, PlayerInput.CONSOLE_COMMAND));
                }
                signs.put(key, ss);
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

    public void addCommandToConfig(SudoSign s, SignCommand cmd, PlayerInput type) {
        if (signConfig.isConfigurationSection("signs." + s.getName())) {
            if (type.equals(PlayerInput.PLAYER_COMMAND)) {
                if (!signConfig.isConfigurationSection("signs." + s.getName() + ".player-commands")) {
                    signConfig.createSection("signs." + s.getName() + ".player-commands");
                }
                List<String> cmds = signConfig.getStringList("signs." + s.getName() + ".player-commands");
                cmds.add(cmd.getCommand());
                signConfig.set("signs." + s.getName() + ".player-commands", cmds);
            } else if (type.equals(PlayerInput.CONSOLE_COMMAND)) {
                if (!signConfig.isConfigurationSection("signs." + s.getName() + ".console-commands")) {
                    signConfig.createSection("signs." + s.getName() + ".console-commands");
                }
                List<String> cmds = signConfig.getStringList("signs." + s.getName() + ".console-commands");
                cmds.add(cmd.getCommand());
                signConfig.set("signs." + s.getName() + ".console-commands", cmds);

            }
        }
        save();
    }

    public void saveToFile(SudoSign s, Player p) {
        String name = null;
        for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
            try {
                SudoSign ss = entry.getValue();
                name = entry.getKey();
                if (!signConfig.isConfigurationSection("signs." + name)) {
                    signConfig.createSection("signs." + name);
                    ConfigurationSection locSec = signConfig.createSection("signs." + name + ".location");
                    String world = ss.getSign().getWorld().getName();
                    Double x = ss.getSign().getLocation().getX();
                    Double y = ss.getSign().getLocation().getY();
                    Double z = ss.getSign().getLocation().getZ();
                    locSec.set("world", world);
                    locSec.set("x", x);
                    locSec.set("y", y);
                    locSec.set("z", z);
                    signConfig.createSection("signs." + s.getName() + ".player-commands");
                    signConfig.createSection("signs." + s.getName() + ".console-commands");

                }
            } catch (Exception e) {
                if (p != null) {
                    p.sendMessage(prefix + ChatColor.RED + " Failed to save sign " + ChatColor.GOLD + name + ChatColor.RED + " to the config!");
                }
                Bukkit.getLogger().warning("[SUDOSIGNS] Failed to save " + name + " to signs.yml!");
                e.printStackTrace();
            }
        }
        save();
    }

}
