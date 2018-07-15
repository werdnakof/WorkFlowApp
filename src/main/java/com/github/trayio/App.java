package com.github.trayio;

import io.reactivex.schedulers.Schedulers;

import static com.github.trayio.Transformers.*;

public class App {
    public static void main(String[] args) {
        System.out.println("Enter full path of your WorkFlow json file in console: \n" +
        "(see tasks/task1.json for example)");

        ConsoleReader.create()
            .subscribeOn(Schedulers.io())
            .compose(getWorkFlow())
            .compose(saveWorkFlow())
            .compose(start())
            .subscribeOn(Schedulers.io())
            .blockingSubscribe();
    }
}
