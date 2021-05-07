package Logic.Component;

import Logic.Entity;
import Logic.Utility;

public class CameraEffectComponent extends CameraComponent{

    private final boolean affectsX, affectsY;

    public CameraEffectComponent(boolean affectsX, boolean affectsY){
        this.affectsX = affectsX;
        this.affectsY = affectsY;
    }
    public CameraEffectComponent(){
        this(true,true);
    }

    public void apply(Entity owner){
        double inverseSquare = Utility.inverseSquare(owner.getZ());
        double x = camMovementX() * inverseSquare;
        double y = camMovementY() * inverseSquare;
        owner.moveBy(affectsX? -x:0, affectsY? -y:0);
    }
}
