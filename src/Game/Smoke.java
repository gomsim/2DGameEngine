package Game;

import Logic.Component.CameraEffectComponent;
import Logic.Engine;
import Logic.Entity;

import java.awt.*;

public class Smoke extends Entity {

    private int counter = 20;

    public Smoke(double x, double y){
        super(x,y,24,24,"GameResources/SmokeAA.png",16,16);
        setVelocity(Math.random()*3-6,Math.random()*3);
        register(new CameraEffectComponent());
    }

    @Override
    public Image getTexture(){
        int x;
        if (counter > 15)
            x = 0;
        else if (counter > 5)
            x = 1;
        else
            x = 2;
        return getSubTexture(x,0);
    }

    public void action(){
        if (counter-- <= 0)
            Engine.instance().remove(this);
    }
}
