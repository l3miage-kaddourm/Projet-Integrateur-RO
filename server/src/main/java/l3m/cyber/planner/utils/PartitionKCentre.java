package l3m.cyber.planner.utils;

public class PartitionKCentre extends Partition {

    public PartitionKCentre(int n, int k) {
        super(n, k);
    }

    @Override
    public void partitionne(Double[][] distances) {
        int[] casernes = new int[k];
        casernes[0] = elemSpecial;

        int[] caserneRef = new int[nbElem];
        double[] distanceMax = new double[nbElem];

        for (int i = 0; i < nbElem; i++) {
            caserneRef[i] = 0;
            distanceMax[i] = distances[elemSpecial][elems.get(i)];
        }

        for (int i = 1; i < k; i++) {
            int sommetEloigneMax = selectFarthestVertex(distanceMax);
            casernes[i] = elems.get(sommetEloigneMax);
            updateDistances(distances, sommetEloigneMax, distanceMax, caserneRef, i);
        }

        for (int i = 1; i < nbElem; i++) {
            parties.get(caserneRef[i]).add(elems.get(i));
        }
    }

    private int selectFarthestVertex(double[] distanceMax) {
        int sommetEloigneMax = 0;
        for (int j = 1; j < nbElem; j++) {
            if (distanceMax[j] > distanceMax[sommetEloigneMax]) {
                sommetEloigneMax = j;
            }
        }
        return sommetEloigneMax;
    }

    private void updateDistances(Double[][] distances, int sommetEloigneMax, double[] distanceMax, int[] caserneRef,
            int caserneIndex) {
        for (int l = 0; l < nbElem; l++) {
            double dist = distances[elems.get(sommetEloigneMax)][elems.get(l)];
            if (dist < distanceMax[l]) {
                distanceMax[l] = dist;
                caserneRef[l] = caserneIndex;
            }
        }
    }
}