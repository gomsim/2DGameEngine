package Logic.Component;

import Logic.Engine;
import Logic.Entity;

public class GravityComponent implements EntityComponent{

    public void apply(Entity owner){
        owner.addVelocity(0, Engine.GRAVITY);
    }
}
