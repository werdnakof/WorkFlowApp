package com.github.trayio;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

import static com.github.trayio.WorkFlowExecution.State.COMPLETED;
import static com.github.trayio.WorkFlowExecution.State.ERROR;

public class Transformers {

    public static ObservableTransformer<ConsoleEvent, WorkFlow> getWorkFlow() {
        return eventObs -> eventObs.flatMap(event -> {
            return Observable.fromCallable(() -> WorkFlowReader.get(event.getContent()))
                    .filter(wf -> wf.getTasks().size() != 0)
                    .subscribeOn(Schedulers.io());
        });
    }

    public static ObservableTransformer<WorkFlow, WorkFlow> saveWorkFlow() {
        return workFlow -> workFlow
                .doOnNext(wf -> DBService.getInstance().write(wf))
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<WorkFlowExecution, WorkFlowExecution> saveWorkFlowExecution() {
        return workFlowExecution -> workFlowExecution
                .doOnNext(wfe -> DBService.getInstance().write(wfe))
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<Executor.Response, Executor.Response> updateWorkFlowExecution() {
        return response -> response
                .doOnNext(re -> {
                        WorkFlowExecution.State state = re.isSuccess() ? COMPLETED : ERROR;
                        DBService.getInstance().updateStatus(re.workFlowExecutionId, state);
                    }
                )
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<WorkFlowExecution, Executor.Response> execute() {
        return workFlowExecution -> workFlowExecution
                .concatMap(Executor::submit)
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<WorkFlow, Executor.Response> start() {
        return workflowObs -> workflowObs.flatMap(workflow -> {
            return Observable
                    .fromIterable(workflow.getTasks())
                    .concatMap(task -> WorkFlowExecution.create(workflow, task)
                        .compose(saveWorkFlowExecution())
                    ).compose(execute())
                    .takeUntil(Executor.Response::isFail)
                    .compose(updateWorkFlowExecution());
        });
    }
}
