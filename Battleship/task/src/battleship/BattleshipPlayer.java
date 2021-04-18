package battleship;

import battleship.BattleshipGame.PlaceShipResult;

import java.util.Arrays;

public class BattleshipPlayer {
    private static final String FOG = "~";
    private static final String SHIP = "O";
    private static final String HIT = "X";
    private static final String MISS = "M";
    private static final String UPPER_ROW = "  1 2 3 4 5 6 7 8 9 10\n";

    private static final int SIZE = 10;

    private String[][] data;

    public BattleshipPlayer() {
        setupData();
    }

    private void setupData() {
        data = new String[SIZE][];
        data[0] = new String[SIZE];
        Arrays.fill(data[0], FOG);
        for (int i = 1; i < SIZE; i++) {
            data[i] = data[0].clone();
        }
    }

    public String render() {
        StringBuilder sb = new StringBuilder(UPPER_ROW);
        char letter = 'A';
        for (String[] row : data) {
            sb.append(letter++).append(' ');
            sb.append(String.join(" ", row));
            sb.append('\n');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    public PlaceShipResult placeShip(int size, String rawCrd) {
        final int[][] crd = parseCoordinates(rawCrd);
        // Check if the coordinates are not 1 cell wide
        if (crd[0][0] != crd[1][0] && crd[0][1] != crd[1][1]) {
            return PlaceShipResult.WRONG_LOCATION;
        }

        sortCoordinates(crd);

        // Check if the ship length is the same as it needs to be
        if ((crd[1][0] - crd[0][0] + 1) * (crd[1][1] - crd[0][1] + 1) != size) {
            return PlaceShipResult.WRONG_LENGTH;
        }

        // Check if the ship overlaps another one
        if (shipOverlapsWithAnother(crd)) {
            return PlaceShipResult.WRONG_LOCATION;
        }

        // Check if the ship is too close to another one
        if (isShipTooCloseToAnother(crd)) {
            return PlaceShipResult.TOO_CLOSE;
        }

        // At this point the ship is of needed size and should be located at the correct coordinates
        placeSymbolToRange(SHIP, crd[0][0], crd[1][0], crd[0][1], crd[1][1]);
        return PlaceShipResult.NO_ERROR;
    }

    /** Parse coordinates and return them in the following format: [[row1, col1], [row2, col2]] */
    private int[][] parseCoordinates(String rawCrd) {
        return Arrays.stream(rawCrd.split("\\s+"))
                .map(s -> new int[]{s.charAt(0) - 'A', Integer.parseInt(s.substring(1)) - 1})
                .toArray(int[][]::new);
    }

    /** Sort coordinates in ascending order */
    private void sortCoordinates(int[][] crd) {
        if (crd[0][0] > crd[1][0] || crd[0][1] > crd[1][1]) {
            int[] tmp = crd[0];
            crd[0] = crd[1];
            crd[1] = tmp;
        }
    }

    private boolean shipOverlapsWithAnother(int[][] crd) {
        return symbolDetected(SHIP, crd[0][0], crd[1][0], crd[0][1], crd[1][1]);
    }

    private boolean isShipTooCloseToAnother(int[][] crd) {
        int rowLo = stickCrdToRange(crd[0][0] - 1);
        int rowHi = stickCrdToRange(crd[1][0] + 1);
        int colLo = stickCrdToRange(crd[0][1] - 1);
        int colHi = stickCrdToRange(crd[1][1] + 1);
        return symbolDetected(SHIP, rowLo, rowHi, colLo, colHi);
    }

    private int stickCrdToRange(int i) {
        return i < 0 ? 0 : i >= SIZE ? SIZE - 1 : i;
    }

    private boolean symbolDetected(String symbol, int rowLo, int rowHi, int colLo, int colHi) {
        for (int y = rowLo; y <= rowHi; y++) {
            for (int x = colLo; x <= colHi; x++) {
                if (symbol.equals(data[y][x])) {
                    return true;
                }
            }
        }
        return false;
    }

    private void placeSymbolToRange(String symbol, int rowLo, int rowHi, int colLo, int colHi) {
        for (int y = rowLo; y <= rowHi; y++) {
            for (int x = colLo; x <= colHi; x++) {
                data[y][x] = symbol;
            }
        }
    }

    @Override
    public String toString() {
        return render();
    }
}
