package Game;

import Logic.Engine;
import Logic.Entity;

import java.awt.*;

public class Smoke extends Entity {

    private int counter = 20;

    public Smoke(double x, double y, int width, int height){
        super(x,y,width,height,"GameResources/SmokeAA.png");
        setVelocity(Math.random()*3,Math.random()*3);
    }

    public Image getTexture(){
        int x;
        if (counter > 15)
            x = 0;
        else if (counter > 5)
            x = 1;
        else
            x = 2;
        return getTexture(x,0);
    }

    public void action(){
        if (counter-- <= 0)
            Engine.instance().remove(this);
    }
}
