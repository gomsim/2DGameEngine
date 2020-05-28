package Logic;

public class Utility {

    public static final int LEFT = 0, RIGHT = 1;

    public static double[] perpendicular(double x, double y, int dir){
        if (dir == LEFT)
            return new double[] {y,-x};
        else
            return new double[] {-y,x};
    }

    public static double[] unitVector(double x, double y){
        double magnitude = magnitude(x,y);
        return new double[] {x/magnitude,y/magnitude};
    }
    public static double[] multiply(double[] vec, double multiplier){
        double[] result = new double[2];
        result[Engine.X] = vec[Engine.X] * multiplier;
        result[Engine.Y] = vec[Engine.Y] * multiplier;
        return result;
    }
    public static int sign(double number){
        if (number == 0)
            return 1;
        return (int)(number/Math.abs(number));
    }
    public static double magnitude(double x, double y){
        return Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
    }
    public static double angle(double x, double y){
        return Math.toDegrees(Math.atan(y/x));
    }
}
