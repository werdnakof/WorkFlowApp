package com.github.trayio;

import io.reactivex.schedulers.Schedulers;

import static com.github.trayio.Transformers.*;

public class App {
    public static void main(String[] args) {
        System.out.println("Enter full path of your WorkFlow json file in console:");

        ConsoleReader.create()
            .compose(getWorkFlow())
            .compose(saveWorkFlow())
            .compose(execute())
            .subscribeOn(Schedulers.io())
            .blockingSubscribe();
    }
}
