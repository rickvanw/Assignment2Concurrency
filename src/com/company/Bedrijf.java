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
    private Gebruiker[] gebruiker;
    private Softwareontwikkelaar[] softwareontwikkelaar;
    private ProductOwner productOwner;
    private Semaphore uitnodiging;

    public Bedrijf() {


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

        private int myId;
        public Softwareontwikkelaar(String name, int id){
            super(name);
            myId = id;
        }

        public void run() {
            while (true) {
                try {

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
                try {

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
                    // probleem?, melden bij bedrijf

                    // wachten tot uitgenodigd voor overleg

                    // reizen naar bedrijf en melden aangekomen om te overleggen

                    // wachten tot product owner zegt dat gesprek begint


                } catch (InterruptedException e) {}
            }
        }
    }



}
