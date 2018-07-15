package com.github.trayio;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class WorkFlowReaderTest {
    @Test
    public void test_get() {
        Task task0 = new Task(0, "step0");
        Task task1 = new Task(1, "step1");
        WorkFlow expected = new WorkFlow("test", Arrays.asList(task0, task1));
        assertEquals(expected, WorkFlowReader.get("test.json"));
    }
}
