package com.company;

import java.util.concurrent.Semaphore;

/**
 * Created by rickv on 15-9-2016.
 */
public class Barbershop {
    private static final int NR_OF_SEATS = 3;
    private static final int NR_OF_CUSTOMERS = 8;
    private Customer[] customer;
    private int customersInWaitingRoom = 0;
    private Barber barber;
    private Semaphore freeSeat, nextCustomer, hairCut, barberInvitation, inBarberChair, mutex;

    public Barbershop(){
        freeSeat = new Semaphore(NR_OF_SEATS, true); // begin alle stoelen vrij
        nextCustomer = new Semaphore(0, true); // begin geen klanten in de shop
        hairCut = new Semaphore(0, true); // niemand geknipt
        barberInvitation = new Semaphore(0, true); // niemand uitgenodigd voor barber chair
        inBarberChair = new Semaphore(0,true);
        mutex = new Semaphore(0);

        customer = new Customer[NR_OF_CUSTOMERS];
        for(int i=0; i < NR_OF_CUSTOMERS; i++){

            customer[i] = new Customer("c"+i,i);
            customer[i].start();
        }

        barber = new Barber();
        barber.start();

    }

    public class Barber extends Thread{

        public void run() {
            while (true) {
                try {
                    nextCustomer.acquire();
                    barberInvitation.release();
                    inBarberChair.acquire();
                    cut();
                    hairCut.release();

                } catch (InterruptedException e) {}
            }
        }

        private void cut(){
            try{
                // kniptijd
                System.out.println("kapper knipt");
                Thread.sleep((int)(Math.random()*1000));

            } catch(InterruptedException e){}
        }
    }

        public class Customer extends Thread{

            private int myId;
            public Customer(String name, int id){
                super(name);
                myId = id;
            }

            public void run(){
                while(true) {
                    try {
                        justLive();

                        mutex.acquire();
                        if(customersInWaitingRoom < NR_OF_SEATS) {
                            customersInWaitingRoom++;
                            mutex.release();

                            freeSeat.acquire();
                            nextCustomer.release();
                            barberInvitation.acquire();

                            mutex.acquire();
                            customersInWaitingRoom--;
                            mutex.release();

                            freeSeat.release();
                            inBarberChair.release();
                            hairCut.acquire();

                        }else{mutex.release();
                        }

                    } catch (InterruptedException e) {
                    }
                }

            }

            private void justLive(){

                /* try{
                    // wacht
                    //System.out.println("keal leeft");
                    Thread.sleep((int)(Math.random()*1000));

                } catch(InterruptedException e){}
                */
            }
            // justLive();

            // goto barber

            // take a seat

            // wait for invitation

            // sit in barber chair

            // get a haircut

            // leave barbershop

        }

        // wait for a customer


        // invite customer to barber chair

        // give customer a haircut



}


