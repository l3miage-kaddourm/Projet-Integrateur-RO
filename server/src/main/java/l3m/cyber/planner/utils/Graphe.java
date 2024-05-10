package l3m.cyber.planner.utils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graphe implements Cloneable {

    private int nbSommets; // Nombre de sommets dans le graphe
    private Integer[][] adj; // Matrice d'adjacence
    private Double[][] poidsA; // Matrice des poids des arêtes
    private ArrayList<Integer> nomSommets;// Liste des noms des sommets

    // Constructeurs
    // Crée un graphe avec n sommets et aucune arête
    public Graphe(int n) {
        this.nbSommets = n;
        this.adj = new Integer[n][n];
        this.poidsA = new Double[n][n];
        this.nomSommets = Auxiliaire.integerList(n);
    }

    // Crée un graphe avec une matrice d'adjacence et une liste de noms de sommets
    public Graphe(Integer[][] adj, ArrayList<Integer> nomSommets) {
        if (Auxiliaire.estCarreeSym(adj)) {
            this.nbSommets = nomSommets.size();
            this.adj = adj;
            this.poidsA = null;
            this.nomSommets = nomSommets;
        } else {
            // Initialiser un graphe par défaut si la matrice n'est pas carrée et symétrique
            this.nbSommets = 0;
            this.adj = new Integer[0][0];
            this.poidsA = null;
            this.nomSommets = new ArrayList<>();
        }
    }

    // Crée un graphe avec une matrice de poids et une liste de noms de sommets
    public Graphe(Double[][] poidsA, ArrayList<Integer> nomSommets) {
        if (Auxiliaire.estCarreeSym(poidsA)) {
            this.nbSommets = nomSommets.size();
            this.poidsA = poidsA;
            this.nomSommets = nomSommets;
            this.adj = new Integer[nbSommets][nbSommets];
            for (int i = 0; i < nbSommets; i++) {
                for (int j = 0; j < nbSommets; j++) {
                    this.adj[i][j] = (poidsA[i][j] != null && poidsA[i][j] > 0) ? 1 : 0;
                }
            }
        } else {
            // Initialiser un graphe par défaut si la matrice n'est pas carrée et symétrique
            this.nbSommets = 0;
            this.poidsA = null;
            this.adj = new Integer[0][0];
            this.nomSommets = new ArrayList<>();
        }
    }

    // Crée un graphe avec une matrice d'adjacence et initialise les noms des
    // sommets de 0 à n-1
    public Graphe(ArrayList<Integer> nomSommets) {
        this.nomSommets = nomSommets;
        this.nbSommets = nomSommets.size();
        this.adj = new Integer[nbSommets][nbSommets];
        this.nomSommets = Auxiliaire.integerList(nbSommets);
    }

    // Crée un graphe avec une matrice de poids, initialise les noms des sommets de
    // 0 à n-1 et construit la matrice d'adjacence
    public Graphe(Double[][] poidsA, int n) {
        if (!Auxiliaire.estCarreeSym(poidsA)) {
            this.nbSommets = n;
            this.poidsA = poidsA;
            this.nomSommets = Auxiliaire.integerList(n);
            this.adj = new Integer[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    this.adj[i][j] = (poidsA[i][j] != null && poidsA[i][j] > 0) ? 1 : 0;
                }
            }
        } else {
            // Initialiser un graphe par défaut si la matrice n'est pas carrée et symétrique
            this.nbSommets = 0;
            this.poidsA = new Double[0][0];
            this.adj = new Integer[0][0];
            this.nomSommets = new ArrayList<>();
        }
    }

    // Méthode pour pondérer les arêtes avec un poids par défaut
    public void pondereAretes() {
        for (int i = 0; i < this.nbSommets; i++) {
            for (int j = 0; j < this.nbSommets; j++) {
                if (this.adj[i][j] == 1 && this.poidsA[i][j] == null) {
                    this.poidsA[i][j] = 1.0; // Poids par défaut
                }
            }
        }
    }

    // methode pour retourner une liste de tous les triplets représentant les arêtes
    // du graphe.
    public List<Triplet> listeAretes() {
        List<Triplet> liste = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            for (int j = i + 1; j < nbSommets; j++) {
                if (adj[i][j] == 1) {
                    liste.add(new Triplet(i, j, poidsA[i][j]));
                }
            }
        }
        return liste;
    }

    // méthode pour retourner une liste des arêtes triées par poids.
    public List<Triplet> aretesTriees(boolean croissant) {
        List<Triplet> liste = listeAretes();
        if (croissant) {
            Collections.sort(liste);
        } else {
            Collections.sort(liste, Collections.reverseOrder());
        }
        return liste;
    }

    // Méthode pour vérifier si le graphe est connexe
    public boolean estConnexe() {
        ArrayList<Integer> resultat = parcoursLargeur(0); // On commence par le sommet 0
        return resultat.size() == nbSommets; // Si tous les sommets sont atteints, le graphe est connexe
    }

    public boolean voisins(int i, int j) {
        return adj[i][j] == 1;
    }

    // Ajouter une arête non pondérée
    public void ajouterArete(int i, int j) {
        if (i >= 0 && i <= nbSommets && j >= 0 && j <= nbSommets) {
            this.adj[i][j] = 1;
            this.adj[j][i] = 1;
        }
    }

    // Méthode pour ajouter une arête avec un poids
    public void ajouterArete(int i, int j, double poids) {
        if (i >= 0 && i < nbSommets && j >= 0 && j < nbSommets && i != j) {
            adj[i][j] = 1;
            adj[j][i] = 1;
            poidsA[i][j] = poids;
            poidsA[j][i] = poids;
        }
    }

    public void retirerArete(int i, int j) {
        if (i >= 0 && i < nbSommets && j >= 0 && j < nbSommets) {
            adj[i][j] = 0;
            adj[j][i] = 0;
            if (poidsA != null) {
                poidsA[i][j] = 0.0;
                poidsA[j][i] = 0.0;
            }
        } else {
            // Ignorer silencieusement les demandes invalides
        }
    }

    // Méthode pour ajuster le poids d'une arête existante
    public void ajusterPoids(int i, int j) {
        if (adj[i][j] == 1 && poidsA[i][j] != null) {
            poidsA[i][j] *= 1.1; // en supposant qu'on veut augmenter le poids de 10%
            poidsA[j][i] *= 1.1; // en supposant qu'on veut augmenter le poids de 10%
        }

    }

    // Parcours en largeur
    public ArrayList<Integer> parcoursLargeur(int debut) {
        // Initialisation
        boolean[] visite = new boolean[this.nbSommets];
        ArrayList<Integer> resultat = new ArrayList<>();
        ArrayList<Integer> queue = new ArrayList<>();

        visite[debut] = true;
        queue.add(debut);

        while (!queue.isEmpty()) {
            int sommet = queue.remove(0);
            resultat.add(sommet);
            for (int i = 0; i < this.nbSommets; i++) {
                if (this.adj[sommet][i] == 1 && !visite[i]) {
                    visite[i] = true;
                    queue.add(i);
                }
            }
        }
        return resultat;
    }

    public ArrayList<Integer> parcoursProfondeur(int debut) {
        ArrayList<Integer> visiteProfondeur = new ArrayList<>();
        boolean[] visite = new boolean[nbSommets];
        ArrayList<Integer> pile = new ArrayList<>();
        pile.add(debut);
        while (!pile.isEmpty()) {
            int sommet = pile.remove(pile.size() - 1);
            if ((!visite[sommet])) {
                visiteProfondeur.add(sommet);
                visite[sommet] = true;
                for (int voisin = 0; voisin < nbSommets; voisin++) {
                    if (voisins(sommet, voisin) && !visite[voisin]) {
                        pile.add(voisin);
                    }
                }
            }

        }
        return visiteProfondeur;
    }

    // Algorithme de Kruskal pour trouver l'arbre couvrant minimal
    public Graphe kruskalInverse() {
        Graphe T = (Graphe) this.clone();
        List<Triplet> aretes = T.aretesTriees(true);
        int sommet1 = 0;
        int sommet2 = 0;
        double poids = 0;
        for (Triplet arete : aretes) {
            sommet1 = arete.getC1();
            sommet2 = arete.getC2();
            poids = arete.getPoids();
            T.retirerArete(sommet1, sommet2);
            if (!T.estConnexe()) {
                T.ajouterArete(sommet1, sommet2, poids);
            }
        }
        return T;
    }

    // tsp pour avoir un cycle hamiltonien a la fin
    public ArrayList<Integer> tsp(int debut) {
        Graphe T = this.kruskalInverse();
        ArrayList<Integer> parcours = T.parcoursProfondeur(debut);
        ArrayList<Integer> res = new ArrayList<>();
        for (int sommet : parcours) {
            if (!res.contains(sommet)) {
                res.add(sommet);
            }
        }
        return res;
    }

    @Override
    protected Object clone() {
        try {
            Graphe clone = (Graphe) super.clone();
            clone.adj = new Integer[nbSommets][];
            clone.poidsA = new Double[nbSommets][];
            for (int i = 0; i < nbSommets; i++) {
                clone.adj[i] = adj[i].clone();
                clone.poidsA[i] = poidsA[i].clone();
            }
            clone.nomSommets = new ArrayList<>(nomSommets);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Méthode toString pour obtenir une représentation en chaîne de caractères du
    // graphe
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graphe{\n");
        sb.append("nbSommets=").append(nbSommets).append(",\n");
        sb.append("adj=").append(Arrays.deepToString(adj)).append(",\n");
        sb.append("poidsA=").append(Arrays.deepToString(poidsA)).append(",\n");
        sb.append("nomSommets=").append(nomSommets).append("\n");
        sb.append('}');
        return sb.toString();
    }

    // Getters
    public int getNbSommets() {
        return nbSommets;
    }

    public Integer[][] getAdj() {
        return adj;
    }

    public Double[][] getPoidsA() {
        return poidsA;
    }

    public ArrayList<Integer> getNomSommets() {
        return nomSommets;
    }

    // Setters
    public void setNbSommets(int nbSommets) {
        this.nbSommets = nbSommets;
    }

    public void setAdj(Integer[][] adj) {
        this.adj = adj;
    }

    public void setPoidsA(Double[][] poidsA) {
        this.poidsA = poidsA;
    }

    public void setNomSommets(ArrayList<Integer> nomSommets) {
        this.nomSommets = nomSommets;
    }

    public Graphe kruskal() {
        // Liste des arêtes triées par poids croissant
        List<Triplet> edges = aretesTriees(true); // Utilise la méthode aretesTriees pour trier les arêtes

        // Créer une structure UnionFind pour gérer les composantes connexes
        UnionFind uf = new UnionFind(nbSommets);

        // Créer un nouveau graphe pour stocker l'arbre couvrant minimal
        Graphe arbreCouvrantMin = new Graphe(nbSommets);

        // Parcourir les arêtes triées
        for (Triplet edge : edges) {
            int u = edge.getC1();
            int v = edge.getC2();

            // Vérifier si l'ajout de cette arête crée un cycle
            if (uf.find(u) != uf.find(v)) {
                // Pas de cycle, ajouter l'arête à l'arbre couvrant
                arbreCouvrantMin.ajouterArete(u, v, edge.getPoids());
                uf.union(u, v); // Fusionner les composantes connexes
            }
        }

        return arbreCouvrantMin;
    }

}