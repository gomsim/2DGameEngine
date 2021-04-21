package Rendering;

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
import java.util.concurrent.atomic.AtomicBoolean;

public class Window extends JFrame {

    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int RENDER_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int RENDER_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;

    private final BlockingQueue<Image> back = new ArrayBlockingQueue<>(2);
    private final BlockingQueue<Image> front = new ArrayBlockingQueue<>(2);

    private final Bufferer bufferer = new Bufferer();
    private final Renderer renderer = new Renderer();

    private final KeyInputListener keyInputListener;

    public Window(CopyOnWriteArraySet<Integer> inputBuffer, String name){
        int bufferSize = back.remainingCapacity();
        for (int i = 0; i < bufferSize; i++)
            back.add(new BufferedImage(Engine.getViewWidth(),Engine.getViewHeight(),BufferedImage.TYPE_INT_ARGB));

        setTitle(name != null ? name:"2DGameEngine");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        Toolkit tk= getToolkit();
        Cursor transparent = tk.createCustomCursor(tk.getImage(""), new Point(), "trans");
        setCursor(transparent);

        addKeyListener(keyInputListener = new KeyInputListener(inputBuffer));

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        device.setFullScreenWindow(this);

        new Thread(bufferer,"bufferThread").start();
        new Thread(renderer,"renderThread").start();
    }

    public void close(){
        renderer.kill();
        bufferer.kill();
        removeKeyListener(keyInputListener);

        dispose();
    }

    public void render(){
        //Uncomment to cap frame rate to 60 FPS
        /*synchronized (this){
            notify();
        }*/
    }

    private class Bufferer implements Runnable{

        private final ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        private final AtomicBoolean running = new AtomicBoolean(true);

        public void run(){
            try{
                while(running.get()){
                    SortedCopyOnWriteArrayList<Entity> toRender = Engine.instance().getEntities();

                    //Uncomment to cap frame rate to 60 FPS
                    /*synchronized (this){
                        wait();
                    }*/
                    Image image = back.take();
                    Graphics bufferGraphics = image.getGraphics();

                    for (Entity entity: toRender){
                        if (insideCamera(entity)){
                            int entityPortionHeight = (int)(entity.getHeight()/ NUM_THREADS);
                            int entityWidth = (int)entity.getWidth();
                            int entityX = (int)entity.getX();
                            int entityY = (int)entity.getY();

                            java.util.List<Callable<Boolean>> tasks = new ArrayList<>();
                            for (int i = 0; i < NUM_THREADS; i++){
                                final int count = i;
                                tasks.add(() -> bufferGraphics.drawImage(entity.getTexture(),
                                        entityX,entityY+entityPortionHeight*count, entityX+entityWidth, entityY+entityPortionHeight*(count+1),
                                        0,entityPortionHeight*count, entityWidth, entityPortionHeight*(count+1),null));
                            }

                            pool.invokeAll(tasks);
                        }
                    }

                    front.add(image);
                    bufferGraphics.dispose();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally {
                pool.shutdown();
            }
        }
        
        private boolean insideCamera(Entity entity){
            return !(entity.getX() + entity.getWidth() < 0 || entity.getX() > Engine.getViewWidth()) ||
                    !(entity.getY() + entity.getHeight() < 0 || entity.getY() > Engine.getViewHeight());
        }

        private void kill(){
            running.set(false);
        }
    }

    private class Renderer implements Runnable{

        private final AtomicBoolean running = new AtomicBoolean(true);

        public void run(){
            try{
                while(running.get()){
                    Image image = front.take();
                    Graphics renderGraphics = getGraphics();

                    renderGraphics.drawImage(image, 0,0, RENDER_WIDTH, RENDER_HEIGHT,0, 0, Engine.getViewWidth(), Engine.getViewHeight(), null);

                    back.add(image);
                    renderGraphics.dispose();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        private void kill(){
            running.set(false);
        }
    }

    private static class KeyInputListener extends KeyAdapter{

        CopyOnWriteArraySet<Integer> inputBuffer;

        private KeyInputListener(CopyOnWriteArraySet<Integer> inputBuffer){
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
