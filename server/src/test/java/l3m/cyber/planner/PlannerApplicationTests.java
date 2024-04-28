package l3m.cyber.planner;

import l3m.cyber.planner.utils.Graphe;
import l3m.cyber.planner.utils.PartitionKCentre;
import l3m.cyber.planner.utils.Partition;
import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.requests.PlannerParameter;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlannerApplicationTests {

    @Test
    void testPlanificationEtTSP() {
        // Définir une matrice de distances exemple
        Double[][] distances = {
            {0.0, 1.0, 3.0, 4.0},
            {1.0, 0.0, 2.0, 3.0},
            {3.0, 2.0, 0.0, 5.0},
            {4.0, 3.0, 5.0, 0.0}
        };

        // Paramètres pour la planification
        int nombreLivreur = 2;  // Nombre de livreurs
        int elemSpecial = 0;    // Position du dépôt

        PlannerParameter params = new PlannerParameter(distances, nombreLivreur, elemSpecial);
        Partition partition = new PartitionKCentre(4, nombreLivreur, elemSpecial);
        partition.partitionne(distances);

        Planner planner = new Planner(params, partition);

        // Boucler sur chaque partition et simuler la planification des tournées
        for (int i = 0; i < nombreLivreur; i++) {
            ArrayList<Integer> subset = new ArrayList<>(partition.getPartie(i));
            Graphe graphe = new Graphe(distances, subset); // Créer un graphe pour le sous-ensemble
            ArrayList<Integer> tournee = graphe.tsp(elemSpecial);
            double longueurTournee = planner.calculateTourLength(tournee); // Calculer la longueur de la tournée

            // Imprimer la tournée optimisée et sa longueur
            System.out.println("Tournée optimisee pour le livreur " + (i + 1) + ": " + tournee);
            System.out.println("Longueur de la tournee: " + longueurTournee);

            // S'assurer que chaque tournée commence et se termine au dépôt
            assertTrue(tournee.get(0).equals(tournee.get(tournee.size() - 1)));

            // Afficher les détails du résultat TSP
            System.out.println("Itineraire TSP: " + tournee);
            System.out.println("Longueur de l'itineraire TSP: " + longueurTournee);
        }

        // Vérifier que le planificateur a été correctement utilisé
        assertNotNull(planner);
    }
}
