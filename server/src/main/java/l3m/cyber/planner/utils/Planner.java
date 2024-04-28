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

        // Partition the deliveries among the drivers
        this.partition.partitionne(distances);
        createAndOptimizeTours();
    }

    private void createAndOptimizeTours() {
        for (List<Integer> subset : partition.getParties()) {
            ArrayList<Integer> optimizedTour = optimizeTour(subset);
            tournees.add(optimizedTour);
            longTournees.add(calculateTourLength(optimizedTour));
        }
    }

    // Uses a TSP approach to find the optimal visiting order
    private ArrayList<Integer> optimizeTour(List<Integer> subset) {
        // Dummy implementation, replace with actual TSP solution
        ArrayList<Integer> tour = new ArrayList<>(subset);
        tour.add(subset.get(0)); // Ensures tour starts and ends at the depot
        return tour;
    }

    public double calculateTourLength(List<Integer> tour) {
        double length = 0.0;
        int lastIndex = tour.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            length += distances[tour.get(i)][tour.get(i + 1)];
        }
        length += distances[tour.get(lastIndex)][tour.get(0)]; // Complete the loop back to the depot
        return length;
    }

    public PlannerResult result() {
        return new PlannerResult(tournees, longTournees);
    }
}
