package Graphics;

import Logic.Engine;
import Logic.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class Renderer extends JPanel {

    private static final GraphicsConfiguration GFX_CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

    //TODO: Behöver effektiviseras. Utmålning över hela skärmen är långsamt
    public Renderer(){
        setIgnoreRepaint(true);
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        for (Entity entity: Engine.instance().getEntities()){
            if (insideCamera(entity))
                graphics.drawImage(entity.getTexture(), (int)entity.getX(), (int)entity.getY(), null);
        }
    }

    private boolean insideCamera(Entity entity){
        return !((entity.getX() + entity.getWidth() < 0 || entity.getX() > Engine.getScreenWidth()) ||
                (entity.getY() + entity.getHeight() < 0 || entity.getY() > Engine.getScreenHeight()));
    }

    public static BufferedImage makeCompatibleImage(BufferedImage image){
        if (image.getColorModel().equals(GFX_CONFIG.getColorModel())) {
            return image;
        }
        final BufferedImage newImage = GFX_CONFIG.createCompatibleImage(image.getWidth(),image.getHeight(),image.getTransparency());

        final Graphics2D g2d = (Graphics2D) newImage.getGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return newImage;
    }
}
