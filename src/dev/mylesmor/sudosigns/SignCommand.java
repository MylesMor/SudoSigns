package dev.mylesmor.sudosigns;

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

}
