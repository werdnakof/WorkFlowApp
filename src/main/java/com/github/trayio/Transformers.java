package com.github.trayio;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.schedulers.Schedulers;
import java.util.logging.Logger;

public class Transformers {

    public static ObservableTransformer<ConsoleEvent, WorkFlow> getWorkFlow() {
        return event -> event.flatMap(e -> {
            return WorkFlowReader.get(e.getContent())
                    .filter(wf -> wf.tasks.size() != 0)
                    .subscribeOn(Schedulers.io());
        });
    }

    public static ObservableTransformer<WorkFlow, WorkFlow> saveWorkFlow() {
        return workFlowObservable -> workFlowObservable
                .doOnNext(wf -> DBService.getInstance().write(wf))
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<WorkFlowExecution, WorkFlowExecution> saveWorkFlowExecution() {
        return workFlowExecution -> workFlowExecution
                .doOnNext(wfe -> DBService.getInstance().write(wfe))
                .subscribeOn(Schedulers.io());
    }

    public static ObservableTransformer<WorkFlow, WorkFlowExecution> nextSteps() {
        return workflow -> workflow.concatMap(wf -> {
            return Observable.fromIterable(wf.getTasks()).concatMap(task -> {
                return Observable.just(new WorkFlowExecution(wf, task))
                        .compose(saveWorkFlowExecution());
            });
        });
    }

    public static ObservableTransformer<WorkFlow, WorkFlowExecution> execute() {
        return workflow -> workflow.compose(nextSteps());
    }
}
