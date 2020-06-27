package dev.mylesmor.sudosigns.config;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.data.SignCommand;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The class for managing the plugins config.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class ConfigManager {

    private FileConfiguration signConfig;
    private File signConfigFile;
    private InvalidEntriesManager invalidEntriesManager;

    public ConfigManager() {
        createCustomConfig();
        loadCustomConfig();
        invalidEntriesManager = new InvalidEntriesManager(this, signConfig);
        loadSigns();
    }

    public InvalidEntriesManager getInvalidEntriesManager() {
        return invalidEntriesManager;
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

    public boolean loadCustomConfig() {
        signConfig = new YamlConfiguration();
        try {
            signConfig.load(signConfigFile);
            if (!signConfig.isConfigurationSection("signs")) {
                signConfig.createSection("signs");
                save();
            }
            if (invalidEntriesManager != null) {
                invalidEntriesManager.setConfig(signConfig);
            }
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to initialise signs.yml!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Loads the signs from the config.
     * @return True if successful, false if not
     */
    public ArrayList<String> loadSigns() {
        Map<String, SudoSign> tempSigns = new HashMap<>();
        Set<String> signSection = signConfig.getConfigurationSection("signs").getKeys(false);
        String name;
        ArrayList<String> invalidSigns = new ArrayList<>();
        for (String key : signSection) {
            name = key;
            try {
                ConfigurationSection locSec = signConfig.getConfigurationSection("signs." + key + ".location");
                String world = locSec.getString("world");
                double x = locSec.getDouble("x");
                double y = locSec.getDouble("y");
                double z = locSec.getDouble("z");
                World w = Bukkit.getServer().getWorld(world);
                if (w != null) {
                    Location loc = new Location(Bukkit.getServer().getWorld(world), x, y, z);
                    if (loc.getBlock().getState() instanceof Sign) {
                        Sign sign = (Sign) loc.getBlock().getState();
                        SudoSign ss = new SudoSign(key);
                        ss.setSign(sign);
                        List<String> pCommands = signConfig.getStringList("signs." + key + ".player-commands");
                        List<String> opCommands = signConfig.getStringList("signs." + key + ".op-commands");
                        List<String> cCommands = signConfig.getStringList("signs." + key + ".console-commands");
                        List<String> permissions = signConfig.getStringList("signs." + key + ".permissions");
                        List<String> messages = signConfig.getStringList("signs." + key + ".messages");
                        for (String cmd : pCommands) {
                            ss.addPlayerCommand(new SignCommand(cmd, PlayerInput.PLAYER_COMMAND), false);
                        }
                        for (String cmd : opCommands) {
                            ss.addPlayerCommand(new SignCommand(cmd, PlayerInput.PLAYER_COMMAND), true);
                        }
                        for (String cmd : cCommands) {
                            ss.addConsoleCommand(new SignCommand(cmd, PlayerInput.CONSOLE_COMMAND));
                        }
                        for (String perm : permissions) {
                            ss.addPermission(perm);
                        }
                        for (String message : messages) {
                            ss.addMessage(message);
                        }
                        tempSigns.put(name, ss);
                    } else {
                        invalidSigns.add(key);
                        Bukkit.getLogger().warning("[SUDOSIGNS] Failed to load Sign " + key + "! The block at the provided location is not a sign. Skipping...");
                    }
                } else {
                    invalidSigns.add(key);
                    Bukkit.getLogger().warning("[SUDOSIGNS] Failed to load Sign " + key + "! The world name for this sign is invalid. Skipping...");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        invalidEntriesManager.setInvalidEntries(invalidSigns);
        SudoSigns.signs = tempSigns;
        return invalidSigns;
    }

    void save() {
        try {
            signConfig.save(signConfigFile);
        } catch (IOException e) {
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to save to signs.yml!");
            e.printStackTrace();
        }

    }


    public void addPermissionToConfig(SudoSign s, String perm) {
        String path = "signs." + s.getName() + ".permissions";
        List<String> perms = signConfig.getStringList(path);
        perms.add(perm);
        signConfig.set(path, perms);
        save();
    }

    public void deletePermissionFromConfig(SudoSign s, String perm) {
        String path = "signs." + s.getName() + ".permissions";
        List<String> perms = signConfig.getStringList(path);
        perms.remove(perm);
        signConfig.set(path, perms);
        save();
    }

    public void addMessageToConfig(SudoSign s, String message) {
        String path = "signs." + s.getName() + ".messages";
        List<String> messages = signConfig.getStringList(path);
        messages.add(message);
        signConfig.set(path, messages);
        save();
    }

    public void deleteMessageFromConfig(SudoSign s, String message) {
        String path = "signs." + s.getName() + ".messages";
        List<String> messages = signConfig.getStringList(path);
        messages.remove(message);
        signConfig.set(path, messages);
        save();
    }

    public void addCommandToConfig(SudoSign s, SignCommand cmd, PlayerInput type) {
        if (signConfig.isConfigurationSection("signs." + s.getName())) {
            switch (type) {
                case PLAYER_COMMAND:
                    List<String> pCmds = signConfig.getStringList("signs." + s.getName() + ".player-commands");
                    pCmds.add(cmd.getCommand());
                    signConfig.set("signs." + s.getName() + ".player-commands", pCmds);
                    break;
                case PLAYER_COMMAND_WITH_PERMISSIONS:
                    List<String> opCmds = signConfig.getStringList("signs." + s.getName() + ".op-commands");
                    opCmds.add(cmd.getCommand());
                    signConfig.set("signs." + s.getName() + ".op-commands", opCmds);
                    break;
                case CONSOLE_COMMAND:
                    List<String> cCmds = signConfig.getStringList("signs." + s.getName() + ".console-commands");
                    cCmds.add(cmd.getCommand());
                    signConfig.set("signs." + s.getName() + ".console-commands", cCmds);
                    break;
            }
        }
        save();
    }

    public void deleteCommandFromConfig(SudoSign s, SignCommand cmd, PlayerInput type) {
        if (signConfig.isConfigurationSection("signs." + s.getName())) {
            if (type.equals(PlayerInput.PLAYER_COMMAND)) {
                if (signConfig.isList("signs." + s.getName() + ".player-commands")) {
                    List<String> cmds = signConfig.getStringList("signs." + s.getName() + ".player-commands");
                    cmds.remove(cmd.getCommand());
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
        if (signConfig.isConfigurationSection("signs." + name + "")) {
            signConfig.set("signs." + name + "", null);
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
            if (!signConfig.isConfigurationSection("signs." + name + "")) {
                signConfig.createSection("signs." + name + "");
                signConfig.createSection("signs." + name + ".text");
                ArrayList<String> lines = new ArrayList<>();
                lines.addAll(s.getText());
                signConfig.set("signs." + name + ".text", lines);
                ConfigurationSection locSec = signConfig.createSection("signs." + name + ".location");
                String world = s.getSign().getWorld().getName();
                double x = s.getSign().getLocation().getX();
                double y = s.getSign().getLocation().getY();
                double z = s.getSign().getLocation().getZ();
                signConfig.set("signs." + name + ".blocktype", s.getSign().getType().toString());
                locSec.set("world", world);
                locSec.set("x", x);
                locSec.set("y", y);
                locSec.set("z", z);
                if (s.getSign().getWorld().getBlockAt(s.getSign().getLocation()).getBlockData() instanceof org.bukkit.block.data.type.Sign) {
                    org.bukkit.block.data.type.Sign facing = (org.bukkit.block.data.type.Sign) s.getSign().getWorld().getBlockAt(s.getSign().getLocation()).getBlockData();
                    locSec.set("rotation", facing.getRotation().toString());
                } else if (s.getSign().getWorld().getBlockAt(s.getSign().getLocation()).getBlockData() instanceof org.bukkit.block.data.type.WallSign) {
                    org.bukkit.block.data.type.WallSign facing = (org.bukkit.block.data.type.WallSign) s.getSign().getWorld().getBlockAt(s.getSign().getLocation()).getBlockData();
                    locSec.set("rotation", facing.getFacing().toString());
                }
                signConfig.createSection("signs." + name + ".permissions");
                signConfig.createSection("signs." + name + ".messages");
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
