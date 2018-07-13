package com.github.trayio;

import java.util.Objects;

public class ConsoleEvent extends BaseEvent {
    private String path;

    public ConsoleEvent(String path) {
        this.path = path;
    }

    @Override
    public String getContent() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsoleEvent that = (ConsoleEvent) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
