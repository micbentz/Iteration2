package Main.Controller.ObjectControllers;

import Main.Controller.Manager.ObjectControllerManager;
import Main.Controller.ObjectControllers.EntityController.EntityController;
import Main.Model.AreaEffect.AreaEffect;
import Main.Model.Entity.Entity;
import Main.Model.Map.Map;
import Main.Model.Map.Tile;

import java.awt.geom.Area;

/**
 * Created by mason on 3/12/16.
 */
public class MapController extends ObjectController {
    private ObjectControllerManager objectControllerManager;
    private Map map;

    public MapController(ObjectControllerManager objectControllerManager, Map map) {
        this.objectControllerManager = objectControllerManager;
        this.map = map;
    }

    public void update() {
        for(int j = 0; j < map.getHeight(); j++) {
            for(int i = 0; i < map.getWidth(); i++) {
                Tile currentTile = map.getTile(i,j);

                // Update effects
                if(currentTile.hasAreaEffect()) {
                    AreaEffect areaEffect = currentTile.getAreaEffect();

                    // Update
                    AreaEffectController aec = (AreaEffectController) objectControllerManager.getObjectController(areaEffect);
                    aec.update();

                    // Apply to entity
                    if(currentTile.hasEntity()) {
                        aec.activate(currentTile.getEntity());
                    }

                }
                // Apply effects

                // Move Effects

                // update characters
                if(currentTile.hasEntity()) {
                    Entity tileEntity = currentTile.getEntity();
                    EntityController ec = (EntityController)objectControllerManager.getObjectController(tileEntity);
                    ec.update();
                }
            }
        }
    }
}
