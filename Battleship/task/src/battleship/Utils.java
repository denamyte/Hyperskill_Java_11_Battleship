package battleship;

import java.util.Arrays;

public class Utils {

    private static final String FOG = "~";
    private static final String SHIP = "O";
    private static final String HIT = "X";
    private static final String MISS = "M";

    private static final int SIZE = 10;

    public static String[][] createInitialField() {
        String[][] data = new String[SIZE][];
        data[0] = new String[SIZE];
        Arrays.fill(data[0], FOG);
        for (int i = 1; i < SIZE; i++) {
            data[i] = data[0].clone();
        }
        return data;
    }

    /** Parse coordinates and return them in the following format: [[row1, col1], [row2, col2]];
     * the coordinates are 0-based. */
    public static int[][] parseShipCoordinates(String rawCrd) {
        return Arrays.stream(rawCrd.split("\\s+"))
                .map(s -> new int[]{s.charAt(0) - 'A', Integer.parseInt(s.substring(1)) - 1})
                .toArray(int[][]::new);
    }

    /** Sort coordinates in ascending order */
    public static void sortShipCoordinates(int[][] crd) {
        if (crd[0][0] > crd[1][0] || crd[0][1] > crd[1][1]) {
            int[] tmp = crd[0];
            crd[0] = crd[1];
            crd[1] = tmp;
        }
    }

    public static boolean shipOverlapsWithAnother(String[][] data, int[][] crd) {
        return Utils.symbolDetected(data, SHIP, crd[0][0], crd[1][0], crd[0][1], crd[1][1]);
    }

    public static boolean isShipTooCloseToAnother(final String[][] data, int[][] crd) {
        int rowLo = stickCrdToRange(crd[0][0] - 1);
        int rowHi = stickCrdToRange(crd[1][0] + 1);
        int colLo = stickCrdToRange(crd[0][1] - 1);
        int colHi = stickCrdToRange(crd[1][1] + 1);
        return Utils.symbolDetected(data, SHIP, rowLo, rowHi, colLo, colHi);
    }

    private static int stickCrdToRange(int i) {
        return i < 0 ? 0 : i >= SIZE ? SIZE - 1 : i;
    }

    public static boolean symbolDetected(final String[][] data, String symbol, int rowLo, int rowHi, int colLo, int colHi) {
        for (int y = rowLo; y <= rowHi; y++) {
            for (int x = colLo; x <= colHi; x++) {
                if (symbol.equals(data[y][x])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void placeShipIntoField(final String data[][], int[][] crd) {
        placeSymbolToRange(data, SHIP, crd[0][0], crd[1][0], crd[0][1], crd[1][1]);
    }

    public static void placeSymbolToRange(final String data[][], String symbol, int rowLo, int rowHi, int colLo, int colHi) {
        for (int y = rowLo; y <= rowHi; y++) {
            for (int x = colLo; x <= colHi; x++) {
                data[y][x] = symbol;
            }
        }
    }

}
