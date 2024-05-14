package l3m.cyber.planner.utils;

// Classe Triplet pour représenter une arête dans le graphe

public class Triplet implements Comparable<Triplet> {
    private int c1; // Premier sommet de l'arête
    private int c2; // Deuxième sommet de l'arête
    private double c3; // Poids de l'arête

    // Constructeur
    public Triplet(int a, int b, double poids) {
        this.c1 = a;
        this.c2 = b;
        this.c3 = poids;
    }

    // Getters
    public int getC1() {
        return c1;
    }

    public int getC2() {
        return c2;
    }

    public double getPoids() {
        return c3;
    }

    @Override
    public int compareTo(Triplet t) {
        return Double.compare(this.c3, t.getPoids());
    }

    // Représentation en chaîne de caractères de Triplet pour le débogage
    @Override
    public String toString() {
        return "Triplet{" + "c1=" + c1 + ", c2=" + c2 + ", c3=" + c3 + '}';
    }

    // Override equals method to comply with the contract of the compareTo method
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Triplet other = (Triplet) obj;
        return Double.compare(this.c3, other.c3) == 0;
    }

}
