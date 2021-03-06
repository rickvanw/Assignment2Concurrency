package com.company;

import java.util.concurrent.Semaphore;

/**
 * Created by rubenassink on 22-09-16.
 */
public class ScrumTeam {

    private static final int AANTAL_SOFTWAREONTWIKKELAARS = 6;
    private static final int AANTAL_GEBRUIKERS = 10;
    private Gebruiker[] gebruiker;
    private Softwareontwikkelaar[] softwareontwikkelaar;
    private ProductOwner productOwner;
    private Semaphore checkIn, mutexDev, mutexUser, inviteDevs, inviteUsers, readyForMeeting, meetingDone;
    public int checkInUser = 0;
    public int checkinDevs = 0;
    Boolean busy = false;


    public ScrumTeam() {
            checkIn = new Semaphore(0,true);
            mutexDev = new Semaphore(1);
            mutexUser = new Semaphore(1);
            inviteUsers = new Semaphore(0,true);
            inviteDevs = new Semaphore(0,true);
            readyForMeeting = new Semaphore(0,true);
            meetingDone = new Semaphore(0, true);

            // New Users
            gebruiker = new Gebruiker[AANTAL_GEBRUIKERS];
            for(int i=0; i < AANTAL_GEBRUIKERS; i++){

                gebruiker[i] = new Gebruiker("g"+i,i);
                gebruiker[i].start();
            }

            // New Developers
            softwareontwikkelaar = new Softwareontwikkelaar[AANTAL_SOFTWAREONTWIKKELAARS];
            for(int i=0; i < AANTAL_SOFTWAREONTWIKKELAARS; i++){

                softwareontwikkelaar[i] = new Softwareontwikkelaar("s"+i,i);
                softwareontwikkelaar[i].start();
            }

            productOwner = new ProductOwner();
            productOwner.start();

        }



        public class Softwareontwikkelaar extends Thread{

            private int myId;
            public Softwareontwikkelaar(String name, int id){
                super(name);
                myId = id;
            }

            public void run() {
                while (true) {

                    try {

                        // Developer checks in at random
                        Thread.sleep((int)(Math.random()*5000));

                        // If productowner !busy, proceed
                        if(!busy) {

                            // Checks in
                            checkIn.release();
                            // TEST System.out.println("1: Developer checked in");

                            // Adds a Developer
                            mutexDev.acquire();
                            checkinDevs++;
                            System.out.println(checkinDevs + " devs");
                            mutexDev.release();

                            // Wait for invite
                            // TEST System.out.println("2: Developer waiting for invite");
                            inviteDevs.acquire();

                            // Developer ready for meeting
                            // TEST System.out.println("3: Developer ready for meeting");
                            readyForMeeting.release();

                            // Waiting for the meeting to be done
                            // TEST System.out.println("4: Developer waiting for meeting to be done");
                            meetingDone.acquire();
                            // TEST System.out.println("5: Developer is done with meeting");

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public class ProductOwner extends Thread{

            public void run() {
                while (true) {
                    try {

                        // Checks all incoming ppl
                        checkIn.acquire();

                        if(checkinDevs>=3 && checkInUser ==0) {
                            // TESTS
                            /*
                            System.out.println("Aantal permits checkIn vóór: "+checkIn.availablePermits());
                            System.out.println("Aantal permits inviteUsers vóór: "+inviteUsers.availablePermits());
                            System.out.println("Aantal permits inviteDevs vóór: "+inviteDevs.availablePermits());
                            System.out.println("Aantal permits readyForMeeting vóór: "+readyForMeeting.availablePermits());
                            System.out.println("Aantal permits meetingDone vóór: "+meetingDone.availablePermits());
                            */

                            // ** Developer Meeting **

                            // Invite Devs(3)
                            inviteDevs.release(3);

                            // ProductOwner is busy
                            busy = true;

                            // Are the devs ready? start
                            readyForMeeting.acquire(3);

                            // Meeting
                            System.out.println("Dev meeting");
                            // Release the devs

                            // Meeting is over, reset the counters, product owner is available again
                            mutexDev.acquire();
                            checkinDevs = 0;
                            mutexDev.release();

                            meetingDone.release(3);

                            busy = false;
                            // TESTS
                            /*
                            System.out.println("Aantal permits checkIn na: "+checkIn.availablePermits());
                            System.out.println("Aantal permits inviteUsers na: "+inviteUsers.availablePermits());
                            System.out.println("Aantal permits inviteDevs na: "+inviteDevs.availablePermits());
                            System.out.println("Aantal permits readyForMeeting na: "+readyForMeeting.availablePermits());
                            System.out.println("Aantal permits meetingDone na: "+meetingDone.availablePermits());
                            */
                        }else if(checkinDevs>=1 && checkInUser>=1){

                            // TESTS
                            /*
                            System.out.println("Aantal permits checkIn vóór: "+checkIn.availablePermits());
                            System.out.println("Aantal permits inviteUsers vóór: "+inviteUsers.availablePermits());
                            System.out.println("Aantal permits inviteDevs vóór: "+inviteDevs.availablePermits());
                            System.out.println("Aantal permits readyForMeeting vóór: "+readyForMeeting.availablePermits());
                            System.out.println("Aantal permits meetingDone vóór: "+meetingDone.availablePermits());
                            */

                            // ** User Meeting **

                            int checkInUserLocal = checkInUser;

                            // invite users
                            inviteUsers.release(checkInUserLocal);

                            // invite dev
                            inviteDevs.release(1);

                            busy = true;

                            // Are the users and devs ready?
                            readyForMeeting.acquire(checkInUserLocal+1);

                            // Meeting
                            System.out.println("User meeting");
                            // Release the devs

                            // Meeting is over, reset the counters, product owner is available again
                            mutexUser.acquire();
                            checkInUser = 0;
                            mutexUser.release();

                            mutexDev.acquire();
                            checkinDevs = 0;
                            mutexDev.release();

                            meetingDone.release(checkInUserLocal+1);

                            busy = false;

                            // TESTS
                            /*
                            System.out.println("Aantal permits checkIn na: "+checkIn.availablePermits());
                            System.out.println("Aantal permits inviteUsers na: "+inviteUsers.availablePermits());
                            System.out.println("Aantal permits inviteDevs na: "+inviteDevs.availablePermits());
                            System.out.println("Aantal permits readyForMeeting na: "+readyForMeeting.availablePermits());
                            System.out.println("Aantal permits meetingDone na: "+meetingDone.availablePermits());
                            */
                        }



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }


        public class Gebruiker extends Thread{

            private int myId;
            public Gebruiker(String name, int id){
                super(name);
                myId = id;
            }

            public void run() {
                while (true) {
                    try {

                        // User checks in at random
                        Thread.sleep((int)(Math.random()*50000));

                        // Checks in
                        checkIn.release();
                        // TEST System.out.println("1: User checked in");

                        mutexUser.acquire();

                        // Adds a User(waiting)
                        checkInUser++;
                        System.out.println(checkInUser + " users");
                        mutexUser.release();

                        // Wait for invite
                        // TEST System.out.println("2: User waiting for invite");
                        inviteUsers.acquire();

                        // User is ready for meeting
                        readyForMeeting.release();
                        // TEST System.out.println("3: User ready for meeting");


                        // Waiting until meeting is done
                        // TEST System.out.println("4: User waiting for meeting to be done");
                        meetingDone.acquire();
                        // TEST System.out.println("5: User is done with meeting");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

