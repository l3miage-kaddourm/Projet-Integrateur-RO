package l3m.cyber.planner.utils;


import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;

import java.util.ArrayList;
import java.util.List;

public class Planner {
    private Double[][] distances;
    private Partition partition;
    private ArrayList<ArrayList<Integer>> tournees;
    private ArrayList<Double> longTournees;

    public Planner(PlannerParameter param, Partition partition) {
        this.distances = param.matrix();
        this.partition = partition;
        this.tournees = new ArrayList<>();
        this.longTournees = new ArrayList<>();

        this.partition.partitionne(distances);
        createAndOptimizeTours();
    }

    private void createAndOptimizeTours() {
        for (List<Integer> partie : partition.getParties()) {
            ArrayList<Integer> tournee = new ArrayList<>(partie);
            tournee.add(partie.get(0)); // Assure that each tour starts and ends at the depot
            tournees.add(tournee);
            longTournees.add(calculateTourLength(tournee));
        }
    }

    private double calculateTourLength(List<Integer> tournee) {
        double length = 0.0;
        Integer lastStop = tournee.get(0); // Start at the depot
        for (Integer stop : tournee) {
            if (!stop.equals(lastStop)) {
                length += distances[lastStop][stop];
                lastStop = stop;
            }
        }
        length += distances[lastStop][tournee.get(0)]; // Return to the depot
        return length;
    }

    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }
}
