package com.company;

import java.util.concurrent.Semaphore;

/**
 * Created by rickv on 22-9-2016.
 */
public class test {

    public test(){

        Semaphore sem = new Semaphore(2,true);

        try {

            System.out.println("aantal permits voor: "+sem.availablePermits());
            sem.acquire();
            sem.acquire();
            sem.release();

            System.out.println("aantal permits na: "+sem.availablePermits());

            sem.acquire();

            System.out.println("hier");



        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
