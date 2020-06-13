package dev.mylesmor.sudosigns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SudoSign {

    private ArrayList<SignCommand> playerCommands = new ArrayList<>();
    private ArrayList<SignCommand> consoleCommands = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private org.bukkit.block.Sign sign;
    private String name;

    SudoSign(String name) {
        this.name = name;
    }

    public void setSign(org.bukkit.block.Sign sign) {
        this.sign = sign;
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

    public void executeCommands(Player p) {
        for (SignCommand sc : playerCommands) {
            String cmd = sc.getCommand().replaceAll("(?i)%PLAYER%", p.getName());
            p.performCommand(cmd);
        }
        for (SignCommand sc : consoleCommands) {
            String cmd = sc.getCommand().replaceAll("(?i)%PLAYER%", p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public String getName() {
        return name;
    }

    public org.bukkit.block.Sign getSign() {
        return sign;
    }

}
