package Main.Model.Entity;


import Main.Model.Map.MapLocationPoint;
import Main.Model.Occupation.Occupation;
import Main.Model.Occupation.Smasher;
import Main.View.Graphics.GraphicsAssets;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by walkhard on 2/18/16.
 */
public class Npc extends Entity {

    public Npc(EntityTypeEnum entityType, EntitySpeechEnum entitySpiel, Occupation occupation, MapLocationPoint location){
        super(entityType, entitySpiel, occupation, location);
    }

    public Npc(MapLocationPoint location) {
        super(EntityTypeEnum.NPC, EntitySpeechEnum.TRASH, new Smasher(), location);
    }

    public Npc(Occupation o, MapLocationPoint location) {
        super(EntityTypeEnum.NPC, EntitySpeechEnum.TRASH, o, location);
    }

    public void respawn(int xLocation, int yLocation) {
        this.location = new MapLocationPoint(xLocation, yLocation);
    }


}
