package Game;

import Logic.Engine;
import Logic.Entity;

import java.awt.*;

public class Cloud extends Entity {

    public Cloud(double x, double y){
        super(x,y,128,128,"GameResources/Cloud.png", 32, 32);
        setVelocity(-Math.random(),0);
        setZ(5);
    }

    @Override
    public Image getTexture() {
        return getTexture(0,0);
    }

    public void action(){
        if (getX() < -getWidth())
            setPosition(Engine.instance().getWidth(),getY());
    }
}
