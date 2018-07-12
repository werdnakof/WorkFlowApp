package com.github.trayio;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBService implements DataBaseService {

    private final static Logger logger = Logger.getLogger(DBService.class.getName());

    private AtomicInteger workflowCounter = new AtomicInteger(0);
    private AtomicInteger excutionCounter = new AtomicInteger(0);

    private static DBService instance;
    private ConcurrentHashMap<Integer, WorkFlow> workflows;
    private ConcurrentHashMap<Integer, WorkFlowExecution> excutions;

    private DBService() {
        workflows = new ConcurrentHashMap<>();
        excutions = new ConcurrentHashMap<>();
    }

    public Boolean write(WorkFlow wf) {
        logger.log(Level.INFO, "writing: " + wf);
        if(workflows.contains(wf.getId())) return false;
        workflows.putIfAbsent(wf.getId(), wf);
        return true;
    }

    public Boolean write(WorkFlowExecution wfe) {
        wfe.setDate(new Date());
        logger.log(Level.INFO, "writing: " + wfe);
        if(excutions.contains(wfe.getId())) return false;
        excutions.putIfAbsent(wfe.getId(), wfe);
        return true;
    }

    public static synchronized DBService getInstance() {
        if(instance == null) {
            instance = new DBService();
        }
        return instance;
    }
}
