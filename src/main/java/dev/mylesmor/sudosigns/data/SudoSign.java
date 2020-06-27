package dev.mylesmor.sudosigns.data;

import dev.mylesmor.sudosigns.SudoSigns;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class assigned to each created sign.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class SudoSign {

    private HashMap<SignCommand, Boolean> playerCommands = new HashMap<>();
    private ArrayList<SignCommand> consoleCommands = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private ArrayList<String> messages = new ArrayList<>();
    private ArrayList<String> text = new ArrayList<>();
    private String worldName;
    private double x;
    private double y;
    private double z;
    private String name;

    public SudoSign(String name) {
        this.name = name;
    }

    public void setSign(Sign sign) {
        Location loc = sign.getLocation();
        this.worldName = loc.getWorld().getName();
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addPlayerCommand(SignCommand sc, boolean permissions) {
        playerCommands.put(sc, permissions);

    }

    public List<String> getText() {
        return text;
    }

    public void addLines() {
        Sign sign = getSign();
        String[] lines = sign.getLines();
        for (int i = 0; i < 4; i++) {
            String line = lines[i];
            text.add(line.replaceAll("§", "&"));
            line = ChatColor.translateAlternateColorCodes('&', line);
            //line = ChatColor.translateAlternateColorCodes('§', line);
            sign.setLine(i, line);
        }
        sign.update();
    }

    public void addConsoleCommand(SignCommand sc) {
        consoleCommands.add(sc);
    }

    public void deleteConsoleCommand(SignCommand sc) {
        consoleCommands.remove(sc);
    }

    public void deletePlayerCommand(SignCommand sc) {
        playerCommands.remove(sc);
    }

    public void addPermission(String s) {
        permissions.add(s);
    }

    public void removePermission(String s) {
        permissions.remove(s);
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void addMessage(String s) {
        messages.add(s);
    }

    public void removeMessage(String s) {
        messages.remove(s);
    }

    public void copyFrom(SudoSign s) {
        this.permissions = s.getPermissions();
        this.playerCommands = s.getPlayerCommands();
        this.consoleCommands = s.getConsoleCommands();
        this.messages = s.getMessages();
    }

    public ArrayList<String> getMessages() { return messages; }

    public void setMessages(ArrayList<String> messages) { this.messages = messages; }


    public void setPlayerCommands(HashMap<SignCommand, Boolean> playerCommands) {
        this.playerCommands = playerCommands;
    }

    public void setConsoleCommands(ArrayList<SignCommand> consoleCommands) {
        this.consoleCommands = consoleCommands;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public HashMap<SignCommand, Boolean> getPlayerCommands() {
        return playerCommands;
    }

    public ArrayList<SignCommand> getConsoleCommands() {
        return consoleCommands;
    }

    /**
     * Executes all of the commands attached to the sign, if the player has the required permissions.
     * @param p The player who is running the sign.
     */
    public void executeCommands(Player p) {
        boolean hasPermission = true;
        for (String perm : permissions) {
            if (!p.hasPermission(perm)) {
                hasPermission = false;
            }
        }
        if (hasPermission) {
            for (String s : messages) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', s).replaceAll("(?i)%PLAYER%", p.getName()));
            }
            for (Map.Entry<SignCommand, Boolean> entry : playerCommands.entrySet()) {
                String cmd = entry.getKey().getCommand().replaceAll("(?i)%PLAYER%", p.getName());
                if (entry.getValue()) {
                    try {
                        p.setOp(true);
                        p.performCommand(cmd);
                        p.setOp(false);
                    } catch (Exception ignored) { }
                    finally {
                        p.setOp(false);
                    }
                } else {
                    p.performCommand(cmd);
                }
            }
            for (SignCommand sc : consoleCommands) {
                String cmd = sc.getCommand().replaceAll("(?i)%PLAYER%", p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        } else {
            p.sendMessage(ChatColor.RED + "You don't have permission to do this!");
        }
    }

    public String getName() {
        return name;
    }

    public Sign getSign() {
        if (worldName != null) {
            Location loc = new Location(Bukkit.getWorld(worldName), x, y, z);
            if (loc.getBlock().getState() instanceof Sign) {
                return (Sign) loc.getBlock().getState();
            }
        }
        Bukkit.getLogger().warning("Failed to locate sign " + name + "!");
        return null;
    }
}
