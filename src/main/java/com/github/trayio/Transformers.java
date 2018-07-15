package com.github.trayio;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

import static com.github.trayio.WorkFlowExecution.State.COMPLETED;
import static com.github.trayio.WorkFlowExecution.State.ERROR;

public class Transformers {

    public static ObservableTransformer<ConsoleEvent, WorkFlow> getWorkFlow() {
        return eventObs -> eventObs
                .observeOn(Schedulers.io())
                .flatMap(event -> Observable
                        .fromCallable(() -> WorkFlowReader.get(event.getContent())));
    }

    public static ObservableTransformer<WorkFlow, WorkFlow> saveWorkFlow() {
        return workFlow -> workFlow
                .observeOn(Schedulers.io())
                .doOnNext(wf -> DBService.getInstance().write(wf));
    }

    public static ObservableTransformer<Task, WorkFlowExecution> generate(WorkFlow workFlow) {
        return taskObs -> taskObs.concatMap(task -> WorkFlowExecution.create(workFlow, task)
                .compose(saveWorkFlowExecution())
        );
    }

    public static ObservableTransformer<WorkFlowExecution, WorkFlowExecution> saveWorkFlowExecution() {
        return workFlowExecution -> workFlowExecution
                .observeOn(Schedulers.io())
                .doOnNext(wfe -> DBService.getInstance().write(wfe));
    }

    public static ObservableTransformer<Executor.Response, Executor.Response> updateWorkFlowExecution() {
        return response -> response
                .observeOn(Schedulers.io())
                .doOnNext(re -> {
                    WorkFlowExecution.State state = re.isSuccess() ? COMPLETED : ERROR;
                    DBService.getInstance().updateStatus(re.workFlowExecutionId, state);
                        }
                );
    }

    public static ObservableTransformer<WorkFlowExecution, Executor.Response> execute() {
        return workFlowExecution -> workFlowExecution
                .concatMap(Executor::submit);
    }

    public static ObservableTransformer<WorkFlow, Executor.Response> start() {
        return workflowObs -> workflowObs.flatMap(wf -> {
            return Observable
                    .fromIterable(wf.getTasks())
                    .compose(generate(wf))
                    .compose(execute())
                    .takeUntil(Executor.Response::isFail)
                    .compose(updateWorkFlowExecution());
        });
    }
}
