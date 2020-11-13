package dev.mylesmor.sudosigns.data;

public class SignMessage {

    private String message;
    private PlayerInput type;
    private int number;
    private double delay;

    public SignMessage(int number, String message, double delay, PlayerInput type) {
        this.number = number;
        this.message = message;
        this.type = type;
        this.delay = delay;
    }

    public String getMessage() {
        return message;
    }

    public PlayerInput getType() {
        return type;
    }

    public int getNumber() { return number; }

    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }
}
