package Logic;


import Graphics.Renderer;
import Logic.Component.EntityComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Entity {

    private static BufferedImage nullSprite;
    private ArrayList<EntityComponent> components = new ArrayList<>();

    private double x, y;
    private double z;
    private double width, height;
    private double velX, velY;
    private String[] traits;
    private BufferedImage texture;

    public Entity(double x, double y, double width, double height, String texturePath, int textureWidth, int textureHeight, String ... traits){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.traits = traits;
        try{
            nullSprite = ImageIO.read(new File("EngineResources/NullSprite.png"));
            texture = ImageIO.read(new File(texturePath));
            texture = Utility.resize(texture,(int)width,(int)height,width/textureWidth,height/textureHeight);
            //texture = Renderer.makeCompatibleImage(texture);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Entity(double x, double y, int width, int height, String texturePath, String ... traits){
        this(x,y,width,height,texturePath,width,height,traits);
    }

    public abstract Image getTexture();
    protected Image getTexture(int x, int y){
        Image subTexture;
        try{
            subTexture = texture.getSubimage((int)(x*width), (int)(y*height), (int)(width), (int)(height));
        }catch(NullPointerException | RasterFormatException e){
            subTexture = nullSprite;
        }
        return subTexture;
    }

    public void register(EntityComponent component){
        components.add(component);
    }
    public void deregister(EntityComponent component){
        components.remove(component);
    }

    public void setZ(double z){
        this.z = z;
    }

    public void tick(){
        for (EntityComponent component: components)
            component.apply(this);
        resolveMovement();
        action();
    }

    public void resolveMovement(){
        x += velX;
        y += velY;
    }
    public void action(){}

    public double[] getEdgePoint(double angle){
        return Utility.add(getCenter(), Utility.multiply(Utility.vector(angle),width/2));
    }
    public double[] getCenter(){
        return new double[] {x + width/2, y + height/2};
    }
    public void centerOn(double x, double y){
        this.x = x - width/2;
        this.y = y - height/2;
    }
    public double getCenterX(){
        return x + width/2;
    }
    public double getCenterY(){
        return y + height/2;
    }
    public double[] getPosition(){
        return new double[] {x, y};
    }
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    public void setRelativePosition(double x, double y){
        this.x += x;
        this.y += y;
    }
    public double getVelX(){
        return velX;
    }
    public double getVelY(){
        return velY;
    }
    public double[] getVelocity(){
        return new double[] {velX, velY};
    }
    public void setVelocity(double x, double y){
        velX = x;
        velY = y;
    }
    public void addVelocity(double x, double y){
        velX += x;
        velY += y;
    }
    public void addVelocityCapped(double x, double y, double cap){
        double magRatio = cap / Utility.magnitude(velX, velY);
        velX = (velX + x) * magRatio;
        velY = (velY + y) * magRatio;
    }

    public double getX() {
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public double getWidth(){
        return width;
    }
    public double getHeight(){
        return height;
    }
    public String[] getTraits(){
        return traits;
    }
    public boolean equals(Object object){
        if (!(object instanceof Entity))
            return false;
        Entity otherEntity = (Entity)object;
        return x == otherEntity.x && y == otherEntity.y &&
                width == otherEntity.width && height == otherEntity.width &&
                Arrays.equals(traits,otherEntity.traits);
    }
}
