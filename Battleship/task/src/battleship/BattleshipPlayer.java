package battleship;

import battleship.Utils.PlaceShipResult;
import battleship.Utils.ShotResult;

import java.util.ArrayList;
import java.util.Collection;

public class BattleshipPlayer {

    private static final String UPPER_ROW = "  1 2 3 4 5 6 7 8 9 10\n";
    private static final String LINE = "\n---------------------\n";

    public final String name;
    private final String[][] data;
    private final String[][] rivalData;
    private final Collection<Ship> ships;

    public BattleshipPlayer(String name) {
        this.name = name;
        data = Utils.createInitialField();
        rivalData = Utils.createInitialField();
        ships = new ArrayList<>();
    }

    public String renderSelf() {
        return render(data);
    }

    public String renderRival() {
        return render(rivalData);
    }

    public String renderBoth() {
        return renderRival() + LINE + renderSelf();
    }

    private static String render(String[][] data) {
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
        ships.add(new Ship(crd[0], crd[1]));

        return PlaceShipResult.NO_ERROR;
    }

    public ShotResult shoot(BattleshipPlayer player, String rawCrd) {
        final Shot shot = player.acceptShot(rawCrd);
        Utils.markRivalData(rivalData, shot.result, shot.crd);
        return shot.result;
    }

    public Shot acceptShot(String rawCrd) {
        final int[] crd = Utils.parseShotCoordinates(rawCrd);
        if (Utils.coordinatesNotInRange(crd)) {
            return new Shot(ShotResult.WRONG_COORDINATES, crd);
        }
        switch (Utils.shoot(data, crd)) {
            case Utils.HIT: return new Shot(ShotResult.HIT, crd);
            case Utils.SHIP: return new Shot(hitShip(crd), crd);
            default: return new Shot(ShotResult.MISSED, crd);
        }
    }

    private ShotResult hitShip(int[] crd) {
        final Ship shotShip = ships.stream().filter(ship -> ship.hit(crd)).findFirst().orElseThrow();
        if (!shotShip.sunken()) {
            return ShotResult.HIT;
        }
        ships.remove(shotShip);
        return !ships.isEmpty() ? ShotResult.SANK : ShotResult.SANK_LAST;
    }

    @Override
    public String toString() {
        return renderSelf();
    }

    public static class Shot {
        public final ShotResult result;
        public final int[] crd;

        public Shot(ShotResult result, int[] crd) {
            this.result = result;
            this.crd = crd;
        }
    }

}
