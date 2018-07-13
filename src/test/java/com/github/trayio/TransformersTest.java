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
        List<Integer> steps = Arrays.asList(0, 1, 2);
        WorkFlow expected = new WorkFlow(0,3, steps);
        TestObserver<WorkFlow> testObserver = ob.compose(getWorkFlow()).test();

        testObserver.awaitTerminalEvent();
        testObserver.assertValue(expected);
    }

    @Test
    public void test_saveWorkFlow() {
        WorkFlow expected = new WorkFlow(0, 3, Arrays.asList(0, 1, 2));

        TestObserver<WorkFlow> testObserver = Observable.just(expected)
                .compose(saveWorkFlow())
                .test();

        testObserver.awaitTerminalEvent();
        testObserver.assertValue(expected);

        assertEquals(expected, DBService.getInstance().getWorkFlow(0));
    }

    @Test
    public void test_saveWorkFlowExecution_1() {
        List<Integer> steps = Arrays.asList(1, 2, 3);
        WorkFlow wf = new WorkFlow(0, 3, steps);

        WorkFlowExecution wfe1 = new WorkFlowExecution(wf, 0);

        TestObserver<WorkFlowExecution> wfes1 = Observable.just(wfe1)
                .compose(saveWorkFlowExecution())
                .test();

        wfes1.awaitTerminalEvent();
        wfes1.assertValue(wfe1);

        assertEquals(wfe1, DBService.getInstance().getWorkFlowExecution(0));

        WorkFlowExecution wfe2 = new WorkFlowExecution(wf, 1);
        TestObserver<WorkFlowExecution> wfes2 = Observable.just(wfe2)
                .compose(saveWorkFlowExecution())
                .test();

        wfes2.awaitTerminalEvent();
        wfes2.assertValue(wfe2);
        assertEquals(wfe2, DBService.getInstance().getWorkFlowExecution(1));
    }

    @Test
    public void test_saveWorkFlowExecution_2() {
        List<Integer> steps = Arrays.asList(1, 2, 3);
        WorkFlow wf = new WorkFlow(1, 3, steps);

        WorkFlowExecution wfe1 = new WorkFlowExecution(0, wf, 0);
        WorkFlowExecution wfe2 = new WorkFlowExecution(1, wf, 1);
        WorkFlowExecution wfe3 = new WorkFlowExecution(2, wf, 2);

        TestObserver<WorkFlowExecution> wfes = Observable.fromIterable(Arrays.asList(0, 1, 2))
                .concatMap(num -> Observable.just(new WorkFlowExecution(wf, num))
                        .compose(saveWorkFlowExecution())).test();

        wfes.awaitTerminalEvent();
        wfes.assertValueCount(3);
        wfes.assertValues(wfe1, wfe2, wfe3);

        assertEquals(wfe1, DBService.getInstance().getWorkFlowExecution(0));
        assertEquals(wfe2, DBService.getInstance().getWorkFlowExecution(1));
        assertEquals(wfe3, DBService.getInstance().getWorkFlowExecution(2));
    }
}
