package battleship;

import battleship.Utils.PlaceShipResult;
import battleship.Utils.ShotResult;

import java.util.*;
import java.util.stream.Stream;

import static battleship.Utils.*;

public class BattleshipGame {

    private static final Scanner scanner = new Scanner(System.in);

    private static final String PLACE_SHIP_PROMPT = "%s, place your ships on the game field\n\n";
    private static final String ENTER_PROMPT = "\nEnter the coordinates of the %s (%d cells):\n\n";
    private static final String PASS_MOVE_PROMPT = "\nPress Enter and pass the move to another player\n...";
    private static final String TURN_PROMPT = "\n%s, it's your turn:\n\n";

    public void play() {
        var players = new BattleshipPlayer[]{
                new BattleshipPlayer("Player 1"),
                new BattleshipPlayer("Player 2")
        };
        Arrays.stream(players).forEach(BattleshipGame::inputShipsForPlayer);

        ShotResult shotResult = null;
        int firstIndex = 0;
        while (!ShotResult.SANK_LAST.equals(shotResult)) {
            var p1 = players[firstIndex];
            var p2 = players[firstIndex = (firstIndex + 1) % 2];

            shotResult = takeOneShot(p1, p2);
        }
    }

    private static void inputShipsForPlayer(BattleshipPlayer player) {
        System.out.printf(PLACE_SHIP_PROMPT, player.name);
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

        passMovePrompt();
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

    private static ShotResult takeOneShot(BattleshipPlayer shooter, BattleshipPlayer target) {
        ShotResult shotResult;
        do {
            System.out.println(shooter.renderBoth());
            System.out.printf(TURN_PROMPT, shooter.name);

            shotResult = shooter.shoot(target, scanner.nextLine());

            System.out.println(SHOT_MESSAGE_MAP.get(shotResult));
            if (canPassMove(shotResult)) {
                passMovePrompt();
            }

        } while (shotResult.equals(ShotResult.WRONG_COORDINATES));

        return shotResult;
    }

    private static boolean canPassMove(ShotResult shotResult) {
        return Stream.of(ShotResult.WRONG_COORDINATES, ShotResult.SANK_LAST).noneMatch(shotResult::equals);
    }

    private static void passMovePrompt() {
        System.out.println(PASS_MOVE_PROMPT);
        scanner.nextLine();
    }
}
