package Game;

import Logic.Engine;
import Logic.Entity;

import java.awt.*;

public class Sky extends Entity {

    public Sky(){
        super(0,0, Engine.getWidth(),Engine.getHeight(), "GameResources/Sky.png",480,270);
        setZ(Double.MAX_VALUE);
    }

    public Image getTexture(){
        return getTexture(0,0);
    }
}
