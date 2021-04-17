A simple and easy to work with 2D game engine.

![Gameplay](https://j.gifs.com/XLm4L8.gif)

The only thing needed to create a simple game-like experience is to write a few Entity implementations, add them to the game engine, then start it up. Each new type of entity extends the abstract class Entity. Entity contains all lifecycle methods needed, such as update, destroy, and others used for scaling of textures, handling sprite sheets, etc.
The flying experience included as an example in this repo consists only of three classes: Player, Sky and Cloud. These are simply added to the engine before it's started, as shown below, to create what's shown in the GIF.

    private void run(){
        Player player = new Player(500, 500);
        engine.add(player);

        engine.add(new Sky());

        for (int i = 0; i < 50; i++){
            engine.add(
                Cloud.createCloud(
                    (double)random.nextInt(Engine.getScreenWidth()),
                    (double)random.nextInt(300),
                    false
                )
            );
        }
        for (int i = 0; i < 4; i++){
            engine.add(
                Cloud.createCloud(
                    (double)random.nextInt(Engine.getScreenWidth()),
                    (double)random.nextInt(900),
                    true
                )
            );
        }

        engine.run();
    }

