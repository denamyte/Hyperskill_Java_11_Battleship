package battleship;

import battleship.BattleshipGame.PlaceShipResult;

import java.util.Arrays;

public class BattleshipPlayer {
    private static final String FOG = "~";
    private static final String SHIP = "0";
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

        // TODO: 4/16/21 Implement
        //  - parse rawCrd
        //  - detect errors from the simplest (wrong ship size) to the hardest (too close)

        return PlaceShipResult.NO_ERROR;
    }

    @Override
    public String toString() {
        return render();
    }
}
