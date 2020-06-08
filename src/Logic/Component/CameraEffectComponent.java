package Logic.Component;

import Logic.Engine;
import Logic.Entity;
import Logic.Utility;

public class CameraEffectComponent implements EntityComponent{

    private boolean affectsX, affectsY;

    public CameraEffectComponent(boolean affectsX, boolean affectsY){
        this.affectsX = affectsX;
        this.affectsY = affectsY;
    }
    public CameraEffectComponent(){
        this(true,true);
    }

    public void apply(Entity owner){
        double inverseSquare = Utility.inverseSquare(owner.getZ());
        double x = Engine.instance().camMovementX() * inverseSquare;
        double y = Engine.instance().camMovementY() * inverseSquare;
        owner.setRelativePosition(affectsX? -x:0, affectsY? -y:0);
    }
}
