package Game;

import Logic.Component.CameraEffectComponent;
import Logic.Engine;
import Logic.Entity;
import Logic.Utility;

import java.awt.*;

public class Cloud extends Entity {

    public Cloud(double x, double y, double z){
        super(x,y,128*Utility.inverseSquare(z)*2,128*Utility.inverseSquare(z)*2,"GameResources/Cloud.png", 32, 32);
        setZ(z);
        register(new CameraEffectComponent());
        setVelocity(-100*Utility.inverseSquare(z*10),0);
    }

    @Override
    public Image getTexture() {
        return getTexture(0,0);
    }

    public void action(){
        if (getX() < -getWidth() && getVelX() <= 0)
            setPosition(Engine.getViewWidth(),getY());
        else if (getX() > Engine.getViewWidth() && getVelX() >= 0)
            setPosition(-getWidth(),getY());
    }

    public static Cloud createCloud(double x, double y){
        double distance = 1 + Math.random();
        return new Cloud(x,y,distance);
    }
}
