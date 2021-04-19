package Logic;

import Rendering.Window;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Engine {

    private static Engine instance;
    
    private static final int FPS = 60;
    public static final int VIEW_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int VIEW_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    public static final int SCREEN_WIDTH = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getWidth();
    public static final int SCREEN_HEIGHT = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getHeight();

    private String name;
    private boolean running = true;

    private final SortedCopyOnWriteArrayList<Entity> entities = new SortedCopyOnWriteArrayList<>(new ZComparator());
    private final CopyOnWriteArraySet<Integer> keyInputBuffer = new CopyOnWriteArraySet<>();
    private final HashMap<Integer,ArrayList<Runnable>> inputFunctions = new HashMap<>();

    private final ArrayList<Entity> toAdd = new ArrayList<>();
    private final ArrayList<Entity> toRemove = new ArrayList<>();

    private Engine(){
        instance = this;
    }

    public void run(){
        Window window = new Window(keyInputBuffer, name);
        int tickInterval = 1000 / FPS;
        long nextTick;
        long delay;
        while (running){
            nextTick = System.currentTimeMillis() + tickInterval;

            //add and remove
            entities.add(toAdd.toArray(new Entity[0]));
            toAdd.clear();
            for (Entity entity : toRemove) {
                entity.destroy();
                entities.remove(entity);
            }
            toRemove.clear();

            //Do all objects' tick
            for (Entity entity : entities) {
                entity.tick();
            }

            //TODO: Kan möjligtvis behöva förflytta föremål explicit här istället för i deras tick ifall det som påverkar kameran inte ligger först i entities.

            //Take user input here
            CopyOnWriteArraySet<Integer> pressedKeys = keyInputBuffer;
            for (Integer key : pressedKeys) {
                if (inputFunctions.containsKey(key)) {
                    for (Runnable func : inputFunctions.get(key)) {
                        func.run();
                    }
                }
            }

            //Collision detection will go loop here

            //Render new image
            window.render();

            //Check if delay or next tick
            delay = (nextTick - System.currentTimeMillis());

            if (delay > 0){
                try {
                    Thread.sleep(delay);
                }catch(InterruptedException e){
                    System.out.println("Engine loop thread interrupted");
                }
            }
        }

        window.exit();
    }

    public void setName(String name){
        this.name = name;
    }

    public static Engine instance(){
        if (instance == null)
            instance = new Engine();
        return instance;
    }

    public void exit(){
        running = false;
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
    public void addKeyBinding(int key, Runnable func){
        if (!inputFunctions.containsKey(key))
            inputFunctions.put(key,new ArrayList<>());
        inputFunctions.get(key).add(func);
    }
    public void removeKeyBinding(int key, Runnable func){
        if (!inputFunctions.containsKey(key))
            return;
        inputFunctions.get(key).remove(func);
    }
    public void add(Entity entity){
        toAdd.add(entity);
    }
    public void addAll(Collection<Entity> entities){
        toAdd.addAll(entities);
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
