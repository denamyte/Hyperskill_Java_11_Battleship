package battleship;

import battleship.BattleshipGame.PlaceShipResult;

public class BattleshipPlayer {
    private static final String UPPER_ROW = "  1 2 3 4 5 6 7 8 9 10\n";


    private final String[][] data;

    public BattleshipPlayer() {
        data = Utils.createInitialField();
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
        final int[][] crd = Utils.parseShipCoordinates(rawCrd);
        // Check if the coordinates are not 1 cell wide
        if (crd[0][0] != crd[1][0] && crd[0][1] != crd[1][1]) {
            return PlaceShipResult.WRONG_LOCATION;
        }

        Utils.sortShipCoordinates(crd);

        // Check if the ship length is the same as it needs to be
        if ((crd[1][0] - crd[0][0] + 1) * (crd[1][1] - crd[0][1] + 1) != size) {
            return PlaceShipResult.WRONG_LENGTH;
        }

        if (Utils.shipOverlapsWithAnother(data, crd)) {
            return PlaceShipResult.WRONG_LOCATION;
        }

        if (Utils.isShipTooCloseToAnother(data, crd)) {
            return PlaceShipResult.TOO_CLOSE;
        }

        Utils.placeShipIntoField(data, crd);
        return PlaceShipResult.NO_ERROR;
    }

    @Override
    public String toString() {
        return render();
    }
}
