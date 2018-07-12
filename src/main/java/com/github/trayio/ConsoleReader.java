package com.github.trayio;

import io.reactivex.Observable;

import java.util.Scanner;

public class ConsoleReader  {
    public static Observable<ConsoleEvent> create() {
        return Observable.create(emitter -> {
            Scanner scanner = new Scanner(System.in);
            while (!emitter.isDisposed()) {
                String text = scanner.nextLine();
                emitter.onNext(new ConsoleEvent(text));
            }
            scanner.close();
        });
    }
}
