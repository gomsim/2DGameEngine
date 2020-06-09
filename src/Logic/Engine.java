package Logic;

import Graphics.Renderer;
import Graphics.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Engine {

    //TODO: Course handler: Takes large-ass course sprite from Aseprite and divides into 32/64-pix Entities
    //TODO: Need support for Z-axis In Basic Entity class because the order of rendering needs to be controlled
    //TODO:    eg. 1.skybox, 2.clouds, 3.ground, 4.character (reverse z-order)
    //TODO: Paralax effect depedency can take advantage of Z-value
    //TODO: Keep static constants of typical Z-values in entity
    //TODO:    eg. Z_HUD, Z_FOREGROUND, Z_CHARACTER, Z_PROPS, Z_BACKGROUND, Z_FAR_BACKGROUND, Z_SKYBOX
    //TODO: Engine needs to keep entities in a sorted list, eg. PriorityQueue (med en extern comparator baserad på z)

    private static Engine instance;
    private Renderer renderer = new Renderer();
    public static final int X = 0, Y = 1;
    public static final int FPS = 60;
    private static final int EXISTENCE_MARGIN = 1000;
    public static final int VIEW_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int VIEW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int SCREEN_WIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    public static final int SCREEN_HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();
    public static final double GRAVITY = 0.2;
    private double camVelX, camVelY;
    private SortedCopyOnWriteArrayList<Entity> entities = new SortedCopyOnWriteArrayList<>(new ZComparator());
    public CopyOnWriteArraySet<Integer> keyInputBuffer = new CopyOnWriteArraySet<>();
    private HashMap<Integer,ArrayList<Runnable>> inputFunctions = new HashMap<>();

    private ArrayList<Entity> toAdd = new ArrayList<>();
    private ArrayList<Entity> toRemove = new ArrayList<>();

    public Engine(){
        instance = this;
    }

    public void run(){
        new Window(renderer, keyInputBuffer);
        boolean running = true;
        int tickInterval = 1000 / FPS;
        long nextTick;
        long delay;
        int i = 0;
        long starTime = System.currentTimeMillis();
        while (running){
            nextTick = System.currentTimeMillis() + tickInterval;

            //add and remove
            entities.add(toAdd.toArray(new Entity[0]));
            toAdd.clear();
            for (Entity entity : toRemove) {
                entities.remove(entity);
            }
            toRemove.clear();

            //Do all objects' tick()
            for (Entity entity : entities) {
                entity.tick();
                //if (entity.getX() < -EXISTENCE_MARGIN || entity.getX() > renderer.getWidth() + EXISTENCE_MARGIN || entity.getY() < -EXISTENCE_MARGIN || entity.getY() > renderer.getHeight() + EXISTENCE_MARGIN)
                //    remove(entity);
            }

            //TODO: Kan möjligtvis behöva förflytta föremål manuellt här istället för i deras tick ifall det som påverkar kameran inte ligger först i entities.

            //Take user input here
            CopyOnWriteArraySet<Integer> pressedKeys = keyInputBuffer;
            for (Integer key : pressedKeys) {
                if (inputFunctions.containsKey(key)) {
                    for (Runnable func : inputFunctions.get(key)) {
                        func.run();
                    }
                }
            }

            //Collision detection loop here

            //Render new image
            renderer.repaint();

            //Checkc if delay or next tick
            delay = (nextTick - System.currentTimeMillis());
            /*if (++i % 60 == 0){
                double time = (System.currentTimeMillis() - starTime);
                System.out.println("FPS Logic: " + 1000/(time / i));
            }*/

            if (delay > 0){
                try {
                    Thread.sleep(delay);
                }catch(InterruptedException e){
                    System.out.println("Engine loop thread interrupted");
                }
            }
        }
    }

    public static Engine instance(){
        if (instance == null)
            instance = new Engine();
        return instance;
    }

    public void moveCamera(double x, double y){
        camVelX = x;
        camVelY = y;
    }
    public double camMovementX(){
        return camVelX;
    }
    public double camMovementY(){
        return camVelY;
    }

    public static int getScreenWidth(){
        return SCREEN_WIDTH;
    }
    public static int getScreenHeight(){
        return SCREEN_HEIGHT;
    }
    public static int getViewWidth(){
        return VIEW_WIDTH;
    }
    public static int getViewHeight(){
        return VIEW_HEIGHT;
    }

    public SortedCopyOnWriteArrayList<Entity> getEntities(){
        return entities;
    }
    public void registerKeyBinding(int key, Runnable func){
        if (!inputFunctions.containsKey(key))
            inputFunctions.put(key,new ArrayList<>());
        inputFunctions.get(key).add(func);
    }
    public void add(Entity entity){
        toAdd.add(entity);
    }
    public void remove(Entity entity){
        toRemove.add(entity);
    }

    private class ZComparator implements Comparator<Entity> {
        public int compare(Entity first, Entity second){
            return Double.compare(first.getZ(),second.getZ());
        }
    }
}
