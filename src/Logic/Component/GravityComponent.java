package Logic.Component;

import Logic.Engine;
import Logic.Entity;

public class GravityComponent implements EntityComponent{

    private static final double GRAVITY = 0.3;

    public void apply(Entity owner){
        owner.addVelocity(0, GRAVITY);
    }
}
