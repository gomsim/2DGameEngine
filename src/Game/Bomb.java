package Game;


import Logic.Engine;
import Logic.Entity;
import Logic.Component.GravityComponent;
import Logic.Utility;

import java.awt.*;

public class Bomb extends Entity {

    public Bomb(double x, double y, double velX, double velY){
        super(x,y,16,16,"GameResources/Bomb.png",8,8);
        register(new GravityComponent());
        double[] dir = Utility.unitVector(velX, velY);
        setVelocity(dir[Engine.X] * 18, dir[Engine.Y] * 18);
    }

    public Image getTexture() {
        return getTexture(0,0);
    }
}