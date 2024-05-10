package l3m.cyber.planner.utils;

import java.util.ArrayList;

public class Auxiliaire {
    // Génère une liste d'entiers de 0 à n-1
    public static ArrayList<Integer> integerList(int n) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }

    // Vérifie si une matrice générique est carrée
    public static <T> boolean estCarree(T[][] matrice) {
        if (matrice == null || matrice.length == 0) {
            return false; // Vérifie que la matrice n'est pas nulle et non vide
        }
        int length = matrice.length;
        for (T[] ligne : matrice) {
            if (ligne == null || ligne.length != length) {
                return false; // Vérifie que chaque ligne a la même longueur que le nombre de lignes
            }
        }
        return true;
    }

    // Vérifie si une matrice générique est carrée et symétrique
    public static <T> boolean estCarreeSym(T[][] matrice) {
        if (!estCarree(matrice)) {
            return false; // Utilise la méthode estCarree pour vérifier d'abord si la matrice est carrée
        }
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < i; j++) {
                if (!matrice[i][j].equals(matrice[j][i])) {
                    return false; // Vérifie la symétrie de la matrice
                }
            }
        }
        return true;
    }
}
