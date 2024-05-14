package l3m.cyber.planner.utils;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Planner {
    private Double[][] distances;
    private int k;
    private int debut;
    private Partition p;
    private ArrayList<ArrayList<Integer>> tournees;
    private ArrayList<Double> longTournees;

    public Planner(PlannerParameter params) {
        this(params.matrix(), params.k(), params.start());
    }

    public Planner(Double[][] distances, int k, int debut) {
        this.distances = distances;
        this.k = k;
        this.debut = debut;
    }

    public void divise() {
        this.p = new PartitionKCentre(distances.length, k);
        p.partitionne(distances);
        tournees = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < k; i++) {
            tournees.add(p.getPartie(i));
        }
        calculeTournees();
        calculeLongTournees();

    }


    public ArrayList<ArrayList<Integer>> getTournees() {
        return tournees;
    }


    public ArrayList<Double> getLongTournees() {
        return longTournees;
    }

    public Double[][] getSousMatrice(List<Integer> selec) {
        int n = selec.size();
        Double[][] sousMatrice = new Double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sousMatrice[i][j] = distances[selec.get(i)][selec.get(j)];
            }
        }
        return sousMatrice;
    }


    public ArrayList<Integer> calculeUneTournee(ArrayList<Integer> selec) {
        Double[][] subMatrix = getSousMatrice(selec);
        Graphe graphe = new Graphe(subMatrix, selec); // Crée un sous-graphe
        ArrayList<Integer> result = new ArrayList<>();
        result.add(debut); // Ajouter le dépôt au début de la tournée
        ArrayList<Integer> tspOrder = graphe.tsp(debut);
        for (Integer vertex : tspOrder) {
            if (vertex != debut) {
                result.add(selec.get(vertex)); // Ajouter les sommets sélectionnés à la tournée
            }
        }

        return result;
    }

    public void calculeTournees() {
        for (int i = 0; i < tournees.size(); i++) {
            ArrayList<Integer> listElem = tournees.get(i);
            ArrayList<Integer> tournee = calculeUneTournee(listElem);
            tournees.set(i, tournee);
        }
    }




    public void calculeLongTournees() {
        longTournees = new ArrayList<Double>();
        for (ArrayList<Integer> listElem : tournees) {
            Double longueur = 0.0;
            for (int j = 0; j < listElem.size() - 1; j++) {
                longueur += distances[listElem.get(j)][listElem.get(j + 1)];
            }
            longueur += distances[listElem.getLast()][listElem.getFirst()];
            longTournees.add(longueur);
        }

    }


    @Override
    public String toString() {
        return "Planner{" +
                "distances=" + Arrays.deepToString(distances) +
                ", k=" + k +
                ", debut=" + debut +
                ", partition=" + p +
                ", tournees=" + tournees +
                ", longTournees=" + longTournees +
                '}';
    }


    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }


}