package dev.mylesmor.sudosigns.commands;

import dev.mylesmor.sudosigns.data.PlayerInput;

/**
 * The SignCommand class for storing sign command data.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class SignCommand {

    private String command;
    private PlayerInput type;

    public SignCommand(String cmd, PlayerInput type) {
        command = cmd;
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public PlayerInput getType() {
        return type;
    }


}
