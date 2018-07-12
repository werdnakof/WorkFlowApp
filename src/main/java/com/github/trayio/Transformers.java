package com.github.trayio;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class Transformers {

    public static ObservableTransformer<ConsoleEvent, WorkFlow> getWorkFlow() {
        return event -> event.concatMap(e -> {
            return WorkFlow.get(e.getContent()).create()
                    .doOnNext(wf -> DBService.getInstance().write(wf));
        });
    }

    public static ObservableTransformer<WorkFlow, WorkFlowExecution> execute() {
        return workflow -> workflow.compose(nextSteps());
    }

    public static ObservableTransformer<WorkFlow, WorkFlowExecution> nextSteps() {
        return workflow -> workflow.concatMap(wf -> {
            return Observable.fromIterable(wf.getTasks()).concatMap(task -> {
                return Observable.just(new WorkFlowExecution(wf, task))
                        .doOnNext(wfe -> DBService.getInstance().write(wfe));
            });
        });
    }
}
