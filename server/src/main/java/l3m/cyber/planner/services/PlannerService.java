package l3m.cyber.planner.services;

import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.utils.Partition;
import l3m.cyber.planner.utils.PartitionKCentre;
import l3m.cyber.planner.utils.Graphe;
import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    public PlannerResult getResult(PlannerParameter params) {
        // Création d'une instance de PartitionKCentre
        Partition partition = new PartitionKCentre(params.matrix().length, params.k(), params.start());

        // Créer un Planner avec les paramètres et la partition
        Planner planner = new Planner(params, partition);

        // Appliquer le TSP sur chaque sous-ensemble défini par la partition
        ArrayList<ArrayList<Integer>> optimizedTours = new ArrayList<>();
        ArrayList<Double> tourLengths = new ArrayList<>();
        
        // Partitionner les données
        partition.partitionne(params.matrix());

        for (List<Integer> subset : partition.getParties()) {
            Graphe graphe = new Graphe(params.matrix(), new ArrayList<>(subset)); // Créer un graphe avec les données et le sous-ensemble
            ArrayList<Integer> tspTour = graphe.tsp(params.start());
            optimizedTours.add(tspTour);
            double length = planner.calculateTourLength(tspTour); // Utiliser la méthode de Planner pour calculer la longueur
            tourLengths.add(length);
        }

        // Retourner le résultat avec les tournées optimisées et leurs longueurs
        return new PlannerResult(optimizedTours, tourLengths);
    }
}
