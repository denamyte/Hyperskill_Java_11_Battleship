package battleship;

import java.util.*;

public class Ship {
    private final Set<Integer> hitPoints;

    public Ship(int[] topLeftCrd, int[] btmRightCrd) {
        hitPoints = new HashSet<>();
        for (int y = topLeftCrd[0]; y <= btmRightCrd[0]; y++) {
            for (int x = topLeftCrd[1]; x <= btmRightCrd[1]; x++) {
                hitPoints.add(crdToHash(y, x));
            }
        }
    }

    public boolean hit(int[] crd) {
        return hitPoints.remove(crdToHash(crd[0], crd[1]));
    }

    public boolean sunken() {
        return hitPoints.isEmpty();
    }

    private int crdToHash(int y, int x) {
        return (y + 1) * 100 + x + 1;
    }
}
