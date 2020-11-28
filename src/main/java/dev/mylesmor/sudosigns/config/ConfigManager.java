package dev.mylesmor.sudosigns.config;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.data.SignCommand;
import dev.mylesmor.sudosigns.data.SignMessage;
import dev.mylesmor.sudosigns.data.SudoSign;
import org.bukkit.Bukkit;
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
    private SignConfig signConfigManager;
    private MessageConfig messageConfig;
    private CommandConfig commandConfig;
    private PermissionConfig permissionConfig;


    public ConfigManager() {
        createCustomConfig();
        loadCustomConfig();
        loadModules();
    }

    public void loadModules() {
        invalidEntriesManager = new InvalidEntriesManager(this, signConfig);
        signConfigManager = new SignConfig(this);
        signConfigManager.loadSigns();
        messageConfig = new MessageConfig(this);
        commandConfig = new CommandConfig(this);
        permissionConfig = new PermissionConfig(this);
    }

    public InvalidEntriesManager getInvalidEntriesManager() {
        return invalidEntriesManager;
    }

    public FileConfiguration getSignConfig() { return signConfig; }

    public ArrayList<String> loadSigns() {
        return signConfigManager.loadSigns();
    }

    public void saveSign(SudoSign s, boolean singular, Player p) {
        signConfigManager.saveToFile(s, singular, p);
    }

    public void deleteSign(String name) {
        signConfigManager.deleteSign(name);
    }

    public void editSignText(String name, int lineNumber, String message) {
        signConfigManager.editText(name, lineNumber, message);
    }

    public void addMessage(SudoSign s, SignMessage sm) {
        messageConfig.addMessageToConfig(s, sm);
    }

    public void deleteMessage(SudoSign s, SignMessage sm, double delay) {
        messageConfig.deleteMessageFromConfig(s, sm, delay);
    }

    public void addCommand(SudoSign s, SignCommand sm, PlayerInput type) {
        commandConfig.addCommandToConfig(s, sm, type);
    }

    public void deleteCommand(SudoSign s, SignCommand sm, PlayerInput type, double oldDelay) {
        commandConfig.deleteCommandFromConfig(s, sm, type, oldDelay);
    }

    public void setPrice(String name, double price) {
        signConfigManager.setPrice(name, price);
    }

    public void addPermission(SudoSign s, String permission) {
        permissionConfig.addPermissionToConfig(s, permission);
    }

    public void deletePermission(SudoSign s, String permission) {
        permissionConfig.deletePermissionFromConfig(s, permission);
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
            if (!signConfig.contains("version")) {
                signConfig.createSection("version");
                signConfig.set("version", "1.1.0");
                save();
                fixConfig();
            }
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to initialise signs.yml!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void fixConfig() {
        Set<String> signSection = signConfig.getConfigurationSection("signs").getKeys(false);
        String name = null;
        for (String key : signSection) {
            signConfig.set("signs." + key + ".op-commands", null);
            signConfig.set("signs." + key + ".price", 0.0);
            name = key;
            List<String> pCommands = signConfig.getStringList("signs." + key + ".player-commands");
            List<String> cCommands = signConfig.getStringList("signs." + key + ".console-commands");
            List<String> messages = signConfig.getStringList("signs." + key + ".messages");
            if (pCommands.size() == 0 && cCommands.size() == 0 && messages.size() == 0) {
                save();
                continue;
            }
            if (pCommands.size() != 0) {
                try {
                    ArrayList<HashMap<String, Double>> mapList = new ArrayList<>();
                    HashMap<String, Double> map = new HashMap<>();
                    for (String cmd : pCommands) {
                        map.put(cmd, 0.0);
                        mapList.add(map);
                        map = new HashMap<>();
                    }
                    signConfig.set("signs." + name + ".player-commands", mapList);
                } catch (Exception ignored) {
                }
            }
            signConfig.set("signs." + key + ".op-commands", null);
            if (cCommands.size() != 0) {
                try {
                    ArrayList<HashMap<String, Double>> mapList = new ArrayList<>();
                    HashMap<String, Double> map = new HashMap<>();
                    for (String cmd : cCommands) {
                        map.put(cmd, 0.0);
                        mapList.add(map);
                        map = new HashMap<>();
                    }
                    signConfig.set("signs." + name + ".console-commands", mapList);
                } catch (Exception ignored) {
                }
            }
            if (messages.size() != 0) {
                try {
                    ArrayList<HashMap<String, Double>> mapList = new ArrayList<>();
                    HashMap<String, Double> map = new HashMap<>();
                    for (String cmd : messages) {
                        map.put(cmd, 0.0);
                        mapList.add(map);
                        map = new HashMap<>();
                    }
                    signConfig.set("signs." + name + ".messages", mapList);
                } catch (Exception ignored) {
                }
            }
            save();
        }
    }

    public void save() {
        try {
            signConfig.save(signConfigFile);
        } catch (IOException e) {
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to save to signs.yml!");
            e.printStackTrace();
        }
    }


}
