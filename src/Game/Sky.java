package Game;

import Logic.Engine;
import Logic.Entity;

import java.awt.*;

public class Sky extends Entity {

    public Sky(){
        super(0,0, Engine.instance().getWidth(),Engine.instance().getHeight(), "GameResources/Sky.png",320,320);
        setZ(11);
    }

    public Image getTexture(){
        return getTexture(0,0);
    }
}
