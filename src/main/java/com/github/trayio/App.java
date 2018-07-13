package com.github.trayio;

import io.reactivex.schedulers.Schedulers;

import static com.github.trayio.Transformers.*;

public class App {
    public static void main(String[] args) {
        ConsoleReader.create()
            .compose(getWorkFlow())
            .compose(saveWorkFlow())
            .compose(execute())
            .subscribeOn(Schedulers.io())
            .blockingSubscribe();
    }
}
