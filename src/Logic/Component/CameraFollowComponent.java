package Logic.Component;

import Logic.Entity;
import Logic.Utility;

public class CameraFollowComponent extends CameraComponent{

    private final double focusRadius;
    private final double focusX, focusY;

    public CameraFollowComponent(double focusX, double focusY, double focusRadius){
        this.focusX = focusX;
        this.focusY = focusY;
        this.focusRadius = focusRadius;
    }
    public CameraFollowComponent(Entity owner, double focusRadius){
        this(owner.getX(),owner.getY(),focusRadius);
    }
    public CameraFollowComponent(Entity owner){
        this(owner.getX(),owner.getY(),0);
    }

    public void apply(Entity owner){
        double ownerDirX = owner.getX() + owner.getVelX() - focusX;
        double ownerDirY = owner.getY() + owner.getVelY() - focusY;

        double distance = Utility.magnitude(ownerDirX, ownerDirY);
        double overshoot = distance - focusRadius;

        double[] unitDir = Utility.unitVector(ownerDirX, ownerDirY);
        double camMovX = unitDir[0] * overshoot;
        double camMovY = unitDir[1] * overshoot;

        moveCamera(camMovX,camMovY);
        owner.moveBy(-camMovX,-camMovY);
    }

}
