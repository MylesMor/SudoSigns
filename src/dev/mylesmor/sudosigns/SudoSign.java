package dev.mylesmor.sudosigns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SudoSign {

    private ArrayList<SignCommand> playerCommands = new ArrayList<>();
    private ArrayList<SignCommand> consoleCommands = new ArrayList<>();
    private org.bukkit.block.Sign sign;
    private String name;

    SudoSign(String name) {
        this.name = name;
    }

    public void createSign(org.bukkit.block.Sign sign) {
        this.sign = sign;
    }

    public void addPlayerCommand(SignCommand e) {
        playerCommands.add(e);
    }

    public void addConsoleCommand(SignCommand e) {
        consoleCommands.add(e);
    }


    public ArrayList<SignCommand> getPlayerCommands() {
        return playerCommands;
    }

    public ArrayList<SignCommand> getConsoleCommands() {
        return consoleCommands;
    }

    public void executeCommands(Player p) {
        for (SignCommand sc : playerCommands) {
            p.performCommand(sc.getCommand());
        }
        for (SignCommand sc : consoleCommands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), sc.getCommand());
        }
    }

    public String getName() {
        return name;
    }

    public org.bukkit.block.Sign getSign() {
        return sign;
    }

}
