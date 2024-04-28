package l3m.cyber.planner.services;

import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.utils.Partition;
import l3m.cyber.planner.utils.PartitionKCentre;  
import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    public PlannerResult getResult(PlannerParameter params) {
        // Création d'une instance de PartitionKCentre ici, à l'intérieur de la méthode
        Partition partition = new PartitionKCentre(params.matrix().length, params.k(), params.start());

        // Créer un Planner avec les paramètres et la partition
        Planner pl = new Planner(params, partition);

        // Obtenir le résultat du planner
        return pl.result();
    }
}
