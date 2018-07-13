package com.github.trayio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBService implements DataBaseService {

    private final static Logger logger = Logger.getLogger(DBService.class.getName());

    private AtomicInteger workflowCounter = new AtomicInteger(0);
    private AtomicInteger executionCounter = new AtomicInteger(0);

    private static DBService instance;
    private ConcurrentHashMap<Integer, WorkFlow> workflows;
    private ConcurrentHashMap<Integer, WorkFlowExecution> executions;

    public static void log(String entry) {
        String log = "writing: " + entry + " on Thread:" + Thread.currentThread().getName() + "\n";
        logger.log(Level.INFO, log);

        try {
            Files.write(Paths.get("log.txt"), log.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private DBService() {
        workflows = new ConcurrentHashMap<>();
        executions = new ConcurrentHashMap<>();
    }

    public synchronized void write(WorkFlow wf) {
        wf.setId(workflowCounter.getAndIncrement());
        workflows.putIfAbsent(wf.getId(), wf);
        log(wf.toString());
    }

    public synchronized void write(WorkFlowExecution wfe) {
        wfe.setDate(new Date());
        wfe.setId(executionCounter.getAndIncrement());
        executions.putIfAbsent(wfe.getId(), wfe);
        log(wfe.toString());
    }

    @Override
    public WorkFlow getWorkFlow(Integer id) {
        return workflows.getOrDefault(id, null);
    }

    @Override
    public WorkFlowExecution getWorkFlowExecution(Integer id) {
        return executions.getOrDefault(id, null);
    }

    public static synchronized DBService getInstance() {
        if(instance == null) {
            instance = new DBService();
        }
        return instance;
    }
}
