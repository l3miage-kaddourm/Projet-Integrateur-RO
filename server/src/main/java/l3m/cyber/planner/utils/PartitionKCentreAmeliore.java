package l3m.cyber.planner.utils;

import java.util.ArrayList;

public class PartitionKCentreAmeliore extends Partition {

    private int[] casernes;

    public PartitionKCentreAmeliore(int n, int k) {
        super(n, k);
    }

    @Override
    public void partitionne(Double[][] distances) {
        casernes = new int[k];
        casernes[0] = elemSpecial;
        int[] caserneRef = new int[nbElem];
        double[] distanceMax = new double[nbElem];

        initialiserVariables(casernes, caserneRef, distanceMax, distances);
        selectionnerCasernes(casernes, caserneRef, distanceMax, distances);
        distribuerElements(caserneRef);
    }

    private void initialiserVariables(int[] casernes, int[] caserneRef, double[] distanceMax, Double[][] distances) {
        for (int i = 0; i < nbElem; i++) {
            caserneRef[i] = 0;
            distanceMax[i] = distances[elemSpecial][elems.get(i)];
        }
    }

    private void selectionnerCasernes(int[] casernes, int[] caserneRef, double[] distanceMax, Double[][] distances) {
        for (int i = 1; i < k; i++) {
            int sommetEloigneMax = trouverSommetEloigneMax(distanceMax);
            casernes[i] = elems.get(sommetEloigneMax);
            mettreAJourDistances(caserneRef, distanceMax, distances, sommetEloigneMax, i);
        }
    }

    private int trouverSommetEloigneMax(double[] distanceMax) {
        int sommetEloigneMax = 0;
        for (int j = 1; j < nbElem; j++) {
            if (distanceMax[j] > distanceMax[sommetEloigneMax]) {
                sommetEloigneMax = j;
            }
        }
        return sommetEloigneMax;
    }

    private void mettreAJourDistances(int[] caserneRef, double[] distanceMax, Double[][] distances,
            int sommetEloigneMax, int caserneIndex) {
        for (int l = 0; l < nbElem; l++) {
            double dist = distances[elems.get(sommetEloigneMax)][elems.get(l)];
            if (dist < distanceMax[l]) {
                distanceMax[l] = dist;
                caserneRef[l] = caserneIndex;
            }
        }
    }

    private void distribuerElements(int[] caserneRef) {
        for (int i = 1; i < nbElem; i++) {
            parties.get(caserneRef[i]).add(elems.get(i));
        }
    }

    public void ameliorerPartition(Double[][] distances) {
        for (int i = 0; i < k; i++) {
            ArrayList<Integer> partie = parties.get(i);
            for (int j = 1; j < partie.size(); j++) {
                int elem = partie.get(j);
                double minDistance = Double.MAX_VALUE;
                int meilleureCaserne = i;
                for (int m = 0; m < k; m++) {
                    double distance = distances[casernes[m]][elem];
                    if (distance < minDistance) {
                        minDistance = distance;
                        meilleureCaserne = m;
                    }
                }
                if (meilleureCaserne != i) {
                    partie.remove(j);
                    parties.get(meilleureCaserne).add(elem);
                    j--;
                }
            }
        }
    }

}
