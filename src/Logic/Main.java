package Logic;

import Game.Player;
import Graphics.Renderer;

import java.awt.event.KeyEvent;

public class Main {

    private Engine engine = new Engine();

    public static void main(String[] args){
        new Main().initiate();
    }
    private void initiate(){
        engine.setRenderer(new Renderer());
        Player player = new Player(50, 500);
        engine.add(player);
        engine.registerKeyBinding(KeyEvent.VK_UP,()->player.thrust());
        engine.registerKeyBinding(KeyEvent.VK_SPACE,()->player.shoot());

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
