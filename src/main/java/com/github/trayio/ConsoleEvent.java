package com.github.trayio;

public class ConsoleEvent extends BaseEvent {
    private String path;

    public ConsoleEvent(String path) {
        this.path = path;
    }

    @Override
    public String getContent() {
        return path;
    }
}
