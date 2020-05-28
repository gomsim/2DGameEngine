package Game;

import Logic.Engine;
import Logic.Entity;
import Logic.GravityComponent;
import Logic.Utility;

import java.awt.*;

public class Player extends Entity {

    private int spriteCounter;
    private double maxSpeed = 12;
    private double thrustForce = maxSpeed * 0.06;
    private static final int THRUSTING = 1;
    private static final int SHOOTING = 2;
    private static final int BOTH = THRUSTING + SHOOTING;
    private int state;
    private int bombCooldown;

    public Player(int x, int y){
        super(x,y,32*3,32*3, "GameResources/Plane.png", 32, 32);
        register(new GravityComponent());
        setVelocity(maxSpeed,0);
    }

    public Image getTexture(){
        int y = 0;
        double rot = Utility.angle(getVelX(),getVelY());
        if (getVelX() >= 0){
            if (rot >= -90 && rot <= -80){
                y = 11;
            }else if (rot >= -80 && rot <= -60){
                y = 14;
            }else if (rot >= -60 && rot <= -30){
                y = 4;
            }else if (rot >= -30 && rot <= -10){
                y = 3;
            }else if (rot >= -10 && rot <= 10){
                y = 0;
            }else if (rot >= 10 && rot <= 30){
                y = 1;
            }else if (rot >= 30 && rot <= 60){
                y = 2;
            }else if (rot >= 60 && rot <= 80){
                y = 13;
            }else if (rot >= 80 && rot <= 90){
                y = 10;
            }
        }else{
            if (rot >= -90 && rot <= -80){
                y = 10;
            }else if (rot >= -80 && rot <= -60){
                y = 12;
            }else if (rot >= -60 && rot <= -30){
                y = 9;
            }else if (rot >= -30 && rot <= -10){
                y = 8;
            }else if (rot >= -10 && rot <= 10){
                y = 5;
            }else if (rot >= 10 && rot <= 30){
                y = 6;
            }else if (rot >= 30 && rot <= 60){
                y = 7;
            }else if (rot >= 60 && rot <= 80){
                y = 15;
            }else if (rot >= 80 && rot <= 90){
                y = 11;
            }
        }

        return getTexture(spriteCounter % 3,y);
    }

    public void action(){
        super.action();

        //setRelativePosition(0,spriteCounter % 5 == 0 ? -3:3);

        if (bombCooldown != 0)
            bombCooldown--;

        switch (state){
            case THRUSTING:
                //TODO: Effect here
                double[] perpendicular = Utility.perpendicular(getVelX(), getVelY(), Utility.LEFT);
                double[] thrust = Utility.multiply(Utility.unitVector(perpendicular[Engine.X],perpendicular[Engine.Y]),thrustForce);
                addVelocityCapped(thrust[Engine.X],thrust[Engine.Y],maxSpeed);
                break;
            case SHOOTING:
                //TODO: Effect here
                if (bombCooldown == 0){
                    Bomb bomb = new Bomb(getX()+((double)getWidth())/2+getVelX()*10,getY()+((double)getHeight())/2+getVelY()*10);
                    bomb.setVelocity(getVelX()*1.7,getVelY()*1.7);
                    Engine.instance().add(bomb);
                    bombCooldown = 30;
                }
                break;
            case BOTH:
                //TODO: Effect here
                break;
        }
        if (spriteCounter % 5 == 0){
            Engine.instance().add(new Smoke(getX()+((double)getWidth())/2-getVelX()*10, getY()+((double)getHeight())/2-getVelY()*10, 16,16));
        }
       // System.out.println(getVelX() + " " + getVelY());

        spriteCounter++;
        state = 0;
    }

    public void thrust(){
        state += THRUSTING;
    }
    public void shoot(){
        state += SHOOTING;
    }
}
