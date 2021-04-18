package Game;

import Logic.Engine;
import Logic.TextureDivider;

import java.awt.event.KeyEvent;
import java.util.Random;

class Game {

    private static final Random RANDOM = new Random();

    public static void main(String[] args){
        new Game().run();
    }
    private void run(){
        Engine engine = Engine.instance();

        Player player = new Player(500,(int)(Engine.getViewHeight()*1.5));
        engine.add(player);

        engine.add(new Sky());
        for (int i = 0; i < 50; i++){
            engine.add(Cloud.createCloud(RANDOM.nextInt(Engine.getScreenWidth()), Engine.getScreenHeight()/2 -50 + RANDOM.nextInt(100),false));
        }
        for (int i = 0; i < 4; i++){
            engine.add(Cloud.createCloud(RANDOM.nextInt(Engine.getScreenWidth()), Engine.getScreenHeight()/2 -50 + RANDOM.nextInt(100),true));
        }

        engine.addAll(
                TextureDivider.divideAndConstruct(
                        "GameResources/Level1.png",
                        0,
                        Engine.getViewHeight()*1.5,
                        Ground.class)
        );

        engine.addKeyBinding(KeyEvent.VK_ESCAPE, engine::exit);
        engine.setName("Flight'n'bump");

        engine.run();
    }
}