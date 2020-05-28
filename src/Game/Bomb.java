package Game;

import Logic.Entity;
import Logic.GravityComponent;

import java.awt.*;

public class Bomb extends Entity {

    public Bomb(double x, double y){
        super(x,y,16,16,"GameResources/Bomb.png",8,8);
        register(new GravityComponent());
    }

    public Image getTexture() {
        return getTexture(0,0);
    }
}
