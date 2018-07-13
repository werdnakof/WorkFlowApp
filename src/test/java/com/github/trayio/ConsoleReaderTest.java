package com.github.trayio;

import io.reactivex.Observable;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class ConsoleReaderTest {
    @Test
    public void test_simulateConsoleInput() {
        String fileName = "test.json";

        ByteArrayInputStream in = new ByteArrayInputStream(fileName.getBytes());
        System.setIn(in);

        Observable<ConsoleEvent> ob = ConsoleReader.create();
        ConsoleEvent expected = new ConsoleEvent(fileName);

        ob.test().assertValue(expected);
    }


}
