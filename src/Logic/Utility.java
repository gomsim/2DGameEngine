package Logic;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import static Logic.Utility.Direction.LEFT;

public class Utility {

    public enum Direction{
        LEFT, RIGHT;
    }

    public static final int X = 0, Y = 1;

    public static double[] perpendicular(double x, double y, Direction dir){
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
        result[X] = vec[X] * multiplier;
        result[Y] = vec[Y] * multiplier;
        return result;
    }
    public static double[] add(double[] a, double[] b){
        double[] result = new double[2];
        result[X] = a[X] + b[X];
        result[Y] = a[Y] + b[Y];
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

    public static double clamp(double val, double min, double max){
        return Math.max(min, Math.min(val, max));
    }

    public static double repel(double val, double lower, double upper){
        if (val < upper && val > lower){
            return val - lower < upper - val ? lower:upper;
        }
        return val;
    }

    public static double[] vector(double angle){
        angle = Math.toRadians(angle);
        double[] vec = new double[2];
        vec[X] = Math.cos(angle);
        vec[Y] = Math.sin(angle);
        return unitVector(vec[X],vec[Y]);
    }

    public static BufferedImage resize(BufferedImage src, int w, int h, double sW, double sH) {
        BufferedImage scaledImage = new BufferedImage((int)(src.getWidth()*sW), (int)(src.getHeight()*sH), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        AffineTransform xform = AffineTransform.getScaleInstance(sW,sH);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        graphics2D.drawImage(src, xform, null);
        graphics2D.dispose();
        return scaledImage;
    }

    /*
     * Compensates for z^2 < 1
     */
    public static double inverseSquare(double z){ //TODO: This needs a little love. Should probably use the repel method instead.
        boolean positive = z >= 0;
        z += positive? 1:(-1);
        double square = Math.pow(z,2);
        return positive? 1/square:square;
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

        private final double offset;
        private final double toRangeSize;

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
