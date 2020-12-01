package dev.mylesmor.sudosigns.config;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.data.SignCommand;
import dev.mylesmor.sudosigns.data.SignMessage;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class SignConfig {

    private FileConfiguration signConfig;
    private ConfigManager configManager;

    SignConfig(ConfigManager configManager) {
        this.configManager = configManager;
        this.signConfig = configManager.getSignConfig();
    }

    public void setSignConfig(FileConfiguration signConfig) {
        this.signConfig = signConfig;
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
                        List<Map<?, ?>> pCommands = signConfig.getMapList("signs." + key + ".player-commands");
                        List<Map<?, ?>> cCommands = signConfig.getMapList("signs." + key + ".console-commands");
                        List<String> permissions = signConfig.getStringList("signs." + key + ".permissions");
                        List<Map<?, ?>> messages = signConfig.getMapList("signs." + key + ".messages");
                        try {
                            ss.setPrice((Double) signConfig.get("signs." + key + ".price"));
                        } catch (Exception e) {
                            Bukkit.getLogger().info("Invalid price for sign " + name + "! Default to 0.");
                            ss.setPrice(0.0);
                        }
                        int number = 0;
                        for (Map<?, ?> sc : pCommands) {
                            for (Map.Entry<?, ?> cmd : sc.entrySet()) {
                                ss.addPlayerCommand(new SignCommand(number++, (String) cmd.getKey(), (Double) cmd.getValue(), PlayerInput.PLAYER_COMMAND));
                            }
                        }
                        for (Map<?, ?> sc : cCommands) {
                            for (Map.Entry<?, ?> cmd : sc.entrySet()) {
                                ss.addConsoleCommand(new SignCommand(number++, (String) cmd.getKey(), (Double) cmd.getValue(), PlayerInput.CONSOLE_COMMAND));
                            }
                        }
                        for (String perm : permissions) {
                            ss.addPermission(perm);
                        }
                        number = 0;
                        for (Map<?, ?> sc : messages) {
                            for (Map.Entry<?, ?> message : sc.entrySet()) {
                                ss.addMessage(new SignMessage(number++, (String) message.getKey(), (Double) message.getValue(), PlayerInput.MESSAGE));
                            }
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
        configManager.getInvalidEntriesManager().setInvalidEntries(invalidSigns);
        SudoSigns.signs = tempSigns;
        SudoSigns.invalidSigns = invalidSigns;
        return invalidSigns;
    }

    /**
     * Deletes a sign from the config.
     * @param name The name of the sign.
     */
    public void deleteSign(String name) {
        if (signConfig.isSet("signs." + name)) {
            signConfig.set("signs." + name, null);
        }
        configManager.save();
    }

    /**
     * Saves the SudoSigns to config.
     * @param s The SudoSign object.
     * @param singular True to save a singular sign, False to save all.
     * @param p The player who has edited the sign.
     */
    public void saveToFile(SudoSign s, boolean singular, Player p) {
        if (singular) {
            saveSign(s, p);
        } else {
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                saveSign(entry.getValue(), p);
            }
        }
        configManager.save();
    }

    /**
     * Saves a SudoSign in the config.
     * @param s The SudoSign object.
     * @param p The player who has edited the sign.
     */
    public void saveSign(SudoSign s, Player p) {
        String name = s.getName();
        try {
            if (!signConfig.isConfigurationSection("signs." + name + "")) {
                signConfig.createSection("signs." + name + "");
                signConfig.createSection("signs." + name + ".text");
                ArrayList<String> lines = new ArrayList<>();
                lines.addAll(s.getText());
                signConfig.set("signs." + name + ".text", lines);
                signConfig.set("signs." + name + ".price", 0.0);
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
                configManager.save();
            }
        } catch (Exception e) {
            if (p != null) {
                Util.sudoSignsMessage(p, ChatColor.RED, "Failed to save sign %NAME% to the config!", name);
            }
            Bukkit.getLogger().warning("[SUDOSIGNS] Failed to save " + name + " to signs.yml!");
            e.printStackTrace();
        }

    }

    public void editText(String name, int lineNumber, String newText) {
        if (signConfig.isConfigurationSection("signs." + name + "")) {
            List<String> text = signConfig.getStringList("signs." + name + ".text");
            Bukkit.getLogger().info(Integer.toString(text.size()));
            text.set(lineNumber-1, newText);
            signConfig.set("signs." + name + ".text", text);
        }
        configManager.save();
    }

    public void setPrice(String name, double price) {
        if (signConfig.isConfigurationSection("signs." + name + "")) {
            signConfig.set("signs." + name + ".price", price);
        }
        configManager.save();
    }
}
