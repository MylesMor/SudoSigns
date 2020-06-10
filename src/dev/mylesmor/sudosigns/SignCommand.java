package dev.mylesmor.sudosigns;

import org.bukkit.entity.Player;

public class SignCommand {

    private String command;
    private int repeat;
    private PlayerInput type;

    SignCommand(String cmd, int r, PlayerInput type) {
        repeat = r;
        command = cmd;
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public int getRepeat() {
        return repeat;
    }

    public void executeCommand(Player p) {

    }

}
