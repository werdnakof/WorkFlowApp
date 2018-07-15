package com.github.trayio;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class WorkFlow {

    private int id;
    private String name;
    private List<Task> tasks;

    public WorkFlow(int id, List<Task> tasks) {
        this.id = id;
        this.tasks = tasks;
        this.tasks.sort(Comparator.comparingInt(task -> task.step));
    }

    public WorkFlow(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
        this.tasks.sort(Comparator.comparingInt(task -> task.step));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "WorkFlow{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkFlow workFlow = (WorkFlow) o;

        return id == workFlow.id &&
                Objects.equals(name, workFlow.name) &&
                Objects.equals(tasks, workFlow.tasks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, tasks);
    }
}
