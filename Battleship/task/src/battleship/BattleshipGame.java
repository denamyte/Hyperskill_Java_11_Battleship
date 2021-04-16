package battleship;

import java.util.Map;
import java.util.Scanner;

public class BattleshipGame {

    private static final Map<String, Integer> shipSizeMap = Map.of(
            "Aircraft Carrier", 5,
            "Battleship", 4,
            "Submarine", 3,
            "Cruiser", 3,
            "Destroyer", 2
    );

    enum PlaceShipResult {
        NO_ERROR, WRONG_LENGTH, WRONG_LOCATION, TOO_CLOSE
    }

    private static final Map<PlaceShipResult, String> PLACE_SHIP_ERRORS = Map.of(
            PlaceShipResult.WRONG_LENGTH, "\nError! Wrong length of the %s! Try again:\n",
            PlaceShipResult.WRONG_LOCATION, "\nError! Wrong ship location! Try again:\n",
            PlaceShipResult.TOO_CLOSE, "\nError! You placed it too close to another one. Try again:\n"
    );

    private static final String ENTER_PROMPT = "\nEnter the coordinates of the %s (%d cells):\n\n";

    Scanner scanner = new Scanner(System.in);
    private final BattleshipPlayer player;

    public BattleshipGame() {
        player = new BattleshipPlayer();
        System.out.println(player);
    }

    public void inputPlayerShips() {
        System.out.println(player);
        shipSizeMap.forEach((name, size) -> {
            PlaceShipResult result;

            System.out.printf(ENTER_PROMPT, name, size);

            do {
                result = player.placeShip(size, scanner.nextLine());
                tryPrintError(result, name);
            } while (!result.equals(PlaceShipResult.NO_ERROR));

            System.out.println();
            System.out.println(player);
        });
    }

    private void tryPrintError(PlaceShipResult placeShipResult, String shipName) {
        String error = getShipPlacementError(placeShipResult, shipName);
        if (!error.isEmpty()) {
            System.out.println(error);
        }
    }

    private String getShipPlacementError(PlaceShipResult placeShipResult, String shipName) {
        String result;
        switch (placeShipResult) {
            case WRONG_LENGTH: result = String.format(PLACE_SHIP_ERRORS.get(placeShipResult), shipName);
                break;
            case NO_ERROR:
            case WRONG_LOCATION:
            case TOO_CLOSE:
            default: result = PLACE_SHIP_ERRORS.getOrDefault(placeShipResult, "");
        }
        return result;
    }
}
