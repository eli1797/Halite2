import hlt.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MyBot {

    public static void main(final String[] args) {
        final Networking networking = new Networking();
        final GameMap gameMap = networking.initialize("Tamagocchi");
        final int playerID = gameMap.getMyPlayerId();
        Map<Planet, Integer> squads = new TreeMap<>();

        final ArrayList<Move> moveList = new ArrayList<>();
        boolean first = true;
        boolean unSafeExpansionOn = true;
        for (;;) {
            moveList.clear();
            gameMap.updateMap(Networking.readLineIntoMetadata());

            Collection<Ship> myShips = gameMap.getMyPlayer().getShips().values();
            Collection<Ship> undockedShips = myShips.stream()
                    .filter(ship -> ship.getDockingStatus() == Ship.DockingStatus.Undocked)
                    .collect(Collectors.toList());

            DebugLog.addLog("-" + undockedShips.size() + " Number of Undocked" +
                    " ships-");

            if (first) {
                ArrayList<Move> open = Opener.opener(gameMap, playerID);
                first = false;
                Networking.sendMoves(open);
            } else {
                for (Ship ship : undockedShips) {
                    if (ship.getDockingStatus() == Ship.DockingStatus.Undocked) {
                        if (!unSafeExpansionOn) {
                            //after reaching possession of five planets
                            //reinforce then until each planet always has
                            // miners until it's resources are depleted
                        }
                        Planet nearestPlanet = GenNav.nearestUnownedPlanet(ship,
                                gameMap);

                        if (nearestPlanet == null) {
                            nearestPlanet = GenNav.nearestPlanet(ship, gameMap);
                            DebugLog.addLog("Change to any nearby planet");
                        }
                        if (ship.canDock(nearestPlanet)) {
                            moveList.add(new DockMove(ship, nearestPlanet));
                            continue;
                        }

                        final ThrustMove newThrustMove = Navigation.navigateShipToDock(gameMap, ship, nearestPlanet, Constants.MAX_SPEED);
                        if (newThrustMove != null) {
                            moveList.add(newThrustMove);
                            continue;
                        }


                    }
                }
                Networking.sendMoves(moveList);
            }
        }
    }
}
