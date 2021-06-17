package com.card.script;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyClass {
    ExecutorService pool = Executors.newFixedThreadPool(12);

    public void a(){

        for (int i = 0; i < 100; i++) {
            final String name = i+"";

            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(name);
                }
            });
        }


    }


    public static void main(String[] args) {
        MyClass a = new MyClass();
        a.a();
    }
}