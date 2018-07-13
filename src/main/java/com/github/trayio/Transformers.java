package com.github.trayio;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;

public class Transformers {

    public static ObservableTransformer<ConsoleEvent, WorkFlow> getWorkFlow() {
        return event -> event.concatMap(e -> {
            return WorkFlow.get(e.getContent()).create();
        });
    }

    public static ObservableTransformer<WorkFlow, WorkFlow> saveWorkFlow() {
        return workFlowObservable -> workFlowObservable
                .doOnNext(wf -> DBService.getInstance().write(wf))
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<WorkFlowExecution, WorkFlowExecution> saveWorkFlowExcution() {
        return workFlowExecution -> workFlowExecution
                .doOnNext(wfe -> DBService.getInstance().write(wfe))
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<WorkFlow, WorkFlowExecution> nextSteps() {
        return workflow -> workflow.concatMap(wf -> {
            return Observable.fromIterable(wf.getTasks()).concatMap(task -> {
                return Observable.just(new WorkFlowExecution(wf, task))
                        .compose(saveWorkFlowExcution());
            });
        });
    }

    public static ObservableTransformer<WorkFlow, WorkFlowExecution> execute() {
        return workflow -> workflow.compose(nextSteps());
    }
}
