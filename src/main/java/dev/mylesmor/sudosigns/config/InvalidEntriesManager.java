package dev.mylesmor.sudosigns.config;

import dev.mylesmor.sudosigns.SudoSigns;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class InvalidEntriesManager {

    ArrayList<String> invalidEntries = new ArrayList<>();
    ConfigManager configManager;
    FileConfiguration config;

    InvalidEntriesManager(ConfigManager configManager, FileConfiguration config) {
        this.configManager = configManager;
        this.config = config;
    }

    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    private boolean fixEntry(String s) {
        try {
            ConfigurationSection locSec = config.getConfigurationSection("signs." + s + ".location");
            String world = locSec.getString("world");
            double x = locSec.getDouble("x");
            double y = locSec.getDouble("y");
            double z = locSec.getDouble("z");
            String matString = config.getString("signs." + s + ".blocktype");
            if (SudoSigns.version.contains("1.13")) {
                matString = "SIGN";
            }
            Material material = Material.valueOf(matString);
            BlockFace blockFace = BlockFace.valueOf(locSec.getString("rotation"));
            World w = Bukkit.getServer().getWorld(world);
            if (w != null) {
                Location loc = new Location(Bukkit.getServer().getWorld(world), x, y, z);
                w.getBlockAt(loc).setType(material);
                Block b = w.getBlockAt(loc);
                if (b.getBlockData() instanceof org.bukkit.block.data.type.Sign) {
                    org.bukkit.block.data.type.Sign facing = (org.bukkit.block.data.type.Sign) b.getBlockData();
                    facing.setRotation(blockFace);
                    b.setBlockData(facing);
                } else if (b.getBlockData() instanceof org.bukkit.block.data.type.WallSign){
                    org.bukkit.block.data.type.WallSign facing = (org.bukkit.block.data.type.WallSign) b.getBlockData();
                    facing.setFacing(blockFace);
                    b.setBlockData(facing);
                }
                Sign newSign = (Sign) b.getState();
                Bukkit.getLogger().warning(w.getBlockAt(loc).getBlockData().toString());
                List<String> lines = getSignText(s);
                int i = 0;
                for (String line : lines) {
                    newSign.setLine(i, ChatColor.translateAlternateColorCodes('&', line));
                    i++;
                }
                newSign.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<String> getInvalidEntries() {
        return invalidEntries;
    }

    public void setInvalidEntries(ArrayList<String> invalid) {
        invalidEntries = invalid;
    }

    public void addInvalidEntry(String entry) {
        invalidEntries.add(entry);
    }


    public List<String> getSignText(String name) {
        List<String> signSec = config.getStringList("signs." + name + ".text");
        for (String item : signSec) {
            item = ChatColor.translateAlternateColorCodes('&', item);
        }
        if (signSec.size() != 0) {
            return signSec;
        }
        return null;
    }


    public boolean purgeInvalidEntry(String name, boolean all) {
        if (all) {
            for (String s : invalidEntries) {
                Bukkit.getLogger().warning(Boolean.toString(config.isConfigurationSection("signs." + s)));
                config.set("signs." + s, null);
            }
            configManager.loadSigns();
            configManager.save();
            return true;
        } else {
            for (String s : invalidEntries) {
                if (name.equalsIgnoreCase(s)) {
                    Bukkit.getLogger().warning(Boolean.toString(config.isConfigurationSection("signs." + s)));
                    config.set("signs." + name, null);
                    configManager.loadSigns();
                    configManager.save();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean fixInvalidEntry(String name, boolean all) {
        if (all) {
            for (String s : invalidEntries) {
                fixEntry(s);
            }
            configManager.save();
            return true;
        } else {
            for (String s : invalidEntries) {
                if (name.equals(s)) {
                    if (fixEntry(s)) {
                        configManager.save();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
