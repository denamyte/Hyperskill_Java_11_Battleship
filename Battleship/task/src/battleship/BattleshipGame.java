package battleship;

import java.util.*;

public class BattleshipGame {

    private static final Map<String, Integer> shipSizeMap = Collections.unmodifiableMap(
            new LinkedHashMap<>() {{
                put("Aircraft Carrier", 5);
                put("Battleship", 4);
                put("Submarine", 3);
                put("Cruiser", 3);
                put("Destroyer", 2);
            }});

    enum PlaceShipResult {
        NO_ERROR, WRONG_LENGTH, WRONG_LOCATION, TOO_CLOSE
    }

    private static final Map<PlaceShipResult, String> PLACE_SHIP_ERRORS = Map.of(
            PlaceShipResult.WRONG_LENGTH, "\nError! Wrong length of the %s! Try again:\n",
            PlaceShipResult.WRONG_LOCATION, "\nError! Wrong ship location! Try again:\n",
            PlaceShipResult.TOO_CLOSE, "\nError! You placed it too close to another one. Try again:\n"
    );

    private static final String ENTER_PROMPT = "\nEnter the coordinates of the %s (%d cells):\n\n";

    public BattleshipGame() {
        BattleshipPlayer player = new BattleshipPlayer();
        inputShipsForPlayer(player);
        System.out.println("\nThe game starts!\n");
        System.out.println(player);
    }

    public static void inputShipsForPlayer(BattleshipPlayer player) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(player);
        shipSizeMap.forEach((name, size) -> {
            PlaceShipResult result;

            System.out.printf(ENTER_PROMPT, name, size);

            do {
                result = player.placeShip(size, scanner.nextLine());
                tryPrintShipPlacementError(result, name);
            } while (!result.equals(PlaceShipResult.NO_ERROR));

            System.out.println();
            System.out.println(player);
        });
    }

    private static void tryPrintShipPlacementError(PlaceShipResult placeShipResult, String shipName) {
        String error = getShipPlacementError(placeShipResult, shipName);
        if (!error.isEmpty()) {
            System.out.println(error);
        }
    }

    private static String getShipPlacementError(PlaceShipResult placeShipResult, String shipName) {
        switch (placeShipResult) {
            case WRONG_LENGTH: return String.format(PLACE_SHIP_ERRORS.get(placeShipResult), shipName);
            case NO_ERROR:
            case WRONG_LOCATION:
            case TOO_CLOSE:
            default: return PLACE_SHIP_ERRORS.getOrDefault(placeShipResult, "");
        }
    }
}
