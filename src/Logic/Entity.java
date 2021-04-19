package Logic;


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

    private static final BufferedImage nullSprite = loadNullSprite();
    private final ArrayList<EntityComponent> components = new ArrayList<>();

    private int ticksPassed;
    private double x, y;
    private double z;
    private final double width, height;
    private double velX, velY;
    private String[] tags;
    private BufferedImage texture;

    public Entity(double x, double y, double width, double height, BufferedImage texture, int textureWidth, int textureHeight, String ... tags){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try{
            this.texture = texture;
            this.texture = Utility.resize(this.texture,(int)width,(int)height,width/textureWidth,height/textureHeight);
            //this.texture = Window.makeCompatibleImage(texture);
        }catch(NullPointerException e){
            this.texture = nullSprite;
        }
        this.tags = tags;
    }

    private static BufferedImage loadNullSprite(){
        BufferedImage nullSprite = null;
        try{
            nullSprite = ImageIO.read(new File("EngineResources/NullSprite.png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        return nullSprite;
    }

    public Entity(double x, double y, double width, double height, String texturePath, int textureWidth, int textureHeight, String ... tags){
        this(x,y,width,height,loadTexture(texturePath),textureWidth,textureHeight,tags);
    }

    public Entity(double x, double y, int width, int height, String texturePath, String ... tags){
        this(x,y,width,height,texturePath,width,height,tags);
    }

    public Entity(double x, double y, int width, int height, BufferedImage texture, String ... tags){
        this(x,y,width,height,texture,width,height,tags);
    }

    public static <E extends Entity> E newInstance(){
        throw new UnsupportedOperationException();
    }

    public static double entitySize(){
        throw new UnsupportedOperationException();
    }

    public static int textureSize(){
        throw new UnsupportedOperationException();
    }

    public void destroy(){}

    private static BufferedImage loadTexture(String texturePath){
        BufferedImage texture = null;
        try{
            texture = ImageIO.read(new File(texturePath));
        }catch(IOException e){
            e.printStackTrace();
        }
        return texture;
    }

    public Image getTexture(){
        return getSubTexture(0,0);
    }
    protected Image getSubTexture(int x, int y){
        Image subTexture;
        try{
            subTexture = texture.getSubimage(x*((int)width), y*((int)height), (int)width, (int)height);
        }catch(NullPointerException | RasterFormatException e){
            subTexture = nullSprite;
        }
        return subTexture;
    }

    public void add(EntityComponent component){
        components.add(component);
    }
    public void remove(EntityComponent component){
        components.remove(component);
    }

    public void setZ(double z){
        this.z = z;
    }

    public void tick(){
        ticksPassed++;
        for (EntityComponent component: components)
            component.apply(this);
        resolveMovement();
        update();
    }

    public void resolveMovement(){
        x += velX;
        y += velY;
    }
    public void update(){}

    protected int ticksPassed(){
        return ticksPassed;
    }

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
    public void addVelocity(double x, double y, double cap){
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
    public String[] getTags(){
        return tags;
    }
    public boolean equals(Object object){
        if (!(object instanceof Entity))
            return false;
        Entity otherEntity = (Entity)object;
        return x == otherEntity.x && y == otherEntity.y &&
                width == otherEntity.width && height == otherEntity.width &&
                Arrays.equals(tags,otherEntity.tags);
    }
}
