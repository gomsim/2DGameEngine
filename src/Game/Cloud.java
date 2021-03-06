package Game;

import Logic.Component.CameraEffectComponent;
import Logic.Engine;
import Logic.Entity;
import Logic.Utility;

class Cloud extends Entity {

    public Cloud(double x, double y, double z){
        super(x,y,128*2*Utility.inverseSquare(z),128*2*Utility.inverseSquare(z),"GameResources/Cloud.png", 32, 32);
        setZ(z);
        add(new CameraEffectComponent());
        setVelocity(-6*Utility.inverseSquare(z),0);
    }

    public void update(){
        if (getX() < -getWidth())
            setPosition(Engine.getViewWidth(),getY());
        else if (getX() > Engine.getViewWidth())
            setPosition(-getWidth(),getY());
    }

    public static Cloud createCloud(double x, double y, boolean foreground){
        double distance = foreground? (-0.5):1 * Math.random();
        return new Cloud(x,y,distance);
    }
}
