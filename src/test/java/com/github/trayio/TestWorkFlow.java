package com.github.trayio;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class TestWorkFlow {
    @Test
    public void dummytest() {
        WorkFlow wf = new WorkFlow(1, 0, new ArrayList<>());
        assertTrue(wf.tasks.size() == 0);
    }
}
