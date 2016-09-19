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
    private ProductOwner productOwner;
    private Semaphore uitnodiging;


    private void SoftwareOntwikkelaarOverleg(){
        try{

        } catch(InterruptedException e){

        }
    }

    private void GebruikersOverleg(){
        try{

        } catch(InterruptedException e){

        }
    }

    public class Softwareontwikkelaar extends Thread{

        public void run() {
            while (true) {
                try {

                } catch (InterruptedException e) {}
            }
        }
    }

    public class ProductOwner extends Thread{

        public void run() {
            while (true) {
                try {

                } catch (InterruptedException e) {}
            }
        }
    }

    public class Gebruiker extends Thread{

        public void run() {
            while (true) {
                try {

                } catch (InterruptedException e) {}
            }
        }
    }



}
