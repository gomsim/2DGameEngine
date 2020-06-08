package Logic.Component;

import Logic.Engine;
import Logic.Entity;
import Logic.Utility;

public class CameraFocusComponent implements EntityComponent{

    private double focusRadius;
    private double focusX, focusY;

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
        double camMovX = owner.getX() + owner.getVelX() - focusX;
        double camMovY = owner.getY() + owner.getVelY() - focusY;

        double distance = Utility.magnitude(camMovX, camMovY);
        double overshoot = distance - focusRadius;

        double[] unitMov = Utility.unitVector(camMovX, camMovY);

        camMovX = unitMov[0] * overshoot;
        camMovY = unitMov[1] * overshoot;

        Engine.instance().moveCamera(camMovX,camMovY);
        owner.setPosition(owner.getX()-camMovX,owner.getY()-camMovY);
    }

}
