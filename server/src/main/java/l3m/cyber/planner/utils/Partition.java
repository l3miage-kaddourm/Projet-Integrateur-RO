package l3m.cyber.planner.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Partition {
    protected int nbElem;
    protected int k;
    protected int elemSpecial;
    protected List<Integer> elems;
    protected List<List<Integer>> parties;

    public Partition(ArrayList<Integer> elems, int k, int elemSpecial) {
        this.elems = new ArrayList<>(elems);
        this.k = k;
        this.elemSpecial = elemSpecial;
        partitionVide();
    }

    public Partition(int n, int k, int elemSpecial) {
        this.elems = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            this.elems.add(i);
        }
        this.k = k;
        this.elemSpecial = elemSpecial;
        partitionVide();
    }

    public Partition(int n, int k) {
        this(n, k, 0); // Default elemSpecial to 0
    }

    protected void partitionVide() {
        this.parties = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            ArrayList<Integer> single = new ArrayList<>();
            single.add(elemSpecial);
            parties.add(single);
        }
    }

    public abstract void partitionne(Double[][] distances);

    public List<List<Integer>> getParties() {
        return parties;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Partition:\n");
        for (List<Integer> partie : parties) {
            sb.append(partie.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<Integer> getPartie(int i) {
        return parties.get(i);
    }
}
