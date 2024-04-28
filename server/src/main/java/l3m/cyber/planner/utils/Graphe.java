package l3m.cyber.planner.utils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Graphe implements Cloneable {

    private int nbSommets;
    private int[][] adj; // Matrice d'adjacence
    private Double[][] poidsA; // Matrice des poids des arêtes
    private ArrayList<Integer> nomSommets; // Liste des noms des sommets

    // Constructeurs 

    public Graphe(int[][] adj, ArrayList<Integer> nomSommets) {
        this.nbSommets = nomSommets.size();
        this.adj = adj;
        this.poidsA = new Double[nbSommets][nbSommets];
        this.nomSommets = nomSommets;
        // Initialisez les poids avec des valeurs par défaut (1.0) où l'adjacence est 1
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                if (adj[i][j] == 1) {
                    poidsA[i][j] = 1.0;
                }
            }
        }
    }

    // Constructeur avec matrice des poids et liste des noms des sommets
    public Graphe(Double[][] poidsA, ArrayList<Integer> nomSommets) {
        this.nbSommets = nomSommets.size();
        this.adj = new int[nbSommets][nbSommets];
        this.poidsA = poidsA;
        this.nomSommets = nomSommets;
        // Construisez la matrice d'adjacence en fonction des poids
        for (int i = 0; i < nbSommets; i++) {
            for (int j = 0; j < nbSommets; j++) {
                if (poidsA[i][j] != null && poidsA[i][j] > 0) {
                    adj[i][j] = 1;
                }
            }
        }
    }


    // Constructeur avec matrice des poids sans liste des noms des sommets
    public Graphe(Double[][] poidsA, int n) {
        this(poidsA, Auxiliaire.integerList(n));
    }
    
    // Constructeur avec matrice d'adjacence sans liste des noms des sommets
    public Graphe(int nbSommets) {
        this.nbSommets = nbSommets;
        adj = new int[nbSommets][nbSommets];
        poidsA = new Double[nbSommets][nbSommets];
        nomSommets = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            nomSommets.add(i);
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

    // methode pour retourner une liste de tous les triplets représentant les arêtes du graphe.
    public List<Triplet> listeAretes() {
        List<Triplet> aretes = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            for (int j = i + 1; j < nbSommets; j++) {
                if (adj[i][j] == 1) {
                    aretes.add(new Triplet(i, j, poidsA[i][j]));
                }
            }
        }
        return aretes;
    }
    
    // méthode pour retourner une liste des arêtes triées par poids.
    public List<Triplet> aretesTriees(boolean croissant) {
        List<Triplet> aretes = listeAretes();
        if (croissant) {
            Collections.sort(aretes);
        } else {
            Collections.sort(aretes, Collections.reverseOrder());
        }
        return aretes;
    }

    // Méthode pour vérifier si le graphe est connexe
    public boolean estConnexe() {
        ArrayList<Integer> resultat = parcoursLargeur(0); // On commence par le sommet 0
        return resultat.size() == nbSommets; // Si tous les sommets sont atteints, le graphe est connexe
    }
    
    // Méthode pour obtenir les voisins d'un sommet
    public List<Integer> voisins(int i) {
        List<Integer> voisins = new ArrayList<>();
        for (int j = 0; j < nbSommets; j++) {
            if (adj[i][j] == 1) { // Si une arête existe entre i et j
                voisins.add(j);
            }
        }
        return voisins;
    }
    
     // Ajouter une arête non pondérée
     public void ajouterArete(int i, int j) {
        // Assurez-vous que i et j sont dans les limites de la matrice
        adj[i][j] = 1;
        adj[j][i] = 1; // Le graphe est non-orienté, donc symétrique
    }

    // Méthode pour ajouter une arête avec un poids
    public void ajouterArete(int i, int j, double poids) {
        if (i < 0 || i >= nbSommets || j < 0 || j >= nbSommets || i == j) {
            throw new IllegalArgumentException("Invalid node index");
        }
        adj[i][j] = 1;
        adj[j][i] = 1;
        poidsA[i][j] = poids;
        poidsA[j][i] = poids;
    }
    
    // Méthode pour retirer une arête
    public void retirerArete(int i, int j) {
        if (i < 0 || i >= nbSommets || j < 0 || j >= nbSommets) {
            throw new IllegalArgumentException("Invalid node index");
        }
        adj[i][j] = 0; 
        adj[j][i] = 0;
        poidsA[i][j] = null;
        poidsA[j][i] = null;
    }

    
    // Méthode pour ajuster le poids d'une arête existante
    public void ajusterPoids(int i, int j, double poids) {
        if (i < 0 || i >= nbSommets || j < 0 || j >= nbSommets) {
            throw new IllegalArgumentException("Indices de sommets invalides");
        }
        if (adj[i][j] == 0) {
            throw new IllegalArgumentException("Aucune arête existante entre les sommets spécifiés");
        }
        poidsA[i][j] = poids;
        poidsA[j][i] = poids; 
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

    // Parcours en profondeur 
    public ArrayList<Integer> parcoursProfondeur(int debut) {
        boolean[] visite = new boolean[this.nbSommets];
        ArrayList<Integer> resultat = new ArrayList<>();
        dfs(debut, visite, resultat);
        return resultat;
    }

    // Méthode récursive pour le parcours en profondeur
    private void dfs(int sommet, boolean[] visite, ArrayList<Integer> resultat) {
        visite[sommet] = true;
        resultat.add(sommet);

        for (int i = 0; i < this.nbSommets; i++) {
            if (this.adj[sommet][i] == 1 && !visite[i]) {
                dfs(i, visite, resultat);
            }
        }
    }

    
    // Algorithme de Kruskal pour trouver l'arbre couvrant minimal
    public Graphe kruskalInverse() {
        // Utiliser une liste pour stocker tous les triplets (arêtes avec poids)
        List<Triplet> edges = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            for (int j = i + 1; j < nbSommets; j++) {
                if (adj[i][j] != 0 && poidsA[i][j] != null) {
                    edges.add(new Triplet(i, j, poidsA[i][j]));
                }
            }
        }
        // Trier les arêtes par poids décroissant
        edges.sort((a, b) -> Double.compare(b.getC3(), a.getC3()));

    
        // Créer une forêt d'arbres (union-find)
        UnionFind uf = new UnionFind(nbSommets);
        // Construire l'arbre couvrant maximal
        Graphe arbreCouvrantMax = new Graphe(nbSommets);
        for (Triplet edge : edges) {
            int i = edge.getC1();
            int j = edge.getC2();
            if (uf.find(i) != uf.find(j)) {
                arbreCouvrantMax.ajouterArete(i, j, edge.getC3());
                uf.union(i, j);
            }
        }
        return arbreCouvrantMax;
    }
    
    // Algorithme de Dijkstra pour trouver le plus court chemin
    public ArrayList<Integer> tsp(int debut) {
        boolean[] visite = new boolean[nbSommets];
        ArrayList<Integer> cycle = new ArrayList<>();
        cycle.add(debut);
        visite[debut] = true;
    
        Integer courant = debut;
        // Continuer jusqu'à visiter chaque sommet une fois
        while (true) {
            int voisinLePlusProche = -1;
            double poidsLePlusFaible = Double.MAX_VALUE;
            // Trouver le voisin non visité le plus proche
            for (int j = 0; j < nbSommets; j++) {
                if (!visite[j] && poidsA[courant][j] != null && poidsA[courant][j] < poidsLePlusFaible) {
                    poidsLePlusFaible = poidsA[courant][j];
                    voisinLePlusProche = j;
                }
            }
            if (voisinLePlusProche == -1) {
                break; // Tous les sommets ont été visités
            }
            cycle.add(voisinLePlusProche);
            visite[voisinLePlusProche] = true;
            courant = voisinLePlusProche;
        }
        cycle.add(debut); // Fermer le cycle
        return cycle;
    }
    

    // Méthode clone pour créer une copie du graphe
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Graphe clone = (Graphe) super.clone();
        clone.adj = new int[nbSommets][];
        clone.poidsA = new Double[nbSommets][];
        for (int i = 0; i < nbSommets; i++) {
            clone.adj[i] = adj[i].clone();
            clone.poidsA[i] = poidsA[i].clone();
        }
        clone.nomSommets = new ArrayList<>(nomSommets);
        return clone;
    }
    


    // Méthode toString pour obtenir une représentation en chaîne de caractères du graphe
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

    public int[][] getAdj() {
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

    public void setAdj(int[][] adj) {
        this.adj = adj;
    }

    public void setPoidsA(Double[][] poidsA) {
        this.poidsA = poidsA;
    }

    public void setNomSommets(ArrayList<Integer> nomSommets) {
        this.nomSommets = nomSommets;
    }
    
    public Graphe kruskalMST() {
        // Récupérer la liste de toutes les arêtes du graphe
        List<Triplet> allEdges = listeAretes();
        
        // Trier les arêtes par poids croissant
        Collections.sort(allEdges, (a, b) -> Double.compare(a.getC3(), b.getC3()));
    
        // Initialiser la structure Union-Find pour gérer les composantes connexes
        UnionFind unionFind = new UnionFind(nbSommets);
    
        // Créer un graphe pour l'arbre couvrant minimal
        Graphe mst = new Graphe(nbSommets);
    
        // Ajouter des arêtes au MST sans former de cycles
        for (Triplet edge : allEdges) {
            int root1 = unionFind.find(edge.getC1());
            int root2 = unionFind.find(edge.getC2());
            if (root1 != root2) {
                mst.ajouterArete(edge.getC1(), edge.getC2(), edge.getC3());
                unionFind.union(root1, root2);
            }
        }
    
        return mst;
    }
    
    
}
