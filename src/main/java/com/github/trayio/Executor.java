package com.github.trayio;

import io.reactivex.Observable;

import java.util.Objects;

public class Executor {

    public enum Status {
        SUCCESS, FAILURE
    }

    public static class Response {
        public Integer workFlowExecutionId;
        public Status status;

        public Response(Integer workFlowExecutionId, Status status) {
            this.workFlowExecutionId = workFlowExecutionId;
            this.status = status;
        }

        public boolean isSuccess() {
            return this.status == Status.SUCCESS;
        }

        public boolean isFail() {
            return this.status == Status.FAILURE;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Response response = (Response) o;
            return Objects.equals(workFlowExecutionId, response.workFlowExecutionId) &&
                    status == response.status;
        }

        @Override
        public int hashCode() {

            return Objects.hash(workFlowExecutionId, status);
        }
    }

    public static Observable<Response> submit(WorkFlowExecution wfe) {
        // start tasks e.g. call external api, save/read data to remote db
        if(wfe.getName().matches("force-error")) {
            return Observable.just(new Response(wfe.getId(), Status.FAILURE));
        }

        return Observable.just(new Response(wfe.getId(), Status.SUCCESS));
    }
}
