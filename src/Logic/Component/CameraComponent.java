package Logic.Component;

public abstract class CameraComponent implements EntityComponent {

    private static double camVelX, camVelY;

    protected void moveCamera(double x, double y){
        camVelX = x;
        camVelY = y;
    }
    protected double camMovementX(){
        return camVelX;
    }
    protected double camMovementY(){
        return camVelY;
    }
}
