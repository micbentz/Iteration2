package Main.Controller.EntityControllers;

import Main.Controller.Manager.UserActionEnum;
import Main.Model.Entity.Entity;
import Main.Model.Entity.EntityTypeEnum;
import Main.Model.Map.*;
import Main.Model.Skills.RadialEffect;
import sun.misc.Queue;

import java.util.ArrayList;

/**
 * Created by Michael on 3/12/16.
 */
public class NpcMovementGenerator {

    private Map map;
    private MapLocationPoint currentTargetLocation;
    private MapLocationPoint followedEntityLocation;
    private Entity followerEntity;
    private PathFinder pathFinder;
    private Path path;
    private ArrayList<Entity> entityLocations;
    private int currentPathPosition = 0;

    /* The pathing controller determines the path for a followerEntity to take. Currently, it takes the followerEntity's level and directly
     translates it to a 1:1 search radius. For example, If Pet is of level 2 then it searches a radius of 2 for enemies.
    */
    public NpcMovementGenerator(Map map, Entity followedEntity , Entity followerEntity, Heuristic heuristic) {
        this.map = map;
        this.followerEntity = followerEntity;
        this.followedEntityLocation = followedEntity.getLocation();
        this.currentTargetLocation = new MapLocationPoint(followedEntityLocation.x, followedEntityLocation.y);
        this.pathFinder = new PathFinder(map, followerEntity.getStats().getLevel(), heuristic);
        this.path = pathFinder.findPath(followerEntity.getLocation().x, followerEntity.getLocation().y,  currentTargetLocation.x, currentTargetLocation.y);

    }

    public void update() {
        // Determines what the followerEntity should set as its currentTargetLocation
        currentTargetLocation = determineTarget(followerEntity.getType());

        // Gets the path from an followerEntity to targetEntity
        if((currentTargetLocation.x != followedEntityLocation.x || currentTargetLocation.y != followedEntityLocation.y)) {
            path = pathFinder.findPath(followerEntity.getLocation().x, followerEntity.getLocation().y,  currentTargetLocation.x, currentTargetLocation.y);
            currentPathPosition = 0;
            currentTargetLocation.x = followedEntityLocation.x;
            currentTargetLocation.y = followedEntityLocation.y;
        }
    }


    private MapLocationPoint determineTarget(EntityTypeEnum e){
//        System.out.println("E type: " + e);
        if (e == EntityTypeEnum.NPC){
            if (map.getPlayerLocation() != null){
                return map.getPlayerLocation();
            }
        }
        if (e == EntityTypeEnum.Pet){
            // Search the radius around a pet for an npc
            searchRadius();

            // There are no NPCs in radius, set Player as target
            if(entityLocations.size() == 0){
                return map.getPlayerLocation();

                // There are NPCs, find the closest NPC and attack
            } else {
//                System.out.println("There are mobs");
                return findMinimum(followerEntity.getLocation().x, followerEntity.getLocation().y,entityLocations);
            }
        }
        return null;
    }

    // Search a radius equal to the aggro range for Entities
    private void searchRadius(){
        entityLocations = new ArrayList<>();
        Queue<ArrayList<MapLocationPoint>> q = new RadialEffect().getAffectedArea(followerEntity.getLocation().x, followerEntity.getLocation().y, followerEntity.getStats().getLevel()/2);
        int currentRadius = 1;
        while (!q.isEmpty()) {
            System.out.println("Radius: " + currentRadius++);
            try {
                ArrayList<MapLocationPoint> current = q.dequeue();
                int size = current.size();
                for (int i = 0; i < size; i++) {
                    MapLocationPoint temp;
                    temp = current.get(i);
                    if (map.getTile(temp.x,temp.y).getEntity().getType() == EntityTypeEnum.NPC){
                        entityLocations.add(map.getTile(temp.x,temp.y).getEntity());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private MapLocationPoint findMinimum(int sx,int sy,ArrayList<Entity> entityLocations){
        // for all npcs on the map, find the shortest distance and then go attack that
        int temp;
        int minimum = 100;
        int j = 0;

        for(int i = 0; i < entityLocations.size(); i++){
            MapLocationPoint point = entityLocations.get(i).getLocation();
//            System.out.println(entityLocations.get(i).getLocation());
            temp = (int)pathFinder.getHeuristicCost(sx, sy, point.x, point.y);
            if (temp < minimum){
                minimum = temp;
                j = i;
            }
        }

        return entityLocations.get(j).getLocation();
    }

    public UserActionEnum getNextMovement(){
        if(path != null) {
            return pointToEnum(path.getPoint(currentPathPosition), path.getPoint(currentPathPosition+1));
        }
        else {
            return null;
        }
    }

    private UserActionEnum pointToEnum(MapLocationPoint currentPoint, MapLocationPoint adjacentPoint) {
        int x1 = currentPoint.x;
        int y1 = currentPoint.y;
        int x2 = adjacentPoint.x;
        int y2 = adjacentPoint.y;
        // For all even cases of X
        // X positions are equal, check for y
        if (x1 % 2 == 0){
            if (x1 == x2) {
                if (y1 > y2) {
                    //followerEntity.move(DirectionEnum.Up);
                    return UserActionEnum.Up;
                }
                if (y1 < y2) {
                    //followerEntity.move(DirectionEnum.Down);
                    return UserActionEnum.Down;
                }
            } else if (y1 == y2) { // Y position are equal, check for x
                if (x1 > x2) {
                    //followerEntity.move(DirectionEnum.DownLeft);
                    return UserActionEnum.DownLeft;
                }
                if (x1 < x2) {
                    //followerEntity.move(DirectionEnum.DownRight);
                    return UserActionEnum.DownRight;
                }
            } else { // Neither Y nor X are equal, check X equivalents
                if (x1 > x2) {
                    //followerEntity.move(DirectionEnum.UpLeft);
                    return UserActionEnum.UpLeft;
                }
                if (x1 < x2) {
                    //followerEntity.move(DirectionEnum.UpRight);
                    return UserActionEnum.UpRight;
                }
            }
        } else { // Odd cases
            if (x1 == x2) {
                if (y1 > y2) {
                     //followerEntity.move(DirectionEnum.Up);
                    return UserActionEnum.Up;
                }
                if (y1 < y2) {
                   // followerEntity.move(DirectionEnum.Down);
                    return UserActionEnum.Down;
                }
            } else if (y1 == y2) {
                if (x1 > x2) {
                    //followerEntity.move(DirectionEnum.UpLeft);
                    return UserActionEnum.UpLeft;
                }
                if (x1 < x2) {
                    //followerEntity.move(DirectionEnum.UpRight);
                    return UserActionEnum.UpRight;
                }
            } else { // Neither Y nor X are equal, check X equivalents
                if (x1 > x2) {
                    //followerEntity.move(DirectionEnum.DownLeft);
                    return UserActionEnum.DownLeft;
                }
                if (x1 < x2) {
                    //followerEntity.move(DirectionEnum.DownRight);
                    return UserActionEnum.DownRight;
                }
            }
        }
        return null;
    }
}
