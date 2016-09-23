package com.company;

// 6 softwareontwikkelaars

// 10 gebruikers

// SoftwareOntwikkelaarOverleg      3 softwareontwikkelaars met product owner                   userstories opgesteld en geprioriteerd

// GebruikersOverleg                1 softwareontwikkelaar met 1 gebruiker en product owner     problemen gebruikers besproken


// gebruiker heeft probleem, meldt bij bedrijf, wacht tot uitgenodigd voor overleg, als uitgenodigd: reist naar bedrijf en meldt dat aangekomen is, wacht tot jaap zegt dat kan overleggen


// softwareontwikkelaar meldt regelmatig dat beschikbaar is overleg. Projectleider in overleg: softwareontwikkelaar weer aan het werk.
// Projectleider niet in overleg: wacht tot projectleider uitnodigt OF wacht tot constateerd dat hij niet hoeft deel te nemen aan gesprek, daarna weer aan werk

// Softwareontwikkelaar kan beide gesprekken hebben


// Gebruikersoverleg
// Als gebruiker meldt en geen wachtende SO, dan wacht projectleider tot meldt.


import java.util.concurrent.Semaphore;

/**
 * Created by rickv on 19-9-2016.
 */
public class Bedrijf {
    private static final int AANTAL_SOFTWAREONTWIKKELAARS = 6;
    private static final int AANTAL_GEBRUIKERS = 10;
    private int AANTAL_SOFTWAREONTWIKKELAARS_BINNEN;
    private int AANTAL_SOFTWAREONTWIKKELAARS_UITGENODIGD;
    private int AANTAL_GEBRUIKERS_BINNEN;
    private int AANTAL_GEBRUIKERS_UITGENODIGD;

    private boolean inOverleg;
    private Gebruiker[] gebruiker;
    private Softwareontwikkelaar[] softwareontwikkelaar;
    private ProductOwner productOwner;
    private Semaphore checkin, waitingDev, waitingDevOverleg, waitingGebrUitn, waitingGebrOverleg, mutexGebr, mutexDev, mutexDevUitn, mutexGebrUitn;

    public Bedrijf() {
        checkin = new Semaphore(0, true);
        waitingGebrUitn = new Semaphore(10, true);
        waitingGebrOverleg = new Semaphore(10, true);
        waitingDev = new Semaphore(3, true);
        waitingDevOverleg = new Semaphore(0, true);

        mutexGebr = new Semaphore(1);
        mutexGebrUitn = new Semaphore(1);
        mutexDev = new Semaphore(1);
        mutexDevUitn = new Semaphore(1);


        inOverleg = false;
        AANTAL_SOFTWAREONTWIKKELAARS_BINNEN = 0;
        AANTAL_GEBRUIKERS_BINNEN = 0;
        AANTAL_GEBRUIKERS_UITGENODIGD = 0;
        AANTAL_SOFTWAREONTWIKKELAARS_UITGENODIGD = 0;


        // Aanmaken gebruikers
        gebruiker = new Gebruiker[AANTAL_GEBRUIKERS];
        for(int i=0; i < AANTAL_GEBRUIKERS; i++){

            gebruiker[i] = new Gebruiker("g"+i,i);
            gebruiker[i].start();
        }

        // Aanmaken softwareontwikkelaars
        softwareontwikkelaar = new Softwareontwikkelaar[AANTAL_SOFTWAREONTWIKKELAARS];
        for(int i=0; i < AANTAL_SOFTWAREONTWIKKELAARS; i++){

            softwareontwikkelaar[i] = new Softwareontwikkelaar("s"+i,i);
            softwareontwikkelaar[i].start();
        }

        // Aanmaken Product Owner
        productOwner = new ProductOwner();
        productOwner.start();

    }

    private void SoftwareOntwikkelaarOverleg(){
        try{
            inOverleg=true;

            System.out.println("in softwareoverleg");

            System.out.println("in softwareoverleg - AANTAL DEVS: " + AANTAL_GEBRUIKERS_UITGENODIGD);
            System.out.println("in softwareoverleg - PERMITS DEVS: " + waitingDevOverleg.availablePermits());

            waitingDev.release(3);

            AANTAL_SOFTWAREONTWIKKELAARS_BINNEN=0;
            System.out.println("in softwareoverleg - PERMITS DEVS: " + waitingDevOverleg.availablePermits());
            System.out.println("in softwareoverleg - AANTAL DEVS: " + AANTAL_GEBRUIKERS_UITGENODIGD);


            Thread.sleep(2000);

            inOverleg=false;
        } catch(InterruptedException e){}
    }

