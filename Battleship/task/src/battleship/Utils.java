package battleship;

import java.util.*;

public class Utils {

    public static final Map<String, Integer> SHIP_NAME_TO_SIZE_MAP = Collections.unmodifiableMap(
            new LinkedHashMap<>() {{
                put("Aircraft Carrier", 5);
                put("Battleship", 4);
                put("Submarine", 3);
                put("Cruiser", 3);
                put("Destroyer", 2);
            }});

    public static final String FOG = "~";
    public static final String SHIP = "O";
    public static final String HIT = "X";
    public static final String MISS = "M";

    private static final int SIZE = 10;

    public enum PlaceShipResult {
        NO_ERROR, WRONG_LENGTH, WRONG_LOCATION, TOO_CLOSE
    }

    public enum ShotResult {
        HIT, SANK, SANK_LAST, MISSED, WRONG_COORDINATES
    }

    public static final Map<PlaceShipResult, String> PLACE_SHIP_RESULT_MAP = Map.of(
            PlaceShipResult.WRONG_LENGTH, "\nError! Wrong length of the %s! Try again:\n",
            PlaceShipResult.WRONG_LOCATION, "\nError! Wrong ship location! Try again:\n",
            PlaceShipResult.TOO_CLOSE, "\nError! You placed it too close to another one. Try again:\n"
    );

    public static final Map<ShotResult, String> SHOT_MESSAGE_MAP = Map.of(
            ShotResult.HIT, "\nYou hit a ship!",
            ShotResult.MISSED, "\nYou missed",
            ShotResult.SANK, "\nYou sank a ship!",
            ShotResult.SANK_LAST, "\nYou sank the last ship. You won. Congratulations!",
            ShotResult.WRONG_COORDINATES, "\nError! You entered the wrong coordinates! Try again:\n"
    );

    public static final Map<ShotResult, String> RIVAL_DATA_MARK_MAP = Map.of(
            ShotResult.HIT, HIT,
            ShotResult.SANK, HIT,
            ShotResult.SANK_LAST, HIT,
            ShotResult.MISSED, MISS
    );

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
                .map(Utils::parseCoordinates)
                .toArray(int[][]::new);
    }

    public static int[] parseShotCoordinates(String rawCrd) {
        return parseCoordinates(rawCrd);
    }

    private static int[] parseCoordinates(String s) {
        return new int[]{s.charAt(0) - 'A', Integer.parseInt(s.substring(1)) - 1};
    }

    public static boolean coordinatesNotInRange(int[] crd) {
        return !Arrays.stream(crd).mapToObj(i -> i >= 0 && i < SIZE).allMatch(b -> b);
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

    public static void placeShipIntoField(final String[][] data, int[][] crd) {
        placeSymbolToRange(data, SHIP, crd[0][0], crd[1][0], crd[0][1], crd[1][1]);
    }

    public static void placeSymbolToRange(final String[][] data, String symbol, int rowLo, int rowHi, int colLo, int colHi) {
        for (int y = rowLo; y <= rowHi; y++) {
            for (int x = colLo; x <= colHi; x++) {
                data[y][x] = symbol;
            }
        }
    }

    public static void markRivalData(final String[][] rivalData, ShotResult result, int[] crd) {
        final String mark = RIVAL_DATA_MARK_MAP.getOrDefault(result, "");
        if (!mark.isEmpty()) {
            rivalData[crd[0]][crd[1]] = mark;
        }
    }

    public static String shoot(final String[][] data, int[] crd) {
        final String symbol = data[crd[0]][crd[1]];
        data[crd[0]][crd[1]] = symbol.equals(SHIP) || symbol.equals(HIT) ? HIT : MISS;
        return symbol;
    }

}
