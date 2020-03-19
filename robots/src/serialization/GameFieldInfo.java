package serialization;

import java.awt.geom.Point2D;

public class GameFieldInfo extends FieldInfo
{
    public final double xRobot;
    public final double yRobot;
    public final double robotDirection;
    public final double xTarget;
    public final double yTarget;


    public GameFieldInfo(
            int x, int y, int height, int width,boolean isIcon,
            boolean isMaximised, double xRobot, double yRobot,
            double robotDirection, double xTarget, double yTarget)
    {
        super(x, y, height, width, isIcon, isMaximised);
        this.xRobot = xRobot;
        this.yRobot = yRobot;
        this.robotDirection = robotDirection;
        this.xTarget = xTarget;
        this.yTarget = yTarget;
    }

}
