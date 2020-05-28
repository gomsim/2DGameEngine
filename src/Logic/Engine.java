package Logic;

import Graphics.Renderer;
import Graphics.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class Engine {

    private static Engine instance;
    private Window window;
    private Renderer renderer;
    public static final int X = 0, Y = 1;
    private static final int FPS = 60;
    public static final double GRAVITY = 0.2;
    private CopyOnWriteArrayList<Entity> entities = new CopyOnWriteArrayList<>();
    public CopyOnWriteArraySet<Integer> keyInputBuffer = new CopyOnWriteArraySet<>();
    private HashMap<Integer,ArrayList<Runnable>> inputFunctions = new HashMap<>();

    private ArrayList<Entity> toAdd = new ArrayList<>();
    private ArrayList<Entity> toRemove = new ArrayList<>();

    public Engine(){
        instance = this;
    }

    public void run(){
        window = new Window(renderer, keyInputBuffer);
        boolean running = true;
        int tickInterval = 1000 / FPS;
        long nextTick;
        long delay;
        while (running){
            nextTick = System.currentTimeMillis() + tickInterval;

            //add and remove
            for (Entity entity: toAdd){
                entities.add(entity);
            }
            toAdd.clear();
            for (Entity entity: toRemove){
                entities.remove(entity);
            }
            toRemove.clear();

            //Do all object's tick()
            for (Entity entity: entities){
                entity.tick();
                if (entity.getX() < -1000 || entity.getX() > renderer.getWidth() + 1000 || entity.getY() < -1000 || entity.getY() > renderer.getHeight() + 1000)
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

    public CopyOnWriteArrayList<Entity> getEntities(){
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
    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }
}
