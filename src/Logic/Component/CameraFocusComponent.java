package Logic.Component;

import Logic.Entity;
import Logic.Utility;

public class CameraFocusComponent extends CameraComponent{

    private final double focusRadius;
    private final double focusX, focusY;

    public CameraFocusComponent(double focusX, double focusY, double focusRadius){
        this.focusX = focusX;
        this.focusY = focusY;
        this.focusRadius = focusRadius;
    }
    public CameraFocusComponent(Entity owner, double focusRadius){
        this(owner.getX(),owner.getY(),focusRadius);
    }
    public CameraFocusComponent(Entity owner){
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
        owner.setRelativePosition(-camMovX,-camMovY);
    }

}
