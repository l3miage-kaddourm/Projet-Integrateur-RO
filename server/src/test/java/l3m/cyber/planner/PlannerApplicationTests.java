package l3m.cyber.planner;

import l3m.cyber.planner.utils.Graphe;
import l3m.cyber.planner.utils.PartitionKCentre;
import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.utils.Triplet;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlannerApplicationTests {
    @Test
    public void testTSP() {
        Double[][] distances = {
                { 0.0, 2.5, 3.0, 0.1, 17.0, 15.5, 8.2 },
                { 2.5, 0.0, 1.0, 5.25, 18.0, 3.5, 12.0 },
                { 3.0, 1.0, 0.0, 0.0, 3.4, 9.9, 14.0 },
                { 0.1, 5.25, 0.0, 0.0, 7.7, 8.8, 6.8 },
                { 17.0, 18.0, 3.4, 7.7, 0.0, 2.0, 2.2 },
                { 15.5, 3.5, 9.9, 8.8, 2.0, 0.0, 3.3 },
                { 8.2, 12.0, 14.0, 6.8, 2.2, 3.3, 0.0 }
        };
        int k = 2; // Number of partitions
        int debut = 0; // Starting city for TSP

        Planner planner = new Planner(distances, k, debut);
        planner.divise(); // Perform partitioning and TSP calculations
        planner.calculeTournee();
        planner.calculeLongTournees();

        ArrayList<ArrayList<Integer>> tournees = planner.getTournees();
        ArrayList<Double> longTournees = planner.getLongTournees();
        assertNotNull(tournees);
        assertNotNull(longTournees);
        assertEquals(k, tournees.size()); // Check if the number of tours matches the number of partitions
        assertEquals(k, longTournees.size());
        // Print out the tours for visual inspection
        for (ArrayList<Integer> tournee : tournees) {
            System.out.println("Tour: " + tournee);
        }
        System.out.println("lontournee: " + longTournees);
    }

    @Test
    public void testPartitionne() {
        Double[][] distances = {
                { 0.0, 2.5, 3.0, 0.1, 17.0, 15.5, 8.2 },
                { 2.5, 0.0, 1.0, 5.25, 18.0, 3.5, 12.0 },
                { 3.0, 1.0, 0.0, 0.0, 3.4, 9.9, 14.0 },
                { 0.1, 5.25, 0.0, 0.0, 7.7, 8.8, 6.8 },
                { 17.0, 18.0, 3.4, 7.7, 0.0, 2.0, 2.2 },
                { 15.5, 3.5, 9.9, 8.8, 2.0, 0.0, 3.3 },
                { 8.2, 12.0, 14.0, 6.8, 2.2, 3.3, 0.0 }
        };

        PartitionKCentre partitionKCentre = new PartitionKCentre(distances.length, 2); // Instanciation manuelle

        partitionKCentre.partitionne(distances);

        // VÃ©rifier les partitions attendues
        ArrayList<ArrayList<Integer>> expectedPartitions = new ArrayList<>();
        expectedPartitions.add(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
        expectedPartitions.add(new ArrayList<>(Arrays.asList(0, 4, 5, 6)));

        assertEquals(expectedPartitions, partitionKCentre.getParties(), "Les partitions ne sont pas correctes");
    }

}