package com.github.trayio;

public interface DataBaseService {
    Boolean write(WorkFlow wf);
    Boolean write(WorkFlowExecution wf);
    WorkFlow getWorkFlow(Integer id);
    WorkFlowExecution getWorkFlowExecution(Integer id);
}
