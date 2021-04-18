package Logic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class TextureDivider {

    public static List<Entity> divideAndConstruct(String texturePath, double posX, double posY, Class<? extends Entity> toConstruct){
        List<Entity> toAdd = new ArrayList<>();

        BufferedImage texture = null;
        try{
            texture = ImageIO.read(new File(texturePath));
        }catch(IOException e){
            e.printStackTrace();
        }

        int textureSize;
        double entitySize;

        try{
            textureSize = (int) toConstruct.getMethod("textureSize").invoke(null);
            entitySize = (double) toConstruct.getMethod("entitySize").invoke(null);
        }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            throw new UnsupportedOperationException(e);
        }

        double sizeRatio = entitySize/textureSize;

        for (int y = 0; y < texture.getHeight(); y += textureSize){
            for (int x = 0; x < texture.getWidth(); x += textureSize){
                BufferedImage subTexture = texture.getSubimage(x, y, textureSize, textureSize);
                try{
                    if (hasContent(subTexture))
                        toAdd.add((Entity) toConstruct.getMethod("newInstance", Double.class, Double.class, BufferedImage.class).invoke(null, x * sizeRatio + posX,y * sizeRatio + posY, subTexture));
                }catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                    throw new UnsupportedOperationException(e);
                }
            }
        }

        return toAdd;
    }

    private static boolean hasContent(BufferedImage image){
        for (int y = 0; y < image.getHeight(); y++){
            for (int x = 0; x < image.getWidth(); x++){
                if (image.getRGB(x,y) >> 24 != 0x00)
                    return true;
            }
        }
        return false;
    }
}
