package battleship;

import battleship.Utils.PlaceShipResult;
import battleship.Utils.ShotResult;

import java.util.*;

import static battleship.Utils.PLACE_SHIP_RESULT_MAP;
import static battleship.Utils.SHOT_RESULT_MAP;
import static battleship.Utils.SHIP_NAME_TO_SIZE_MAP;

public class BattleshipGame {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String ENTER_PROMPT = "\nEnter the coordinates of the %s (%d cells):\n\n";

    public BattleshipGame() {
        BattleshipPlayer player = new BattleshipPlayer();
        inputShipsForPlayer(player);
        System.out.println("\nThe game starts!\n");
        System.out.println(player);
        takeOneShot(player);
    }

    private static void inputShipsForPlayer(BattleshipPlayer player) {
        System.out.println(player);
        SHIP_NAME_TO_SIZE_MAP.forEach((name, size) -> {
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
            case WRONG_LENGTH: return String.format(PLACE_SHIP_RESULT_MAP.get(placeShipResult), shipName);
            case NO_ERROR:
            case WRONG_LOCATION:
            case TOO_CLOSE:
            default: return PLACE_SHIP_RESULT_MAP.getOrDefault(placeShipResult, "");
        }
    }

    private static void takeOneShot(BattleshipPlayer player) {
        System.out.println("\nTake a shot!\n");
        ShotResult result;

        do {
            result = player.takeShot(scanner.nextLine());
            if (!result.equals(ShotResult.WRONG_COORDINATES)) {
                System.out.println("\n" + player);
            }
            System.out.println(SHOT_RESULT_MAP.get(result));

        } while (result.equals(ShotResult.WRONG_COORDINATES));
    }
}
