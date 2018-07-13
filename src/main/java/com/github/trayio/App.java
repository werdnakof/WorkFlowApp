package com.github.trayio;

import static com.github.trayio.Transformers.*;

public class App {
    public static void main(String[] args) {
        ConsoleReader.create()
            .compose(getWorkFlow())
            .compose(saveWorkFlow())
            .compose(execute())
//                .subscribeOn(Schedulers.io())
            .subscribe(event -> {
                System.out.println(event);
            });
    }
}
