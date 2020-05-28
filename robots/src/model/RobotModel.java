package model;

import gui.RobotState;

import java.awt.Point;
import java.util.ArrayList;

public class RobotModel implements RobotObservable
{
    private double m_robotPositionX;
    private double m_robotPositionY;
    private double m_robotDirection;
    private double fieldWidth;
    private double fieldHeight;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.0025;

    private final ArrayList<RobotObserver> observers;

    public RobotModel(double x, double y, double direction, double width, double height)
    {
        m_robotPositionX = x;
        m_robotPositionY = y;
        m_robotDirection = direction;

        fieldWidth = width;
        fieldHeight = height;

        observers = new ArrayList<>();
    }
    
    public static int getMinDistance()
    {
    	return 20;
    }

    public DoublePoint moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = Geometry.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = Geometry.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }

        if (newX > fieldWidth)
            newX -= fieldWidth;
        else
        if (newX < 0)
            newX = fieldWidth + newX;

        m_robotPositionX = newX;

        if (newY > fieldHeight)
            newY -= fieldHeight;
        else
        if (newY < 0)
            newY = fieldHeight + newY;

        m_robotPositionY = newY;

        m_robotDirection = Geometry.asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        notifyObservers();
        return new DoublePoint(m_robotPositionX, m_robotPositionY);
    }
    
    public void setX(double x)
    {
    	this.m_robotPositionX = x;
    }
    
    public void setY(double y)
    {
    	this.m_robotPositionY = y;
    }
    public void setD(double d)
    {
    	m_robotDirection = d;
    }

    @Override
    public void registerObserver(RobotObserver obs)
    {
        observers.add(obs);
    }

    @Override
    public void unregisterObserver(RobotObserver obs)
    {
        observers.remove(obs);
    }

    @Override
    public void notifyObservers()
    {
        RobotState state = getRobotState();
        for (RobotObserver obs : observers)
        {
            obs.onCoordinateChange(state);
        }
    }

    public RobotState getRobotState()
    {
        return new RobotState(m_robotPositionX, m_robotPositionY, m_robotDirection);
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
}
