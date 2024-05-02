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
    private Partition partition;
    private ArrayList<ArrayList<Integer>> tournees;
    private ArrayList<Double> longTournees;

    public Planner(Double[][] distances, int k, int debut) {
        this.distances = distances;
        this.k = k;
        this.debut = debut;
        this.partition = new PartitionKCentre(distances.length, k, debut);
        this.tournees = new ArrayList<>();
        this.longTournees = new ArrayList<>();
        divise();
    }

    public void divise() {
        partition.partitionne(distances);
        calculeTournees();
    }

    public void calculeTournees() {
        for (List<Integer> subset : partition.getParties()) {
            ArrayList<Integer> optimizedTour = calculeUneTournee(new ArrayList<>(subset));
            tournees.add(optimizedTour);
            longTournees.add(calculeLongTournees(optimizedTour));
        }
    }

    public ArrayList<Integer> calculeUneTournee(ArrayList<Integer> subset) {
        Graphe graphe = new Graphe(getSousMatrice(subset), subset);
        Graphe mst = graphe.kruskal(); // Utilise Kruskal pour obtenir l'arbre couvrant minimal
        return mst.tsp(debut); // Utilise l'algorithme TSP pour optimiser la tournée à partir du MST
    }

    public Double[][] getSousMatrice(List<Integer> selec) {
        Double[][] sousMatrice = new Double[selec.size()][selec.size()];
        for (int i = 0; i < selec.size(); i++) {
            for (int j = 0; j < selec.size(); j++) {
                sousMatrice[i][j] = distances[selec.get(i)][selec.get(j)];
            }
        }
        return sousMatrice;
    }

    public double calculeLongTournees(ArrayList<Integer> tour) {
        double length = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            length += distances[tour.get(i)][tour.get(i + 1)];
        }
        length += distances[tour.get(tour.size() - 1)][tour.get(0)];
        return length;
    }

    public List<ArrayList<Integer>> getTournees() {
        return tournees;
    }

    public List<Double> getLongTournees() {
        return longTournees;
    }

    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }

    @Override
    public String toString() {
        return "Planner{" +
                "distances=" + Arrays.deepToString(distances) +
                ", k=" + k +
                ", debut=" + debut +
                ", partition=" + partition +
                ", tournees=" + tournees +
                ", longTournees=" + longTournees +
                '}';
    }
}
