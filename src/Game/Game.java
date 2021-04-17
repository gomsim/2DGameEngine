package Game;

import Logic.Engine;

import java.awt.event.KeyEvent;
import java.util.Random;

class Game {

    private static Random random = new Random();

    private Engine engine = Engine.instance();

    public static void main(String[] args){
        new Game().run();
    }
    private void run(){
        Player player = new Player(500, 500);
        engine.add(player);

        engine.add(new Sky());
        for (int i = 0; i < 50; i++){
            engine.add(Cloud.createCloud(random.nextInt(Engine.getScreenWidth()),random.nextInt(300),false));
        }
        for (int i = 0; i < 4; i++){
            engine.add(Cloud.createCloud(random.nextInt(Engine.getScreenWidth()),random.nextInt(900),true));
        }

        /*engine.addAll(
                TextureDivider.divideAndConstruct(
                        "GameResources/Level1.png",
                        0,
                        Engine.getViewHeight()-64,
                        Ground.class)
        );*/

        engine.addKeyBinding(KeyEvent.VK_ESCAPE, engine::exit);

        engine.run();
    }
}