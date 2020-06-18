package Game;

import Logic.Engine;

import java.util.Random;

public class Game {

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
            engine.add(Cloud.createCloud((double)random.nextInt(Engine.getScreenWidth()),(double)random.nextInt(300),false));
        }
        for (int i = 0; i < 4; i++){
            engine.add(Cloud.createCloud((double)random.nextInt(Engine.getScreenWidth()),(double)random.nextInt(900),true));
        }

        engine.divideAndAdd(
                "GameResources/Ground.png",
                32,
                64,
                0,
                0,
                (x,y,texture) -> new Ground(x,y,texture));

        engine.run();
    }
}