package model;

import gui.RobotState;

import java.awt.*;
import java.awt.geom.Point2D;

public class GameModel
{
    private RobotModel robot;
    
    private TargetModel target;

    private double fieldWidth;
    private double fieldHeight;

    public GameModel(double width, double height)
    {
        robot = new RobotModel(100, 100, 0, width, height);
        fieldWidth = width;
        fieldHeight = height;
        target = new TargetModel(150, 150);
    }

    public GameModel(double robotX, double robotY, double robotDirection,
                     double targetX, double targetY, double width, double height)
    {
        robot = new RobotModel(robotX, robotY, robotDirection, width, height);
        fieldWidth = width;
        fieldHeight = height;
        target = new TargetModel((int)targetX, (int)targetY);
    }
    
    public void setFieldSize(double sizeX, double sizeY)
    {
    	fieldWidth = sizeX;
    	fieldHeight = sizeY;
    	robot.setFieldBorders(sizeX, sizeY);
    }

    public void setTargetPosition(Point p)
    {
    	target.setPosition(p.x, p.y);
    }
    
    public Point getTargetPosition()
    {
    	return target.getPosition();
    }

    public RobotModel getRobotModel()
    {
    	return robot;
    }
    
    public TargetModel getTargetModel()
    {
    	return target;
    }
    
    public void onModelUpdateEvent()
    {
        RobotState state = robot.getRobotState();
        double distance = Geometry.distance(target.getPosition().x, target.getPosition().y, state.x, state.y);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = robot.getMaxVelocity();
        double angleToTarget = Geometry.angleTo(state.x, state.y, target.getPosition().x, target.getPosition().y);
        double angularVelocity = 0;
        if (angleToTarget > state.direction)
        {
            angularVelocity = robot.getMaxAngularVelocity();
        }
        if (angleToTarget < state.direction)
        {
            angularVelocity = -robot.getMaxAngularVelocity();
        }
        robot.moveRobot(velocity, angularVelocity, 10);
    }

    public RobotState getRobotState()
    {
        return robot.getRobotState();
    }

    public Point2D.Double getTargetCoordinates()
    {
        return new Point2D.Double(target.getPosition().x, target.getPosition().y);
    }
}
