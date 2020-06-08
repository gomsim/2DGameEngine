package Game;

import Logic.*;

import java.awt.*;

public class Player extends Entity {

    private int spriteCounter;
    // Offset to make the plane look like it shifts its body movement slightly before turning in space
    private Utility.Mapper rotationMapper = Utility.getCircularMapper(-180,180,0,15, 0);//-0.2);
    private double maxSpeed = 12;
    private double thrustForce = maxSpeed * 0.06;
    private static final int IDLE = 0;
    private static final int THRUSTING = 1;
    private static final int SHOOTING = 2;
    private static final int BOTH = THRUSTING + SHOOTING;
    private int environmentState;
    private int actionState;
    private int bombCooldown;

    public Player(int x, int y){
        super(x,y,32*3,32*3, "GameResources/Plane.png", 32, 32);
        register(new GravityComponent());
        register(new CameraFocusComponent(Engine.getViewHeight()/2,500,150));
        setVelocity(maxSpeed,0);
    }

    public Image getTexture(){
        double rot = Utility.angle(getVelX(),getVelY());
        int y = rotationMapper.mapInt(rot);
        return getTexture(spriteCounter % 4 == 3? 1:spriteCounter % 4,y);
    }

    public void action(){
        //Uncommenting this changes the thrustforce based on your current speed.
        thrustForce = 0.06 * Utility.magnitude(getVelX(),getVelY());
        if (bombCooldown != 0)
            bombCooldown--;

        switch (actionState){
            case THRUSTING:
                thrust();
                break;
            case SHOOTING:
                shoot();
                break;
            case BOTH:
                thrust();
                shoot();
                break;
        }
        //Note: More frequent smoke looks like the plane's damaged. Freq of 1 looks like near crash.
        if (spriteCounter % 5 == 0){
            double[] spawnPoint = getEdgePoint(Utility.angle(-getVelX(),-getVelY()));
            Engine.instance().add(new Smoke(spawnPoint[Engine.X],spawnPoint[Engine.Y]));
        }

        spriteCounter++;
        actionState = IDLE;
    }

    private void thrust(){
        double[] perpendicular = Utility.perpendicular(getVelX(), getVelY(), Utility.LEFT);
        double[] thrust = Utility.multiply(Utility.unitVector(perpendicular[Engine.X],perpendicular[Engine.Y]),thrustForce);
        addVelocityCapped(thrust[Engine.X],thrust[Engine.Y],maxSpeed);

    }
    private void shoot(){
        if (bombCooldown == 0){
            double[] spawnPoint = getEdgePoint(Utility.angle(getVelX(),getVelY()));
            Bomb bomb = new Bomb(spawnPoint[Engine.X],spawnPoint[Engine.Y], getVelX(), getVelY());

            Engine.instance().add(bomb);
            bombCooldown = 30;
        }
    }

    public void setThrusting(){
        actionState += THRUSTING;
    }
    public void setShooting(){
        actionState += SHOOTING;
    }
}
