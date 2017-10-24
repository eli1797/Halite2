package hlt;

import hlt.Constants;
import hlt.DockMove;
import hlt.Entity;
import hlt.GameMap;
import hlt.Move;
import hlt.Navigation;
import hlt.Planet;
import hlt.Ship;
import hlt.ThrustMove;

import java.util.ArrayList;
import java.util.Map;

/**
 * By Eli Bailey
 * A class that contains special instructions for the opening of the game
 */
public class Opener {

    /**
     * Moves the three opening ships to dock with the nearest planets
     * @param gameMap 2D gamemap
     * @param playerID My identification
     * @return ArrayList of moves to complete
     */
    public static ArrayList<Move> opener(GameMap gameMap, int playerID) {
        Ship ship1 = gameMap.getShip(playerID, 0);

        ArrayList<Move> toReturn = new ArrayList<>();
        ArrayList<Planet> nearestThree = openerHelper(ship1, gameMap);

        for (int i = 0; i < 3; i++) {
            Ship curShip = gameMap.getShip(playerID, i);
            if (curShip.canDock(nearestThree.get(i))) {
                toReturn.add(new DockMove(curShip, nearestThree.get(i)));
                continue;
            } else {
                final ThrustMove newThrustMove = Navigation.navigateShipToDock
                        (gameMap, curShip, nearestThree.get(i), Constants
                                .MAX_SPEED /
                                2);
                if (newThrustMove != null) {
                    toReturn.add(newThrustMove);
                    continue;
                }
            }
        }
        return toReturn;
    }

    /**
     * Given on ships location it produces an arraylist of the three nearest
     * planets
     * @param ship One of the three ships I start the game with
     * @param gameMap The map of the game
     * @return An ArrayList containing the three planets closest to the
     * ships starting location
     */
    private static ArrayList<Planet> openerHelper(Ship ship, GameMap gameMap) {
        ArrayList<Planet> toReturn = new ArrayList<>();
        Map<Double, Entity> entityMap = gameMap.nearbyEntitiesByDistance(ship);
        if (entityMap != null && !(entityMap.isEmpty())) {
            Double shortDist = null;
            int count = 0;
            for (Map.Entry<Double, Entity> entry : entityMap.entrySet()) {
                Entity ent = entry.getValue();
                if (!(ent instanceof Planet)) {
                    //skip this iteration
                    continue;
                } else {
                    Planet temp = (Planet) ent;
                    if (temp.isOwned()) {
                        //skip this iteration
                        continue;
                    }
                    if (count < 3) {
                        toReturn.add(temp);
                        count++;
                    } else {
                        break;
                    }
                }
            }
        }
        return toReturn;
    }
}
