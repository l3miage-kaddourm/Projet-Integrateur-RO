package l3m.cyber.planner.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Partition {
    protected int nbElem;
    protected int k;
    protected int elemSpecial;
    protected ArrayList<Integer> elems;
    protected ArrayList<ArrayList<Integer>> parties;

    public Partition(ArrayList<Integer> elems, int k, int elemSpecial) {
        this.elems = elems;
        this.k = k;
        this.elemSpecial = elemSpecial;
        this.nbElem = elems.size();
        this.parties = new ArrayList<ArrayList<Integer>>();
        this.partitionVide();
    }

    public Partition(int nbElem, int k, int elemSpecial) {
        this.nbElem = nbElem;
        this.k = k;
        this.elemSpecial = elemSpecial;
        this.elems = new ArrayList<Integer>();
        for (int i = 0; i < nbElem; i++) {
            this.elems.add(i);
        }
        this.parties = new ArrayList<ArrayList<Integer>>();
        this.partitionVide();
    }

    public Partition(int n, int k) {
        this(n, k, 0); // mettre l'element special a 0 par defaut
    }

    public void partitionVide() {
        // create this.k parties
        for (int i = 0; i < this.k; i++) {
            ArrayList<Integer> tour = new ArrayList<Integer>();
            tour.add(this.elemSpecial);
            this.parties.add(tour);
        }
        // mettre l'element special dans chaque partie
    }

    public abstract void partitionne(Double[][] distances);

    public ArrayList<ArrayList<Integer>> getParties() {
        return parties;
    }

    @Override
    public String toString() {
        return "{ nombre d'elements = " + nbElem + ",  tournee = " + k + ", liste des elements = "
                + elems + ", parties = " + parties + " }";
    }

    public ArrayList<Integer> getPartie(int i) {
        return parties.get(i);
    }
}