package Main.Model.Entity;

import Main.Model.DirectionEnum;
import Main.Model.Inventory.Inventory;
import Main.Model.Map.MapLocationPoint;
import Main.Model.Occupation.Smasher;

/**
 * Created by johnkaufmann on 3/12/16.
 * TODO:
 */
public class Mount extends Entity {

    private Avatar player;

    public Mount(MapLocationPoint location) {
        super(EntityTypeEnum.Mount, EntitySpeechEnum.TRASH, new Smasher(), location, 1, new Inventory());
        this.player = null;
    }

    public Avatar getPlayerInside() {
        return player;
    }

    public void setPlayerInside(Avatar player) {
        this.player = player;
    }

    public Avatar removePlayerInside() {
        Avatar player = this.player;
        this.player = null;
        return player;
    }

    @Override
    public void move(DirectionEnum dir) {
        super.move(dir);
        if (this.player != null) player.move(dir);
    }

    public void respawn(MapLocationPoint location) {

    }

    @Override
    public void setLocation(MapLocationPoint location) {
        super.setLocation(location);
        if (this.player != null) player.setLocation(location);
    }
}
