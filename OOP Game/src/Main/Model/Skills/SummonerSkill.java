package Main.Model.Skills;

import Main.Model.Entity.Entity;

/**
 * Created by AndyZhu on 7/3/2016.
 */
public abstract class SummonerSkill extends Skill {
    public SummonerSkill(Entity entity, double cooldown, double manacost) {
        super(entity, cooldown, manacost);
    }
}
