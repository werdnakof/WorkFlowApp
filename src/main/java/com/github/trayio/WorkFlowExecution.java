package com.github.trayio;

import java.util.Date;

public class WorkFlowExecution {
    private int id;
    private WorkFlow workflow;
    private int currentStep;
    private Date date;

    public WorkFlowExecution(WorkFlow workflow, int currentStep) {
        this.workflow = workflow;
        this.currentStep = currentStep;
    }

    public int getId() {
        return id;
    }

    public WorkFlow getWorkflow() {
        return workflow;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "WorkFlowExecution{" +
                "id=" + id +
                ", workflowId=" + workflow.id +
                ", currentStep=" + currentStep +
                ", date=" + date +
                '}';
    }
}
