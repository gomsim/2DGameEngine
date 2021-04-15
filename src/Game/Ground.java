package Game;

import Logic.Component.CameraEffectComponent;
import Logic.Entity;

import java.awt.image.BufferedImage;

public class Ground extends Entity {

    public Ground(double x, double y, BufferedImage texture){
        super(x,y,64,64,texture,32,32);
        register(new CameraEffectComponent());
    }

}
