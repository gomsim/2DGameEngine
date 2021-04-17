package Graphics;

import Logic.Engine;
import Logic.Entity;
import Logic.SortedCopyOnWriteArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;

public class Window extends JFrame {

    private BlockingQueue<Image> back = new ArrayBlockingQueue<>(2);
    private BlockingQueue<Image> forward = new ArrayBlockingQueue<>(2);

    private Thread renderThread;
    private Thread bufferThread;

    public Window(CopyOnWriteArraySet<Integer> inputBuffer){
        int bufferSize = back.remainingCapacity();
        for (int i = 0; i < bufferSize; i++)
            back.add(new BufferedImage(Engine.getScreenWidth(),Engine.getScreenHeight(),BufferedImage.TYPE_INT_ARGB));

        setTitle("Death from Above");
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        //setOpacity(0.3f); //For testing

        Toolkit tk= getToolkit();
        Cursor transparent = tk.createCustomCursor(tk.getImage(""), new Point(), "trans");
        setCursor(transparent);

        addKeyListener(new KeyInputListener(inputBuffer));

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        try{
            device.setFullScreenWindow(this);
        }finally{
            device.setFullScreenWindow(null);
        }

        bufferThread = startBufferThread();
        renderThread = startRenderThread();
    }

    public void exit(){
        renderThread.interrupt();
        bufferThread.interrupt();

        dispose();
    }

    public void render(){
        //Uncomment to cap frame rate to 60 FPS
        /*synchronized (this){
            notify();
        }*/
    }

    private Thread startBufferThread(){
        Thread thread = new Thread(()->{

            try{
                while(true){
                    SortedCopyOnWriteArrayList<Entity> toRender = Engine.instance().getEntities();

                    //Uncomment to cap frame rate to 60 FPS
                    /*synchronized (this){
                        wait();
                    }*/
                    Image image = back.take();
                    Graphics bufferGraphics = image.getGraphics();

                    for (Entity entity: toRender){
                        if (insideCamera(entity))
                            bufferGraphics.drawImage(entity.getTexture(), (int)entity.getX(), (int)entity.getY(), null);
                    }

                    forward.add(image);
                    bufferGraphics.dispose();
                }
            }catch(InterruptedException e){
                System.out.println("BufferThread interrupted");
            }
        });
        thread.start();
        return thread;
    }
    private Thread startRenderThread(){
        Thread thread = new Thread(()->{
            try{
                while(true){
                    Image image = forward.take();

                    Graphics renderGraphics = getGraphics();
                    renderGraphics.drawImage(image,0,0,null);

                    back.add(image);
                    renderGraphics.dispose();

                }
            }catch(InterruptedException e){
                System.out.println("RenderThread interrupted");
            }
        });
        thread.start();
        return thread;
    }

    private boolean insideCamera(Entity entity){
        return !((entity.getX() + entity.getWidth() < 0 || entity.getX() > Engine.getViewWidth()) ||
                (entity.getY() + entity.getHeight() < 0 || entity.getY() > Engine.getViewHeight()));
    }

    public static BufferedImage makeCompatibleImage(BufferedImage image){
        GraphicsConfiguration GFX_CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        if (image.getColorModel().equals(GFX_CONFIG.getColorModel())) {
            return image;
        }
        final BufferedImage newImage = GFX_CONFIG.createCompatibleImage(image.getWidth(),image.getHeight(),image.getTransparency());

        final Graphics2D g2d = (Graphics2D) newImage.getGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return newImage;
    }

    private static class KeyInputListener extends KeyAdapter{

        CopyOnWriteArraySet<Integer> inputBuffer;

        KeyInputListener(CopyOnWriteArraySet<Integer> inputBuffer){
            this.inputBuffer = inputBuffer;
        }

        public void keyPressed(KeyEvent event){
            inputBuffer.add(event.getKeyCode());
        }
        public void keyReleased(KeyEvent event){
            inputBuffer.remove(event.getKeyCode());
        }
    }
}
