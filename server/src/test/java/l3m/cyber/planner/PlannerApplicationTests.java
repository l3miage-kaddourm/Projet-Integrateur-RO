package l3m.cyber.planner;

import l3m.cyber.planner.utils.PartitionKCentre;
import l3m.cyber.planner.utils.PartitionKCentreAmeliore;
import l3m.cyber.planner.utils.Planner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlannerApplicationTests {

    private Double[][] distances;
    private ArrayList<Integer> elems;
    private int nbElem;
    private int k;
    private int elemSpecial;

    @BeforeEach
    public void setUp() {
        // Initialisation des données de test
        nbElem = 7;// nombre de sommet dans le graphe
        k = 2; // nombre de partition
        elemSpecial = 0;// debut

        // Création des éléments
        elems = new ArrayList<>();
        for (int i = 0; i < nbElem; i++) {
            elems.add(i);
        }

        // Utilisation de la matrice de distances préremplie
        distances = new Double[][] {
                { 0.0, 2.5, 3.0, 0.1, 17.0, 15.5, 8.2 },
                { 2.5, 0.0, 1.0, 5.25, 18.0, 3.5, 12.0 },
                { 3.0, 1.0, 0.0, 0.0, 3.4, 9.9, 14.0 },
                { 0.1, 5.25, 0.0, 0.0, 7.7, 8.8, 6.8 },
                { 17.0, 18.0, 3.4, 7.7, 0.0, 2.0, 2.2 },
                { 15.5, 3.5, 9.9, 8.8, 2.0, 0.0, 3.3 },
                { 8.2, 12.0, 14.0, 6.8, 2.2, 3.3, 0.0 }
        };
    }

    // test de parition k centre
    @Test
    public void testPartitionne() {

        PartitionKCentre partitionKCentre = new PartitionKCentre(distances.length, k); // Instanciation manuelle

        partitionKCentre.partitionne(distances);

        // Vérifier les partitions attendues
        ArrayList<ArrayList<Integer>> expectedPartitions = new ArrayList<>();
        expectedPartitions.add(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
        expectedPartitions.add(new ArrayList<>(Arrays.asList(0, 4, 5, 6)));

        assertEquals(expectedPartitions, partitionKCentre.getParties(), "Les partitions ne sont pas correctes");
    }

    @Test
    public void testPartitionKCentreAmeliore() {
        // Création de l'objet PartitionKCentreAmeliore
        PartitionKCentreAmeliore partitionKCentreAmeliore = new PartitionKCentreAmeliore(nbElem, k);

        // Appel de la méthode partitionne
        partitionKCentreAmeliore.partitionne(distances);

        // Récupération des partitions
        ArrayList<ArrayList<Integer>> partitions = partitionKCentreAmeliore.getParties();

        // Vérification du nombre de partitions
        assertEquals(k, partitions.size(), "Le nombre de partitions doit être égal à " + k);

        // Vérification que chaque élément est présent dans une et une seule partition
        boolean[] elemPresent = new boolean[nbElem];
        for (ArrayList<Integer> partition : partitions) {
            for (Integer elem : partition) {
                elemPresent[elem] = true;
            }
        }

        for (int i = 0; i < nbElem; i++) {
            assertTrue(elemPresent[i], "L'élément " + i + " doit être présent dans une partition");
        }

        // Vérification que l'élément spécial est dans chaque partition
        for (ArrayList<Integer> partition : partitions) {
            assertTrue(partition.contains(elemSpecial), "L'élément spécial doit être présent dans chaque partition");
        }
        System.out.println("PartitionKCentreAmeliore: " + partitions);
    }

    @Test
    public void testTSP() {

        Planner planner = new Planner(distances, k, elemSpecial);
        planner.divise(); // Perform partitioning and TSP calculations
        planner.calculeTournees();
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
}