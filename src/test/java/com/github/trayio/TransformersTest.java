package com.github.trayio;

import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;

import static com.github.trayio.Transformers.*;
import static org.junit.Assert.assertEquals;

public class TransformersTest {

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = DBService.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void test_getWorkFlow() {
        Observable<ConsoleEvent> ob = Observable.just(new ConsoleEvent("test.json"));
        List<Integer> steps = Arrays.asList(1, 2, 3);
        WorkFlow expected = new WorkFlow(1, 3, steps);
        ob.compose(getWorkFlow()).test().assertValue(expected);
    }

    @Test
    public void test_saveWorkFlow() {
        List<Integer> steps = Arrays.asList(1, 2, 3);
        WorkFlow expected = new WorkFlow(1, 3, steps);

        TestObserver<WorkFlow> testObserver = Observable.just(expected)
                .compose(saveWorkFlow())
                .test();
        testObserver.awaitTerminalEvent();
        testObserver.assertValue(expected);

        assertEquals(expected, DBService.getInstance().getWorkFlow(1));
    }

    @Test
    public void test_saveWorkFlowExecution_sync() {
        List<Integer> steps = Arrays.asList(1, 2, 3);
        WorkFlow wf = new WorkFlow(1, 3, steps);

        WorkFlowExecution wfe1 = new WorkFlowExecution(wf, 0);
        Observable<WorkFlowExecution> ob = Observable.just(wfe1);

        ob.compose(saveWorkFlowExcution()).test().assertValue(wfe1);
        assertEquals(wfe1, DBService.getInstance().getWorkFlowExecution(0));

        WorkFlowExecution wfe2 = new WorkFlowExecution(wf, 1);

        Observable.just(wfe2)
                .compose(saveWorkFlowExcution())
                .test()
                .assertValue(wfe2);

        assertEquals(wfe2, DBService.getInstance().getWorkFlowExecution(1));
    }

    @Test
    public void test_saveWorkFlowExecution_async() {
        List<Integer> steps = Arrays.asList(1, 2, 3);
        WorkFlow wf = new WorkFlow(1, 3, steps);

        WorkFlowExecution wfe1 = new WorkFlowExecution(1, wf, 0);
        WorkFlowExecution wfe2 = new WorkFlowExecution(2, wf, 1);
        WorkFlowExecution wfe3 = new WorkFlowExecution(3, wf, 2);

        TestObserver<WorkFlowExecution> wfes = Observable.fromIterable(Arrays.asList(0, 1, 2))
                .concatMap(num -> Observable.just(new WorkFlowExecution(wf, num))
                        .compose(saveWorkFlowExcution())).test();

        wfes.awaitTerminalEvent();
        wfes.assertValueCount(3);
        wfes.assertValues(wfe1, wfe2, wfe3);

        assertEquals(wfe1, DBService.getInstance().getWorkFlowExecution(1));
        assertEquals(wfe2, DBService.getInstance().getWorkFlowExecution(2));
        assertEquals(wfe3, DBService.getInstance().getWorkFlowExecution(3));
    }
}
