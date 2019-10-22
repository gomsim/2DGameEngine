package Logic;

import Graphics.Window;

public class Game {

    private Window window;
    private static final int FPS = 60;

    public static void main(String[] args){
        new Game().run();
    }

    private void run(){
        window = new Window();

        boolean running = true;
        int tickInterval = 1000 / FPS;
        long nextTick;
        long delay;
        int x = 0;
        while (running){
            nextTick = System.currentTimeMillis() + tickInterval;

            System.out.println(x++);
            //Do all object's tic()

            //Checkc if delay or next frame
            delay = (nextTick - System.currentTimeMillis());
            if (delay > 0){
                try {
                    Thread.sleep(delay);
                }catch(InterruptedException e){
                    System.out.println("Game loop thread interrupted");
                }
            }
        }
    }
}