    private void GebruikersOverleg(){
        try{
            inOverleg=true;

            // De duur van het gesprek
            Thread.sleep(2000);

            // TEST PRINT
            System.out.println("in gebruikersoverleg");
            System.out.println("in gebruikersoverleg - AANTAL GEBR: " + AANTAL_GEBRUIKERS_UITGENODIGD);
            System.out.println("in gebruikersoverleg - PERMITS GEBR: " + waitingGebrOverleg.availablePermits());
            //


            // GEBR vrijlaten na gesprek
            waitingGebrOverleg.release(AANTAL_GEBRUIKERS_UITGENODIGD);

            // TEST PRINT
            System.out.println("in gebruikersoverleg - PERMITS GEBR: " + waitingGebrOverleg.availablePermits());
            //

            // Aantal uitgenodigde DEVS is nu 0
            mutexDev.acquire();
            AANTAL_SOFTWAREONTWIKKELAARS_UITGENODIGD=0;
            mutexDev.release();

            // Aantal uitgenodigde GEBR is nu 0
            mutexGebr.acquire();
            AANTAL_GEBRUIKERS_UITGENODIGD=0;
            mutexGebr.release();

            // TEST PRINT
            System.out.println("in gebruikersoverleg - AANTAL GEBR: " + AANTAL_GEBRUIKERS_BINNEN);
            //


            inOverleg=false;

        } catch(InterruptedException e){}
    }

    public class ProductOwner extends Thread{

        public void run() {
            while (true) {
                try {

                    checkin.acquire();

                    System.out.println("aantal gebruikers: "+AANTAL_GEBRUIKERS_BINNEN);
                    System.out.println("aantal softwareontwikkelaars: "+AANTAL_SOFTWAREONTWIKKELAARS_BINNEN);

                    if(AANTAL_GEBRUIKERS_BINNEN>=1){

                        // Zeker een gebruiksoverleg, maar eerst minstens 1 dev aanwezig

                        if(AANTAL_SOFTWAREONTWIKKELAARS_BINNEN>=1) {

                            // gebruiksoverleg kan plaatsvinden, eerst iedereen uitnodigen

                            System.out.println("gebruikersoverleg");

                            // gebruikers naar stap uitnodiging
                            waitingGebrUitn.release(AANTAL_GEBRUIKERS_BINNEN);

                            // devs naar stap uitnodiging
                            waitingDev.release(AANTAL_SOFTWAREONTWIKKELAARS_BINNEN);


                            mutexDev.acquire();
                            AANTAL_SOFTWAREONTWIKKELAARS_BINNEN=0;
                            mutexDev.release();

                            mutexDev.acquire();
                            AANTAL_GEBRUIKERS_BINNEN=0;
                            mutexDev.release();

                            // 1 DEV uitnodigen
                            waitingDevOverleg.release();

                            // Alle GEBR uitnodigen
                            waitingGebrOverleg.release(AANTAL_GEBRUIKERS_UITGENODIGD);

                            // Overleg starten
                            GebruikersOverleg();
                        }

                    }else if (AANTAL_SOFTWAREONTWIKKELAARS_BINNEN>=3){

                        // Een softwareoverleg

                        System.out.println("softwareoverleg");

                        // Devs naar stap uitnodiging
                        waitingDev.release(AANTAL_SOFTWAREONTWIKKELAARS_BINNEN);

                        mutexDev.acquire();
                        AANTAL_SOFTWAREONTWIKKELAARS_BINNEN=0;
                        mutexDev.release();

                        // 3 DEVS uitnodigen
                        waitingDevOverleg.release(3);

                        // Overleg starten
                        SoftwareOntwikkelaarOverleg();
                    }else{
                        System.out.println("Jaap slaapt");
                    }

                } catch (InterruptedException e) {}
            }
        }
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
                    Thread.sleep((int)(Math.random()*5000));
                    checkin.release();

                    if(!inOverleg) {

                        mutexDev.acquire();
                        AANTAL_SOFTWAREONTWIKKELAARS_BINNEN++;
                        mutexDev.release();

                        waitingDev.acquire();

                        if(waitingDevOverleg.tryAcquire()){
                            System.out.println("DEV in overleg");
                            mutexDevUitn.acquire();
                            AANTAL_SOFTWAREONTWIKKELAARS_UITGENODIGD++;
                            mutexDevUitn.release();
                        }

                    }
                } catch (InterruptedException e) {}
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
                    Thread.sleep((int)(Math.random()*100000));
                    checkin.release();

                    mutexGebr.acquire();
                    AANTAL_GEBRUIKERS_BINNEN++;
                    mutexGebr.release();

                    waitingGebrUitn.acquire();

                    // aangekomen bij bedrijf
                    mutexGebrUitn.acquire();
                    AANTAL_GEBRUIKERS_UITGENODIGD++;
                    mutexGebrUitn.release();

                    waitingGebrOverleg.acquire();
                    System.out.println("GEBRUIKER ACQUIRED");

                } catch (InterruptedException e) {}
            }
        }
    }
}
