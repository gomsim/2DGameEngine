package Game;

import Logic.Component.CameraEffectComponent;
import Logic.Entity;

import java.awt.image.BufferedImage;

public class Ground extends Entity {

    private static final int TEXTURE_SIZE = 32;
    private static final int ENTITY_SIZE = 64*2;

    public Ground(){
        this(0,0,null);
    }

    public Ground(double x, double y, BufferedImage texture){
        super(x,y,ENTITY_SIZE,ENTITY_SIZE,texture,TEXTURE_SIZE,TEXTURE_SIZE);
        add(new CameraEffectComponent());
    }


    public static Ground newInstance(Double x, Double y, BufferedImage texture){
        return new Ground(x, y, texture);
    }


    public static int textureSize() {
        return TEXTURE_SIZE;
    }


    public static double entitySize() {
        return ENTITY_SIZE;
    }
}
