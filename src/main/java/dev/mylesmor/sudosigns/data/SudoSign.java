package dev.mylesmor.sudosigns.data;

import dev.mylesmor.sudosigns.commands.SignCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * The class assigned to each created sign.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class SudoSign {

    private ArrayList<SignCommand> playerCommands = new ArrayList<>();
    private ArrayList<SignCommand> consoleCommands = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
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

    public void addPlayerCommand(SignCommand sc) {
        playerCommands.add(sc);

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

    public void setPlayerCommands(ArrayList<SignCommand> playerCommands) {
        this.playerCommands = playerCommands;
    }

    public void setConsoleCommands(ArrayList<SignCommand> consoleCommands) {
        this.consoleCommands = consoleCommands;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public ArrayList<SignCommand> getPlayerCommands() {
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
            for (SignCommand sc : playerCommands) {
                String cmd = sc.getCommand().replaceAll("(?i)%PLAYER%", p.getName());
                p.performCommand(cmd);
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
        Location loc = new Location(Bukkit.getWorld(worldName), x, y, z);
        if (loc.getBlock().getState() instanceof Sign) {
            return (Sign) loc.getBlock().getState();
        }
        Bukkit.getLogger().warning("Failed to locate sign " + name + "!");
        return null;
    }
}
