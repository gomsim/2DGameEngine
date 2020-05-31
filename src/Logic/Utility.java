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
    public static double[] add(double[] a, double[] b){
        double[] result = new double[2];
        result[Engine.X] = a[Engine.X] + b[Engine.X];
        result[Engine.Y] = a[Engine.Y] + b[Engine.Y];
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
        return Math.toDegrees(Math.atan2(y,x));
    }

    //TODO: DOESN*T WORK!! May have something to do with the faulty angle-function
    public static double[] vector(double angle){
        angle = Math.toRadians(angle);
        double[] vec = new double[2];
        vec[Engine.X] = Math.cos(angle);
        vec[Engine.Y] = Math.sin(angle);
        return unitVector(vec[Engine.X],vec[Engine.Y]);
    }
    public static Mapper getMapper(double from, double to){
        return new Mapper(0,from,0,to);
    }
    public static Mapper getMapper(double fromStart, double fromEnd, double toStart, double toEnd){
        return new Mapper(fromStart,fromEnd,toStart,toEnd);
    }
    public static Mapper getCircularMapper(double from, double to, double offset){
        return new CircularMapper(0,from,0,to, offset);
    }
    public static Mapper getCircularMapper(double fromStart, double fromEnd, double toStart, double toEnd, double offset){
        return new CircularMapper(fromStart,fromEnd,toStart,toEnd, offset);
    }

    public static class Mapper{

        protected double ratio;
        protected double fromStart, toStart;

        private Mapper(double fromStart, double fromEnd, double toStart, double toEnd){
            this.fromStart = fromStart;
            this.toStart = toStart;
            // + 1 to include last indices
            ratio = (fromEnd - fromStart + 1) / (toEnd - toStart + 1);
        }

        public double mapDouble(double value){
            return ((value - fromStart) / ratio) + toStart;
        }
        public int mapInt(double value){
            return (int)mapDouble(value);
        }
    }
    public static class CircularMapper extends Mapper{

        private double offset;
        private double toRangeSize;

        private CircularMapper(double fromStart, double fromEnd, double toStart, double toEnd, double offset){
            super(fromStart,fromEnd,toStart,toEnd);
            this.offset = offset * ratio;
            // + 1 to include last index
            this.toRangeSize = toEnd - toStart + 1;
        }

        public double mapDouble(double value){
            return (((offset + value - fromStart) / ratio) + toStart) % toRangeSize;
        }
        public int mapInt(double value){
            return (int)mapDouble(value);
        }
    }
}
