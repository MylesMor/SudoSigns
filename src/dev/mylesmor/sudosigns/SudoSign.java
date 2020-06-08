package dev.mylesmor.sudosigns;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class SudoSign {

    private ArrayList<String> commands = new ArrayList<>();
    private org.bukkit.block.Sign sign;
    private String name;

    SudoSign(String name) {
        this.name = name;
    }

    public void createSign(org.bukkit.block.Sign sign) {
        this.sign = sign;
    }

    public void addCommand(String e) {
        commands.add(e);
    }

    public String getName() {
        return name;
    }

    public org.bukkit.block.Sign getSign() {
        return sign;
    }

}
