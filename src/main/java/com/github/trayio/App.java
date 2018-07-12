package com.github.trayio;

public class App {
    public static void main(String[] args) {
        ConsoleReader.create()
            .compose(Transformers.getWorkFlow())
            .compose(Transformers.execute())
//                .subscribeOn(Schedulers.io())
            .subscribe(event -> {
                System.out.println(event);
            });
    }
}
