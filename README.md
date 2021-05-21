A simple and easy to work with 2D game engine.

![Gameplay](https://github.com/gomsim/2DGameEngine/blob/master/demo/flygplansdemo.gif)

With this Engine the only thing needed to create a simple game-like experience is to write a few Entity implementations, add them to the Engine, then start it up. Entity contains all lifecycle methods needed, such as update, destroy, and others used for scaling of textures, handling of sprite sheets, etc.
The flying experience included as an example in this repo consists only of three main Entity classes: Player, Sky and Cloud. These are simply added to the engine before it's started up, as shown in the pseudo code below, to create what's shown in the GIF above.

    engine.add(new Player());
    engine.add(new Sky());

    for (int i = 0; i < 50; i++){
        engine.add(new Cloud("Background"));
    }
    for (int i = 0; i < 4; i++){
        engine.add(new Cloud("Foreground"));
    }

    engine.run();


The Engine is set up in such a way that all the bells and whistles of it can be utilized by setting up the Entity classes in certain ways. There are a few concepts that are important to know during the update cycle of the Engine, which will steer the behaviour of the Entities being placed into the world.
<br></br>

<h3>UPDATE-METHOD</h3>
During each engine cycle every Entity's update-method will be called. It's a public method which is non-mandatory, but does nothing if not overridden. This method is the heart of the behaviour of the Entity. As an example, in the included flying game the update method of the Player-Entity will take care of counting down a bomb shooting cooldown, it will determine if it's time to release another puff of exhaust smoke from the rear of the player aircraft and it will determine the airplanes thrust based on user input. 
<br></br>

<h3>TEXTURE HANDLING</h3>
The Engine will each update cycle ask Entities for their texture by calling their getTexture-method. This method can be overridden to customize what will be returned. By providing the correct input (sprite sheet and size parameters) upon construction of an Entity, the getTexture-method can be made to use the built in getSubTexture(int x, int y)-method which makes it very easy to pick a sprite from a sprite sheet based on x and y coordinates.
<br></br>

<h3>MAPPER-OBJECTS</h3>
The Mapper is a utility class crucial for texture picking. It's a simple, efficient and elegant solution to the problem of mapping one range to another. In the case of the flying game the range of the angle of the velocity vector of the airplane (-180 to 180 degrees) is mapped to a range 0 to 15 (the height, in sprites, of the airplane sprite sheet). If preferred the mapper can provide the return value as the integer closest to the true mapping based on the input and thus make it possible to choose the sprite from the sheet which closest matches the airplanes movement direction.
<br></br>

<h3>ENTITY COMPONENTS</h3>
In order to make the Engine as flexible as possible I wanted less functionality to be hard coded into the Engine and instead let any functionality be added in a modular fashion. Thus the EntityComponent. The EntityComponent is an interface that provides a way to give many Entities certain properties. One such simple example is gravity. The GravityComponent implements the EntityComponent interface and gives Entitities the ability to experience gravity. This is done by, upon construction of an Entity, adding a new GravityComponent to it. 
The Entity will then be pulled down with a certain force every Engine update cycle. EntityComponent is a simple functional interface with a method apply(Entity). The Entity supplied to the method is the owner Entity, upon which the effect of the component will be applied.
Other components include CameraFollowComponent which makes the "camera" follow the Entity upon which is is added, CameraEffectComponent, which makes the Entity react to camera movement (used by the clouds in the flying game, and not the sky, for example) and a PixelColliderComponent to make Entities use pixel collision detection. In this specific implementation of the CameraEffectComponent, the Entities' z-property determines the magnitude of the movement effect, creating a convincing parallax effect.
<br></br>

<h3>KEY BINDING</h3>
Key binding is done by adding them to the Engine singleton object via addKeyBinding(KeyCode, Runnable). This is easiest done in the constructor of the Entity which will be affected by the key press. The Player class in the flying game adds bindings for thrust (arrowkey up) and shoot (spacebar) to methods defined in the class.
<br></br>

    Engine.instance().addKeyBinding(KeyEvent.VK_UP, this::setThrusting);
    Engine.instance().addKeyBinding(KeyEvent.VK_SPACE, this::setShooting);

<h3>SORTED COPY ON WRITE ARRAYLIST</h3>
For rendering and managing of all the entities in the world I needed an efficient, thread safe, sorted data structure. Sorted, because entities can define their own z-property, determining their position depth-wise. A sorted data structure ensures entities are rendered in the correct order, with the closest entities last; Efficient, because the structure will be read from, and written to, several times per update cycle; And thread safe because it will be accessed by three processes simultaneously. One process handling the engine update cycle, which will add and remove entities from the data structure, and two processes which handle rendering and which will only read from the structure.
