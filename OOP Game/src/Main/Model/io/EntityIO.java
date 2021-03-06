package Main.Model.io;

import Main.Model.Entity.*;
import Main.Model.Inventory.Inventory;
import Main.Model.Map.MapLocationPoint;
import Main.Model.Occupation.Occupation;
import Main.Model.Occupation.Smasher;
import Main.Model.Occupation.Sneak;
import Main.Model.Occupation.Summoner;
import Main.Model.Stats.Stats;

import java.util.ArrayList;

/**
 * Created by johnkaufmann on 3/12/16.
 * TODO:
 */
public class EntityIO {

    io io;
    public EntityIO() {
        this.io = new io();
    }

    //load an avatar given the current avatar and a filename
    public ArrayList<Entity> loadEntities(ArrayList<Entity> entity, String fileName) {

        ArrayList<String> FileData;
        FileData = io.readFile(fileName);

        for (int i = 0; i < FileData.size(); i++) {
            ArrayList<String> subList = new ArrayList<>();
            for (int j = i; j < FileData.size(); j++) {
                if (FileData.get(j).equals("~")) break;
                subList.add(FileData.get(j));
                i++;
            }
            entity.add(loadEntity(subList));
        }
        return entity;
    }

    //load an avatar with no object or file name given
    public ArrayList<Entity> loadEntities(String fileName) {
        return loadEntities(new ArrayList<Entity>(), fileName);
    }

    //load an avatar with no object or file name given
    public ArrayList<Entity> loadEntities() {
        return loadEntities(new ArrayList<Entity>(), "Entities.txt");
    }

    //loads a single entity given its data
    private Entity loadEntity(ArrayList<String> FileData) {
        //set up avatar parameters
        EntityTypeEnum type = EntityTypeEnum.NPC;
        Stats stats;
        Occupation occupation = null;
        Inventory inventory = null;
        MapLocationPoint location = new MapLocationPoint(0,0);
        int level = 1;
        Entity entity = null;

        //set the avatar parameters given file data
        for (int i = 0; i < FileData.size(); i++) {
            String line = FileData.get(i);
            String[] data = line.split(":", 2);
            switch (data[0]) {
                case "Name":
                    System.out.print("Loading " + data[1]);
                    break;
                case "Type":
                    type = setType(data[1]);
                    System.out.println(" the " + data[1] + ".");
                    break;
                case "Occupation":
                    occupation = setOccupation(data[1]);
                    break;
                case "Level":
                    level = Integer.valueOf(data[1]);
                    break;
                case "Inventory":
                    inventory = setInventory(data[1]);
                    break;
                case "Location":
                    location = setLocation(data[1]);
                    break;
            }
        }

        //instantiate a new entity;
        switch (type) {
            case Avatar:
                entity = new Avatar(occupation,location,level, inventory);
                break;
            case NPC:
                entity = new Npc(occupation,location,level,inventory);
                break;
            case Pet:
                entity = new Pet(location,level);
                break;
            case Mount:
                entity = new Mount(location);
                break;
            default:
                System.out.println("Something went wrong in the instatiation of an entity in " + this.toString());
        }

        //level the entity up appropriately
        for (int i = 1; i < level; i++) {
            entity.levelUp();
        }


        return entity;
    }

    //given a string sets an entity type
    private EntityTypeEnum setType(String s) {
        return EntityTypeEnum.valueOf(s);
    }

    //given a string x,y return the map location
    private MapLocationPoint setLocation(String s) {
        String[] loc = s.split(",");
        return new MapLocationPoint(Integer.valueOf(loc[0]),Integer.valueOf(loc[1]));
    }

    //change in file structure
    private Inventory setInventory(String s) {
        return new ItemsIO().getEntityInventory(s);
    }

    //given a string containing the name of an occupation the file return an occupation (defaults to smasher if data corrupted)
    private Occupation setOccupation(String s) {
        switch (s) {
            case "Smasher":
                return new Smasher();
            case "Summoner":
                return new Summoner();
            case "Sneak":
                return new Sneak();
            default:
                System.out.print("ERROR IN OCCUPATION!");
                return new Smasher();
        }
    }

    //save entities to a text file given an array of them and a file name
    public void saveEntities(ArrayList<Entity> entities, String fileName) {
        ArrayList<String> entityStrings = new ArrayList<>();
        for (Entity entity : entities) {
            saveEntity(entityStrings, entity);
            entityStrings.add("~");
        }
        io.writeFile(entityStrings, fileName);
    }

    //save entities to a text file name Entities.txt by default
    public void saveEntities(ArrayList<Entity> entities) {
        saveEntities(entities, "Entities.txt");
    }

    //converts an array list of entity data to a format saveable by the io
    private ArrayList<String> saveEntity(ArrayList<String> entityData, Entity entity) {

        /**
         * Name:John
         * Type:entity
         * Occupation:Smasher
         * Level:3
         * Inventory:3
         * Takeable1:1
         * Takable2:1
         * Takable3:3
         * Location:0,2
         */

        entityData.add(getName("Name:", entity));
        entityData.add(getType("Type:", entity));
        entityData.add(getOccupation("Occupation:", entity));
        entityData.add(getLevel("Level:", entity));
        // TODO: 3/12/16 uncomment when we finish printing out the inventory
//        entityData.add(getInventory("Inventory:", entity));
        entityData.add(getLocation("Location:", entity));

        return entityData;
    }

    //gets a location of an entity given its format and that entity
    private String getLocation(String locationFormat, Entity entity) {
        return locationFormat + String.valueOf(entity.getLocation().x)+","+String.valueOf(entity.getLocation().y);
    }

    // TODO: 3/12/16 implement get inventory and shit
    //gets the inventory of an entity given its format and the entity
    private String getInventory(String inventoryFormat, Entity entity) {
        return entity.toString();
    }

    //formats the level of the entity to a format that can be read by io
    private String getLevel(String levelFormat, Entity entity) {
        return levelFormat += String.valueOf(entity.getStats().getLevel());
    }

    //formats the occupation of the entity to a format that can be read by io
    private String getOccupation(String occupationFormat, Entity entity) {
        return occupationFormat + entity.getOccupation().toString();
    }

    //formats the type of the entity to a format that can be read by io
    private String getType(String typeFormat, Entity entity) {
        switch (entity.getType()) {
            case Avatar:
                return typeFormat + "Avatar";
            case NPC:
                return typeFormat + "NPC";
            case Pet:
                return typeFormat + "Pet";
            case Mount:
                return typeFormat + "Mount";
            default:
                return typeFormat + "NPC";
        }
    }

    // TODO: 3/12/16 change this if we implement a way to see the avatars name
    //formats the name of the entity to a format that can be read by io
    private String getName(String nameFormat, Entity entity) {
        return nameFormat + "John";
    }
}
