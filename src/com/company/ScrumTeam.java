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
            checkIn = new Semaphore(AANTAL_SOFTWAREONTWIKKELAARS,true);
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

                            // Adds a Developer
                            mutexDev.acquire();
                            checkinDevs++;
                            System.out.println(checkinDevs + " devs");
                            mutexDev.release();

                            // Wait for invite
                            inviteDevs.acquire();

                            // Developer ready for meeting
                            readyForMeeting.release();

                            // Waiting for the meeting to be done
                            meetingDone.acquire();
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
                        }else if(checkinDevs>=1 && checkInUser>=1){

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
                        mutexUser.acquire();

                        // Adds a User(waiting)
                        checkInUser++;
                        System.out.println(checkInUser + " users");
                        mutexUser.release();

                        // Wait for invite
                        inviteUsers.acquire();

                        // User is ready for meeting
                        readyForMeeting.release();

                        // Waiting until meeting is done
                        meetingDone.acquire();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

