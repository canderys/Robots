package model;

import gui.RobotState;

import java.awt.*;
import java.awt.geom.Point2D;

public class GameModel
{
    private RobotModel robot;

    private int m_targetPositionX;
    private int m_targetPositionY;

    private double fieldWidth;
    private double fieldHeight;

    public GameModel(double width, double height)
    {
        robot = new RobotModel(100, 100, 0, width, height);
        fieldWidth = width;
        fieldHeight = height;

        m_targetPositionX = 150;
        m_targetPositionY = 150;
    }

    public GameModel(double robotX, double robotY, double robotDirection,
                     double targetX, double targetY, double width, double height)
    {
        robot = new RobotModel(robotX, robotY, robotDirection, width, height);
        fieldWidth = width;
        fieldHeight = height;
        m_targetPositionX = (int)targetX;
        m_targetPositionY = (int)targetY;
    }

    public void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    public void onModelUpdateEvent()
    {
        RobotState state = robot.getRobotState();
        double distance = Geometry.distance(m_targetPositionX, m_targetPositionY, state.x, state.y);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = robot.getMaxVelocity();
        double angleToTarget = Geometry.angleTo(state.x, state.y, m_targetPositionX, m_targetPositionY);
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
        return new Point2D.Double(m_targetPositionX, m_targetPositionY);
    }
}
