package com.github.trayio;

public interface DataBaseService {
    void write(WorkFlow wf);
    void write(WorkFlowExecution wf);
    WorkFlow getWorkFlow(Integer id);
    WorkFlowExecution getWorkFlowExecution(Integer id);
}
