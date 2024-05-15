package l3m.cyber.planner.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Graphe implements Cloneable {
    private int nbSommets; // Nombre de sommets dans le graphe
    private Integer[][] adj; // Matrice d'adjacence
    private Double[][] poidsA; // Matrice des poids des arêtes
    private ArrayList<Integer> nomSommets;// Liste des noms des sommets

    // Constructeurs

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

    // Crée un graphe avec n sommets et aucune arête
    public Graphe(int n) {
            this.nbSommets = n;
            this.adj = new Integer[n][n];
            this.poidsA = new Double[n][n];
            this.nomSommets = Auxiliaire.integerList(n);
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


    public void ajouterArete(int i, int j, double poids) {
        this.adj[i][j] = 1;
        this.adj[j][i] = 1;
        this.poidsA[i][j] = poids;
        this.poidsA[j][i] = poids;
    }

    public void ajusterPoids(int i, int j, double poids) {
        this.poidsA[i][j] = poids;
        this.poidsA[j][i] = poids;
    }

    public void retirerArete(int i, int j) {
        this.adj[i][j] = 0;
        this.adj[j][i] = 0;
        this.poidsA[i][j] = null;
        this.poidsA[j][i] = null;
    }

    public boolean voisins(int i, int j) {
        return adj[i][j] == 1;
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
        boolean[] visited = new boolean[nbSommets];
        ArrayList<Integer> order = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();

        stack.push(debut);

        while (!stack.isEmpty()) {
            int current = stack.pop();

            if (!visited[current]) {
                visited[current] = true;
                order.add(current);

                // Parcourt tous les sommets du graphe et les ajoute à la pile
                // Trier les voisins par poids pour assurer un ordre déterministe
                List<Triplet> neighbors = getNeighbors(current);
                Collections.sort(neighbors);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    int neighbor = neighbors.get(i).getC2();
                    if (!visited[neighbor]) {
                        stack.push(neighbor);
                    }
                }
            }
        }

        return order;
    }

    
    // Effectue un parcours en profondeur (DFS) à partir du sommet courant dans le
    // graphe donné
    private void dfs(int debut, int current, Graphe graphe, boolean[] visited, ArrayList<Integer> order) {
        // Marque le sommet courant comme visité
        visited[current] = true;

        // Ajoute le sommet courant à la liste d'ordre des visites
        order.add(current);

        // Parcourt tous les sommets du graphe
        for (int i = 0; i < nbSommets; i++) {
            // Si un sommet adjacent non visité est trouvé, lance un DFS récursif sur ce
            // sommet
            if (graphe.voisins(current, i) && !visited[i]) {
                dfs(debut, i, graphe, visited, order);
            }
        }

        // Si le sommet courant n'est pas le sommet de départ et est connecté au sommet
        // de départ, ajoute le sommet de départ à la liste d'ordre
        if (current != debut && graphe.voisins(current, debut)) {
            order.add(debut);
        }
    }

    // Vérifie si le graphe est connexe
    public boolean estConnexe() {
        ArrayList<Integer> resultat = parcoursLargeur(0); // On commence par le sommet 0
        return resultat.size() == nbSommets; // Si tous les sommets sont atteints, le graphe est connexe
    }

    // Retourne la liste des arêtes du graphe
    public List<Triplet> listeAretes() {
        List<Triplet> aretes = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            for (int j = i + 1; j < nbSommets; j++) {
                if (voisins(i, j)) {
                    aretes.add(new Triplet(i, j, this.poidsA[i][j]));
                }
            }
        }
        return aretes;
    }

    public List<Triplet> aretesTriees(boolean croissant) {
        List<Triplet> aretes = listeAretes();
        Collections.sort(aretes);
        if (!croissant) {
            Collections.reverse(aretes);
        }
        return aretes;
    }

    public Graphe kruskalInverse() {
        Graphe g = (Graphe) this.clone();
        List<Triplet> listeAretes = listeAretes();
        Collections.sort(listeAretes, Collections.reverseOrder());
        for (Triplet edge : listeAretes) {
            g.retirerArete(edge.getC1(), edge.getC2());
            if (!g.estConnexe()) {
                g.ajouterArete(edge.getC1(), edge.getC2(), edge.getPoids());
            }
        }
        return g;
    }
    public Graphe kruskal() {
        // Étape 1 : Initialiser un graphe T avec le même nombre de sommets et aucune
        // arête
        Graphe arbreCouvrantMin = new Graphe(nbSommets);

        // Étape 2 : Trier les arêtes du graphe courant par poids croissant
        List<Triplet> edges = aretesTriees(true);

        // Étape 3 : Initialiser la structure Union-Find pour la détection des cycles
        UnionFind uf = new UnionFind(nbSommets);

        // Étape 4 : Parcourir les arêtes triées
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

    public Double[][] getPoidsA() {
        return poidsA;
    }

    

    public ArrayList<Integer> tsp(int debut) {
        Graphe mst = kruskalInverse();

        ArrayList<Integer> order = new ArrayList<>();
        boolean[] visited = new boolean[nbSommets];

        dfs(debut, debut, mst, visited, order);

        order.add(debut);

        return order;
    }

    
    // Retourne la liste des voisins d'un sommet donné
    private List<Triplet> getNeighbors(int sommet) {
        List<Triplet> neighbors = new ArrayList<>();
        for (int i = 0; i < nbSommets; i++) {
            if (voisins(sommet, i)) {
                neighbors.add(new Triplet(sommet, i, poidsA[sommet][i]));
            }
        }
        return neighbors;
    }

    public int getNbSommets() {
        return nbSommets;
    }

   

}