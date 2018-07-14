package com.github.trayio;

import java.util.Objects;

public class Task {
    public int step;
    public String name;

    public Task(int step, String name) {
        this.step = step;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return step == task.step &&
                Objects.equals(name, task.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(step, name);
    }
}
