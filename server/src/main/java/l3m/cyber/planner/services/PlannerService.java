package l3m.cyber.planner.services;

import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.utils.PartitionAlea;
import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    public PlannerResult getResult(PlannerParameter params) {
        // Calculer le nombre de locations basé sur la longueur de la dimension de la matrice
        int numberOfLocations = params.matrix().length; // Nombre de sommets dans la matrice
        
        // Créer une instance de PartitionAlea avec le nombre de locations, le nombre de livreurs et l'indice du dépôt
        PartitionAlea partition = new PartitionAlea(numberOfLocations, params.k(), params.start());
        
        // Créer un Planner avec les paramètres et la partition
        Planner pl = new Planner(params, partition);
        
        // Obtenir le résultat du planner
        return pl.result();
    }
}
