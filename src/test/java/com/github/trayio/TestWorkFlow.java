package com.github.trayio;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestWorkFlow {
    @Test
    public void test_readJson() {
        WorkFlow expected = new WorkFlow(3, Arrays.asList(0, 1, 2));
        WorkFlow result = WorkFlowReader.get("test.json").blockingFirst();
        assertEquals(expected, result);
    }
}
