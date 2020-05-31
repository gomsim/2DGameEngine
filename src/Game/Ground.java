package Game;

import Logic.Engine;
import Logic.Entity;

import java.awt.*;

public class Ground extends Entity {

    public Ground(double x){
        super(x, Engine.instance().getHeight()-64*4.5 +10,64,64,"GameResources/Ground.png", 32, 32);
    }

    public Image getTexture(){
        return getTexture(1,0);
    }
}
