package Graphics;

import Logic.Engine;
import Logic.Entity;
import Logic.SortedCopyOnWriteArrayList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Window extends JFrame {

    private final BlockingQueue<Image> back = new ArrayBlockingQueue<>(2);
    private final BlockingQueue<Image> front = new ArrayBlockingQueue<>(2);

    private final Thread renderThread = new Thread(new Bufferer(),"bufferThread");
    private final Thread bufferThread = new Thread(new Renderer(),"renderThread");

    public Window(CopyOnWriteArraySet<Integer> inputBuffer){
        int bufferSize = back.remainingCapacity();
        for (int i = 0; i < bufferSize; i++)
            back.add(new BufferedImage(Engine.getScreenWidth(),Engine.getScreenHeight(),BufferedImage.TYPE_INT_ARGB));

        setTitle("2DGameEngine");
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

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

        bufferThread.start();
        renderThread.start();
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

    private class Bufferer implements Runnable{

        private static final int NUM_THREADS = 8;
        private final ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        public void run(){
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
                        if (insideCamera(entity)){
                            Image entityImg = entity.getTexture();

                            int imagePortion = entityImg.getHeight(null)/NUM_THREADS;
                            int entityPortion = (int)entity.getHeight()/NUM_THREADS;
                            int imageWidth = entityImg.getWidth(null);
                            int entityWidth = (int)entity.getWidth();
                            int entityX = (int)entity.getX();
                            int entityY = (int)entity.getY();

                            java.util.List<Callable<Boolean>> tasks = new ArrayList<>();
                            for (int i = 0; i < NUM_THREADS; i++){
                                final int count = i;
                                tasks.add(() -> bufferGraphics.drawImage(entityImg,
                                        entityX,entityY+entityPortion*count, entityX+entityWidth, entityY+entityPortion*(count+1),
                                        0,imagePortion*count, imageWidth, imagePortion*(count+1),null));
                            }

                            pool.invokeAll(tasks);
                            //bufferGraphics.drawImage(entity.getTexture(), (int)entity.getX(), (int)entity.getY(), null);
                        }
                    }

                    front.add(image);
                    bufferGraphics.dispose();
                }
            }catch(InterruptedException e){
                System.out.println("BufferThread interrupted");
            }
        }
        
        private boolean insideCamera(Entity entity){
            return !((entity.getX() + entity.getWidth() < 0 || entity.getX() > Engine.getViewWidth()) ||
                    (entity.getY() + entity.getHeight() < 0 || entity.getY() > Engine.getViewHeight()));
        }
    }

    private class Renderer implements Runnable{

        public void run(){
            try{
                while(true){
                    Image image = front.take();
                    Graphics renderGraphics = getGraphics();

                    renderGraphics.drawImage(image, 0,0, null);

                    back.add(image);
                    renderGraphics.dispose();
                }
            }catch(InterruptedException e){
                System.out.println("RenderThread interrupted");
            }
        }
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
