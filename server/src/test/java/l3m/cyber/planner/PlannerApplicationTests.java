package l3m.cyber.planner;

import l3m.cyber.planner.requests.PlannerParameter;
import l3m.cyber.planner.responses.PlannerResult;
import l3m.cyber.planner.utils.PartitionAlea;
import l3m.cyber.planner.utils.Planner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlannerApplicationTests {

    @Test
    void plannerCreatesValidTours() {
        Double[][] distances = {
            {0.0, 1.0, 2.0, 3.0},
            {1.0, 0.0, 1.0, 2.0},
            {2.0, 1.0, 0.0, 1.0},
            {3.0, 2.0, 1.0, 0.0}
        };
        PlannerParameter parameter = new PlannerParameter(distances, 2, 0);
        PartitionAlea partition = new PartitionAlea(distances.length, 2, 0);
        Planner planner = new Planner(parameter, partition);
        PlannerResult result = planner.result();

        assertNotNull(result);
        assertEquals(2, result.tournees().size());
        assertTrue(result.tournees().stream().allMatch(tournee -> tournee.get(0) == 0 && tournee.get(tournee.size() - 1) == 0));

        result.tournees().forEach(tournee -> {
            assertEquals(0, tournee.get(0));
            assertEquals(0, tournee.get(tournee.size() - 1));
        });
    }
}
