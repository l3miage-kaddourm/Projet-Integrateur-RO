package l3m.cyber.planner.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class PartitionKCentre extends Partition {

    public PartitionKCentre(ArrayList<Integer> elems, int k, int elemSpecial) {
        super(elems, k, elemSpecial);
    }

    public PartitionKCentre(int n, int k, int elemSpecial) {
        super(n, k, elemSpecial);
    }

    public PartitionKCentre(int n, int k) {
        super(n, k);
    }

    @Override
    public void partitionne(Double[][] distance) {
        int n = this.elems.size();
        ArrayList<Integer> centres = new ArrayList<>();
        int[] closestCentre = new int[n];
        double[] minDistanceToCentre = new double[n];
        Arrays.fill(minDistanceToCentre, Double.MAX_VALUE);

        // Initialiser avec l'élément spécial comme premier centre
        centres.add(this.elemSpecial);
        updateDistances(distance, closestCentre, minDistanceToCentre, this.elemSpecial);

        // Choisir les k centres
        for (int i = 1; i < this.k; i++) {
            int newCentre = selectFarthestVertex(minDistanceToCentre);
            centres.add(newCentre);
            updateDistances(distance, closestCentre, minDistanceToCentre, newCentre);
        }

        // Remplir les parties selon les centres les plus proches
        for (int centreIndex = 0; centreIndex < centres.size(); centreIndex++) {
            parties.get(centreIndex).clear();
            parties.get(centreIndex).add(this.elemSpecial); // Ajout de l'élément spécial
            int centre = centres.get(centreIndex);
            for (int vertexIndex = 0; vertexIndex < n; vertexIndex++) {
                if (closestCentre[vertexIndex] == centre) {
                    parties.get(centreIndex).add(this.elems.get(vertexIndex));
                }
            }
        }
    }

    private void updateDistances(Double[][] distance, int[] closestCentre, double[] minDistanceToCentre,
            int newCentre) {
        for (int i = 0; i < distance.length; i++) {
            if (distance[i][newCentre] < minDistanceToCentre[i]) {
                minDistanceToCentre[i] = distance[i][newCentre];
                closestCentre[i] = newCentre;
            }
        }
    }

    private int selectFarthestVertex(double[] minDistanceToCentre) {
        double maxDistance = -1;
        int farthestVertex = 0;
        for (int i = 0; i < minDistanceToCentre.length; i++) {
            if (minDistanceToCentre[i] > maxDistance) {
                maxDistance = minDistanceToCentre[i];
                farthestVertex = i;
            }
        }
        return farthestVertex;
    }
}
