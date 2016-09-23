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
public class Bedrijf2 {
    private static final int AANTAL_SOFTWAREONTWIKKELAARS = 6;
    private static final int AANTAL_GEBRUIKERS = 10;
    private int AANTAL_SOFTWAREONTWIKKELAARS_BINNEN;
    private int AANTAL_GEBRUIKERS_BINNEN;
    private int wachtendeSoftwareOntwikkelaars;
    private Gebruiker[] gebruiker;
    private Softwareontwikkelaar[] softwareontwikkelaar;
    private ProductOwner productOwner;
    private Semaphore probleemMelding, uitnodigingGesprek, aankomstMelding, gesprekUitnodiging,
            beschikbaarVoorOverleg, inOverleg, checkin;

    public Bedrijf2() {
        checkin = new Semaphore(AANTAL_GEBRUIKERS+AANTAL_SOFTWAREONTWIKKELAARS, true);
        uitnodigingGesprek = new Semaphore(0, true);
        inOverleg = new Semaphore(0,true);

        gebruiker = new Gebruiker[AANTAL_GEBRUIKERS];
        for(int i=0; i < AANTAL_GEBRUIKERS; i++){

            gebruiker[i] = new Gebruiker("g"+i,i);
            gebruiker[i].start();
        }

        softwareontwikkelaar = new Softwareontwikkelaar[AANTAL_SOFTWAREONTWIKKELAARS];
        for(int i=0; i < AANTAL_SOFTWAREONTWIKKELAARS; i++){

            softwareontwikkelaar[i] = new Softwareontwikkelaar("s"+i,i);
            softwareontwikkelaar[i].start();
        }

        productOwner = new ProductOwner();
        productOwner.start();

    }

    private void SoftwareOntwikkelaarOverleg(){
        /*
        try{
            // 3 softwareontwikkelaars melden dan uitnodiging
            uitnodigingGesprek.release();
            // gebruiksoverleg of ontwikkelaaresoverleg (gebruiks heeft voorrang)

            // overleg klaar
            inOverleg.release();
            // softwareontwikkelaars weer werken, product owner slapen

        } catch(InterruptedException e){

        }
        */
    }

    private void GebruikersOverleg(){
        try{
            // gebruiker meld, zijn al wachtende softwareontwikkelaars dan punt 2
            aankomstMelding.acquire();
            // 1. softwareontwikkelaar meld

            // meerdere gemeldde gebruikers toelaten

            // alle gebruikers uitnodigen


            // 2.  gebruiker meteen uitgenodigd naar bedrijf

            // gebruiker uitgenodig voor gesprek

            // wachtende softwareontwikkelaars op de hoogte gebracht van gesprek

            inOverleg.release();

        } catch(InterruptedException e){

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
                    checkin.acquire();

                    // meldt regelmatig dat beshcikbaar is voor overleg
                    // als projectleider in overleg is, verder met werk
                    // als projectleider niet in overleg is, wachten voor uitnodiging voor een overleg

                    // OF tot geconstateerd dat niet bij gesprek hoort, dan verder met werk

                } catch (InterruptedException e) {}
            }
        }
    }

    public class ProductOwner extends Thread{

        public void run() {
            while (true) {
                /*try {


                } catch (InterruptedException e) {}*/


            }
        }

        private void overleg(){
            try{
                System.out.println("in overleg");
                Thread.sleep((int)(Math.random()*1000));

            } catch(InterruptedException e){}
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
                    checkin.acquire();

                    // probleem?, melden bij bedrijf
                    // wachten tot uitgenodigd voor overleg
                    // reizen naar bedrijf en melden aangekomen om te overleggen

                    // wachten tot product owner zegt dat gesprek begint
                } catch (InterruptedException e) {}
            }
        }
    }
}
