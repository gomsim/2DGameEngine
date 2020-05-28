package Graphics;

import Logic.Engine;
import Logic.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Renderer extends JPanel {

    private ArrayList<Entity> entities = new ArrayList<>();

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        //Graphics2D g2d = (Graphics2D)graphics;
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Entity entity: Engine.instance().getEntities()){
            graphics.drawImage(entity.getTexture(), (int)entity.getX(), (int)entity.getY(), (int)entity.getWidth(), (int)entity.getHeight() , null);
        }
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }
    public void removeEntity(Entity entity){
        entities.remove(entity);
    }
    public void addAll(ArrayList<Entity> all){
        for (Entity entity: all){
            entities.add(entity);
        }
    }
    public void removeAll(ArrayList<Entity> all){
        for (Entity entity: all){
            entities.remove(entity);
        }
    }

}
