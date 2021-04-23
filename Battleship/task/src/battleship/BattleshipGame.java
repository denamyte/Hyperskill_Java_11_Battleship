package battleship;

import battleship.Utils.PlaceShipResult;
import battleship.Utils.ShotResult;

import java.util.*;

import static battleship.Utils.PLACE_SHIP_RESULT_MAP;
import static battleship.Utils.SHOT_MESSAGE_MAP;
import static battleship.Utils.SHIP_NAME_TO_SIZE_MAP;

public class BattleshipGame {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String ENTER_PROMPT = "\nEnter the coordinates of the %s (%d cells):\n\n";

    public BattleshipGame() {
        BattleshipPlayer player1 = new BattleshipPlayer();
        BattleshipPlayer player2 = new BattleshipPlayer();
        inputShipsForPlayer(player1);
        System.out.println("\nThe game starts!\n");
        System.out.println(player2.renderRival());
        takeOneShot(player1, player2);
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

    private static void takeOneShot(BattleshipPlayer shotPlayer, BattleshipPlayer shooterPlayer) {
        System.out.println("\nTake a shot!\n");
        ShotResult shotResult;

        do {
            shotResult = shooterPlayer.shoot(shotPlayer, scanner.nextLine());
            final String shotMsg = SHOT_MESSAGE_MAP.get(shotResult);
            if (shotResult.equals(ShotResult.WRONG_COORDINATES)) {
                System.out.println(shotMsg);
            } else {
                System.out.println("\n" + shooterPlayer.renderRival());
                System.out.println(shotMsg);
                System.out.println(shotPlayer);
            }

        } while (shotResult.equals(ShotResult.WRONG_COORDINATES));
    }
}
