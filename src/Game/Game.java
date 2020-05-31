package Game;

import Graphics.Renderer;
import Logic.Engine;
import Logic.Utility;

import java.awt.event.KeyEvent;

public class Game {

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

        System.out.println("-> " + Utility.angle(1,0));
        System.out.println("\\> " + Utility.angle(1,1));
        System.out.println("v " + Utility.angle(0,1));
        System.out.println("</ " + Utility.angle(-1,1));
        System.out.println("<- " + Utility.angle(-1,0));
        System.out.println("<\\ " + Utility.angle(-1,-1));
        System.out.println("^ " + Utility.angle(0,-1));
        System.out.println("/> " + Utility.angle(1,-1));

        engine.run();
    }
}
