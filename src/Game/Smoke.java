package Game;

import Logic.Component.CameraEffectComponent;
import Logic.Engine;
import Logic.Entity;

import java.awt.*;

class Smoke extends Entity {

    public Smoke(double x, double y){
        super(x,y,24,24,"GameResources/SmokeAA.png",16,16);
        setVelocity(Math.random()*3-6,Math.random()*3);
        add(new CameraEffectComponent());
    }

    @Override
    public Image getTexture(){
        int x;
        if (ticksPassed() < 5)
            x = 0;
        else if (ticksPassed() < 15)
            x = 1;
        else
            x = 2;
        return getSubTexture(x,0);
    }

    public void update(){
        if (ticksPassed() >= 20)
            Engine.instance().remove(this);
    }
}
