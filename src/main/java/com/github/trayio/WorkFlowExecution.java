package com.github.trayio;

import io.reactivex.Observable;

import java.util.Date;
import java.util.Objects;

public class WorkFlowExecution {

    public enum State { PENGING, ERROR, COMPLETED }

    private Integer id;
    private WorkFlow workflow;
    private Integer currentStep;
    private String name;
    private State state = State.PENGING;
    private Date created;
    private Date updated;

    public WorkFlowExecution(WorkFlow workflow, Task task) {
        this.workflow = workflow;
        this.name = task.name;
        this.currentStep = task.step;
    }

    public WorkFlowExecution(Integer id, WorkFlow workflow, Task task) {
        this.id = id;
        this.workflow = workflow;
        this.name = task.name;
        this.currentStep = task.step;
    }

    public static Observable<WorkFlowExecution> create(WorkFlow workflow, Task task) {
        return Observable.just(new WorkFlowExecution(workflow, task));
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "WorkFlowExecution{" +
                "id=" + id +
                ", workflowId=" + workflow.getId() +
                ", currentStep=" + currentStep +
                ", state=" + state +
                ", created=" + created +
                ", updated=" + updated +
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
