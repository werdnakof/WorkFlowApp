package com.github.trayio;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WorkFlow {


    public int id;
    public int steps;
    public List<Integer> tasks;

    public WorkFlow(int id, int steps, List<Integer> tasks) {
        this.id = id;
        this.steps = steps;
        this.tasks = tasks;
    }

    public WorkFlow(int steps, List<Integer> tasks) {
        this.steps = steps;
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WF{id=" + id + ", steps=" + steps + '}';
    }


    public List<Integer> getTasks() {
        return tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkFlow workFlow = (WorkFlow) o;
        return id == workFlow.id &&
                steps == workFlow.steps &&
                Objects.equals(tasks, workFlow.tasks);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, steps, tasks);
    }
}
