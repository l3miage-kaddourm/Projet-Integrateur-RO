package l3m.cyber.planner;

import l3m.cyber.planner.utils.Graphe;
import l3m.cyber.planner.utils.PartitionKCentre;
import l3m.cyber.planner.utils.Planner;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlannerApplicationTests {

    @Test
    void testKruskalTSP() {
        // Matrice de distances pour un graphe complet
        Double[][] distances = {
            {0.0, 2.0, 3.0, 1.0},
            {2.0, 0.0, 4.0, 5.0},
            {3.0, 4.0, 0.0, 8.0},
            {1.0, 5.0, 8.0, 0.0}
        };

        // Création du graphe
        Graphe graphe = new Graphe(distances.length);
        for (int i = 0; i < distances.length; i++) {
            for (int j = i + 1; j < distances[i].length; j++) {
                graphe.ajouterArete(i, j, distances[i][j]);
            }
        }

        // Application de Kruskal pour obtenir l'arbre couvrant minimal
        Graphe mst = graphe.kruskal();

        // Utilisation du Planner avec la matrice de distances, k et le début
        Planner planner = new Planner(distances, 2, 0);

        // Partition et optimisation des tournées
        planner.divise();

        // On simule le TSP en partant du premier sommet de l'MST
        ArrayList<Integer> tournee = mst.tsp(0);
        double longueurTournee = planner.calculeLongTournees(tournee); // Utiliser Graphe pour calculer la longueur

        // Affichage des résultats
        System.out.println("Arbre couvrant minimal (MST) via Kruskal: ");
        System.out.println(mst);
        System.out.println("Tour TSP approximé à partir de l'MST: " + tournee);
        System.out.println("Longueur de la tournée TSP approximée: " + longueurTournee);

        // Vérification des résultats
        assertNotNull(tournee);
        assertTrue(tournee.size() > 1); // Doit contenir au moins 2 sommets si connecté
        assertTrue(longueurTournee > 0); // La longueur doit être positive
    }
}
