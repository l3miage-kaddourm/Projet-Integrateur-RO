package l3m.cyber.planner.services;

import l3m.cyber.planner.utils.Planner;
import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    public PlannerResult getResult(PlannerParameter params) {
        // Création de l'instance de Planner avec les paramètres fournis
        Planner planner = new Planner(params.matrix(), params.k(), params.start());

        // La méthode divise() du planner effectue la partition et optimise les tournées
        planner.divise();

        // Compilation des résultats des tournées optimisées
        return planner.result();
    }
}
