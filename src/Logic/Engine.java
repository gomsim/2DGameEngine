package Logic;

import Game.Sky;
import Graphics.Renderer;
import Graphics.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.PriorityBlockingQueue;

public class Engine {

    //TODO: Course handler: Takes large-ass course sprite from Aseprite and divides into 32/64-pix Entities
    //TODO: Need support for Z-axis In Basic Entity class because the order of rendering needs to be controlled
    //TODO:    eg. 1.skybox, 2.clouds, 3.ground, 4.character (reverse z-order)
    //TODO: Paralax effect depedency can take advantage of Z-value
    //TODO: Keep static constants of typical Z-values in entity
    //TODO:    eg. Z_HUD, Z_FOREGROUND, Z_CHARACTER, Z_PROPS, Z_BACKGROUND, Z_FAR_BACKGROUND, Z_SKYBOX
    //TODO: Engine needs to keep entities in a sorted list, eg. PriorityQueue (med en extern comparator baserad på z)

    private static Engine instance;
    private Entity focus;
    private Window window;
    private Renderer renderer = new Renderer();
    public static final int X = 0, Y = 1;
    public static final int FPS = 60;
    private static final int EXISTENCE_MARGIN = 1000;
    public static final double GRAVITY = 0.2;
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
            for (Entity entity: toRemove){
                entities.remove(entity);
            }
            toRemove.clear();

            //Do all object's tick()
            for (Entity entity: entities){
                entity.tick();
                if (entity.getX() < -EXISTENCE_MARGIN || entity.getX() > renderer.getWidth() + EXISTENCE_MARGIN || entity.getY() < -EXISTENCE_MARGIN || entity.getY() > renderer.getHeight() + EXISTENCE_MARGIN)
                    remove(entity);
            }

            //TakeInput
            CopyOnWriteArraySet<Integer> pressedKeys = keyInputBuffer;
            for (Integer key: pressedKeys){
                switch (key){
                    //all global functions before default
                    default:
                        if (inputFunctions.containsKey(key)){
                            for (Runnable func: inputFunctions.get(key)){
                                func.run();
                            }
                        }
                }
            }

            //Render new image
            renderer.repaint();

            //Collision detection loop here

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

    public static int getWidth(){
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }
    public static int getHeight(){
        return Toolkit.getDefaultToolkit().getScreenSize().height;
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
    public void setFocus(Entity focus){
        this.focus = focus;
    }
    public Entity getFocused(){
        return focus;
    }

    private class ZComparator implements Comparator<Entity> {
        public int compare(Entity first, Entity second){
            return (int)(second.getZ() - first.getZ());
        }
    }
}
