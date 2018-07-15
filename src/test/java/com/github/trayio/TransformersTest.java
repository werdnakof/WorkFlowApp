package com.github.trayio;

import io.reactivex.functions.Predicate;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import io.reactivex.Observable;

import static com.github.trayio.Executor.Status.FAILURE;
import static com.github.trayio.Executor.Status.SUCCESS;
import static com.github.trayio.Transformers.*;
import static org.junit.Assert.assertEquals;

public class TransformersTest {

    private Task task1;
    private Task task2;
    List<Task> steps;
    private WorkFlow wf;

    @Before
    public void setTestData() {
        task1 = new Task(0, "step0");
        task2 = new Task(1, "step1");
        steps = Arrays.asList(task1, task2);
        wf = new WorkFlow("test1", steps);
    }

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = DBService.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void test_getWorkFlow() {
        WorkFlow expected = wf;

        TestObserver<WorkFlow> testObserver = Observable.just(new ConsoleEvent("./src/test/java/com/github/data/test1.json"))
                .compose(getWorkFlow())
                .test();

        testObserver.awaitTerminalEvent();
        testObserver.assertValue(expected);
    }

    @Test
    public void test_getWorkFlow_withNoTasks() {
        WorkFlow expected = new WorkFlow("test2", new ArrayList<>());

        TestObserver<WorkFlow> testObserver = Observable.just(new ConsoleEvent("./src/test/java/com/github/data/test2.json"))
                .compose(getWorkFlow())
                .test();

        testObserver.awaitTerminalEvent();
        testObserver.assertValue(expected);
    }

    @Test
    public void test_saveWorkFlow() {
        WorkFlow expected = wf;
        expected.setId(0);

        TestObserver<WorkFlow> testOb = Observable.just(expected)
                .compose(saveWorkFlow())
                .test();

        testOb.awaitTerminalEvent();
        testOb.assertValue(expected);

        assertEquals(expected, DBService.getInstance().getWorkFlow(0));
    }

    @Test
    public void test_saveWorkFlowExecution() {
        Task task4 = new Task(4, "step4");

        WorkFlowExecution wfe1 = new WorkFlowExecution(wf, task1);
        WorkFlowExecution wfe2 = new WorkFlowExecution(wf, task2);
        WorkFlowExecution wfe3 = new WorkFlowExecution(wf, task4);

        WorkFlowExecution expected1 = new WorkFlowExecution(0, wf, task1);
        WorkFlowExecution expected2 = new WorkFlowExecution(1, wf, task2);
        WorkFlowExecution expected3 = new WorkFlowExecution(2, wf, task4);

        TestObserver<WorkFlowExecution> testOb = Observable.fromArray(wfe1, wfe2, wfe3)
                .compose(saveWorkFlowExecution())
                .test();

        testOb.awaitTerminalEvent();

        assertEquals(expected1, DBService.getInstance().getWorkFlowExecution(0));
        assertEquals(expected2, DBService.getInstance().getWorkFlowExecution(1));
        assertEquals(expected3, DBService.getInstance().getWorkFlowExecution(2));
    }

    @Test
    public void test_execute() {

        WorkFlowExecution wfe1 = new WorkFlowExecution(10, wf, task1);
        WorkFlowExecution wfe2 = new WorkFlowExecution(11, wf, new Task(4, "force-error"));

        TestObserver<Executor.Response> testOb = Observable
                .just(wfe1, wfe2)
                .compose(execute())
                .test();

        testOb.awaitTerminalEvent();

        Executor.Response expected1 = new Executor.Response(10, SUCCESS);
        Executor.Response expected2 = new Executor.Response(11, Executor.Status.FAILURE);

        testOb.assertValueCount(2)
                .assertValueAt(0, response -> response.equals(expected1))
                .assertValueAt(1, response -> response.equals(expected2));
    }

    @Test
    public void test_updateWorkFlowExecution() {
        WorkFlowExecution wfe1 = new WorkFlowExecution(0, wf, task1);

        DBService.getInstance().write(wfe1);
        assertEquals(WorkFlowExecution.State.PENGING, DBService.getInstance().getWorkFlowExecution(0).getState());

        TestObserver<Executor.Response> testOb = Observable
                .just(new Executor.Response(0, SUCCESS))
                .compose(updateWorkFlowExecution())
                .test();

        testOb.awaitTerminalEvent();

        assertEquals(WorkFlowExecution.State.COMPLETED, DBService.getInstance().getWorkFlowExecution(0).getState());

        testOb = Observable
                .just(new Executor.Response(0, Executor.Status.FAILURE))
                .compose(updateWorkFlowExecution())
                .test();

        testOb.awaitTerminalEvent();
        assertEquals(WorkFlowExecution.State.ERROR, DBService.getInstance().getWorkFlowExecution(0).getState());
    }

    @Test
    public void test_start_successResponse() {

        TestObserver<Executor.Response> testOb = Observable
                .just(wf)
                .compose(start())
                .test();

        testOb.awaitTerminalEvent();

        testOb.assertValueCount(2);
        testOb.assertValues(
                new Executor.Response(0, SUCCESS),
                new Executor.Response(1, SUCCESS));
    }

    @Test
    public void test_start_failResponse() {

        wf = new WorkFlow("test1", Arrays.asList(new Task(0, "force-error"), task2, new Task(2, "something")));

        TestObserver<Executor.Response> testOb = Observable
                .just(wf)
                .compose(start())
                .test();

        testOb.awaitTerminalEvent();

        testOb.assertValueCount(1);
        testOb.assertValues(new Executor.Response(0, FAILURE));
    }
}
