package dev.mylesmor.sudosigns;

/**
 * The SignCommand class for storing sign command data.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class SignCommand {

    private String command;
    private PlayerInput type;

    SignCommand(String cmd, PlayerInput type) {
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
