import hlt.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MyBot {

    public static void main(final String[] args) {
        final Networking networking = new Networking();
        final GameMap gameMap = networking.initialize("Tamagocchi");

        final ArrayList<Move> moveList = new ArrayList<>();
        for (;;) {
            moveList.clear();
            gameMap.updateMap(Networking.readLineIntoMetadata());

            for (final Ship ship : gameMap.getMyPlayer().getShips().values()) {
                if (ship.getDockingStatus() != Ship.DockingStatus.Undocked) {

                    Planet nearestPlanet = nearestPlanet(ship, gameMap);

                        if (nearestPlanet.isOwned()) {
                            continue;
                        }

                        if (ship.canDock(nearestPlanet)) {
                            moveList.add(new DockMove(ship, nearestPlanet));
                            break;
                        }

                        final ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, nearestPlanet, Constants.MAX_SPEED/2);
                        if (newThrustMove != null) {
                            moveList.add(newThrustMove);
                        }

                        break;

                }
            }
            Networking.sendMoves(moveList);
        }
    }

    private static Planet nearestPlanet(Ship ship, GameMap gameMap) {
        Planet nearestPlanet = null;
        Map<Double, Entity> nearby = gameMap.nearbyEntitiesByDistance(ship);

        for (Map.Entry<Double, Entity> entry : nearby.entrySet()) {
            Double distance = entry.getKey();
            Entity ent = entry.getValue();

            if (ent instanceof Planet) {
                nearestPlanet = (Planet) ent;
                return nearestPlanet;
            }
        }
        return null;
    }
}
