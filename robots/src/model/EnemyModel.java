package model;

import java.util.ArrayList;

import gui.EnemyState;
import gui.RobotState;

public class EnemyModel implements RobotObserver{
    private double x;
    private double y;
    private double direction;
    private double fieldWidth;
    private double fieldHeight;
    private RobotState state;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.003;
    public static boolean isMeetPlayer = false;

    public EnemyModel(double x, double y, double direction, double width, double height)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;

        fieldWidth = width;
        fieldHeight = height;
    }
    
    public int getMinDistance()
    {
    	return 10;
    }
    public void setD(double direction)
    {
    	this.direction = direction;
    }
    
    public void setX(double X)
    {
    	this.x = X;
    }
    
    public void setY(double Y)
    {
    	this.y = Y;
    }

    public DoublePoint moveEnemy(double velocity, double angularVelocity, double duration)
    {
        velocity = Geometry.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = Geometry.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = x + velocity / angularVelocity *
                (Math.sin(direction  + angularVelocity * duration) -
                        Math.sin(direction));
        if (!Double.isFinite(newX))
        {
            newX = x + velocity * duration * Math.cos(direction);
        }
        double newY = y - velocity / angularVelocity *
                (Math.cos(direction  + angularVelocity * duration) -
                        Math.cos(direction));
        if (!Double.isFinite(newY))
        {
            newY = y + velocity * duration * Math.sin(direction);
        }
        if (newX > fieldWidth)
            newX -= fieldWidth;
        else
        if (newX < 0)
            newX = fieldWidth + newX;

        x = newX;

        if (newY > fieldHeight)
            newY -= fieldHeight;
        else
        if (newY < 0)
            newY = fieldHeight + newY;
        y = newY;
        direction = Geometry.asNormalizedRadians(direction + angularVelocity * duration);
        return new DoublePoint(x, y);
    }


    public EnemyState getEnemyState()
    {
        return new EnemyState(x, y, direction);
    }

    public double getMaxVelocity()
    {
        return maxVelocity;
    }

    public double getMaxAngularVelocity()
    {
        return maxAngularVelocity;
    }

    public void setFieldBorders(double width, double height)
    {
        fieldWidth = width;
        fieldHeight = height;
    }
    public RobotState getRobotState()
    {
    	return state;
    }

	@Override
	public void onCoordinateChange(RobotState state) {
		this.state  = state;
	}
}
