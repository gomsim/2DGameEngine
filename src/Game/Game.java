package Game;

import Logic.Engine;

import java.awt.event.KeyEvent;
import java.util.Random;

public class Game {

    private static Random random = new Random();

    private Engine engine = new Engine();

    public static void main(String[] args){
        new Game().initiate();
    }
    private void initiate(){
        Player player = new Player(50, 500);
        engine.add(player);
        engine.setFocus(player);
        engine.registerKeyBinding(KeyEvent.VK_UP,()->player.setThrusting());
        engine.registerKeyBinding(KeyEvent.VK_SPACE,()->player.setShooting());

        engine.add(new Sky());
        for (int i = 0; i < 50; i++){
            engine.add(Cloud.createCloud((double)random.nextInt(Engine.getWidth()),(double)random.nextInt(300)));
        }

        engine.run();
    }
}