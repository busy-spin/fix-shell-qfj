package io.github.busy_spin.fix_showcase.qfj.utils;

public class LogElement {
    private String message;
    private String type;


    public LogElement(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }
}
