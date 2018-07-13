package com.github.trayio;

import java.util.Date;
import java.util.Objects;

public class WorkFlowExecution {
    private Integer id;
    private WorkFlow workflow;
    private Integer currentStep;
    private Date date;

    public WorkFlowExecution(WorkFlow workflow, int currentStep) {
        this.workflow = workflow;
        this.currentStep = currentStep;
    }

    public WorkFlowExecution(Integer id, WorkFlow workflow, Integer currentStep) {
        this.id = id;
        this.workflow = workflow;
        this.currentStep = currentStep;
    }

    public Integer getId() {
        return id;
    }

    public WorkFlow getWorkflow() {
        return workflow;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkFlowExecution that = (WorkFlowExecution) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(workflow, that.workflow) &&
                Objects.equals(currentStep, that.currentStep);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, workflow, currentStep);
    }
}
