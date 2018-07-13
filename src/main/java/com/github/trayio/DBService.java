package com.github.trayio;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBService implements DataBaseService {

    private final static Logger logger = Logger.getLogger(DBService.class.getName());

    private AtomicInteger workflowCounter = new AtomicInteger(1);
    private AtomicInteger executionCounter = new AtomicInteger(1);

    private static DBService instance;
    private ConcurrentHashMap<Integer, WorkFlow> workflows;
    private ConcurrentHashMap<Integer, WorkFlowExecution> executions;

    private DBService() {
        workflows = new ConcurrentHashMap<>();
        executions = new ConcurrentHashMap<>();
    }

    public synchronized Boolean write(WorkFlow wf) {
        if(workflows.contains(wf.getId())) return false;
        logger.log(Level.INFO, "writing: " + wf);
        workflows.putIfAbsent(wf.getId(), wf);
        return true;
    }

    public synchronized Boolean write(WorkFlowExecution wfe) {
        wfe.setDate(new Date());
        if(wfe.getId() == null) wfe.setId(executionCounter.getAndIncrement());
        if(executions.contains(wfe.getId())) return false;
        logger.log(Level.INFO, "writing: " + wfe);
        executions.putIfAbsent(wfe.getId(), wfe);
        return true;
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
