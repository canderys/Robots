package model;

import gui.EnemyState;
import gui.RobotState;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GameModel
{
    private RobotModel robot;
    
    private TargetModel target;

    private double fieldWidth;
    private double fieldHeight;
    private List<EnemyModel> enemyList;
    private List<ObstacleModel> obstacleList;
    private boolean isGameOver  = false;
    
    public boolean isGameOver()
    {
    	return isGameOver;
    }
    
    public List<EnemyModel> getEnemyList()
    {
    	return enemyList;
    }
    public List<ObstacleModel> getObstacleList()
    {
    	return obstacleList;
    }
    
    private void initListEnemy(double width, double height)
    {
    	EnemyModel enemy1 = new EnemyModel(50, 50, 0, width, height);
    	EnemyModel enemy2 = new EnemyModel(160, 50, 0, width, height);
    	EnemyModel enemy3 = new EnemyModel(89, 54, 0, width, height);
    	EnemyModel enemy4 = new EnemyModel(300, 300, 0, width, height);
    	enemyList = new ArrayList<EnemyModel>();
    	enemyList.add(enemy1);
    	enemyList.add(enemy2);
    	enemyList.add(enemy3);
    	enemyList.add(enemy4);
    	for(EnemyModel enemy: enemyList)
    	{
    		robot.registerObserver(enemy);
    	}
    }
    private List<ObstacleModel> initListObstacle(double widht, double height)
    {
    	ObstacleModel obstacle1 = new ObstacleModel(30, 135, widht, height);
    	ObstacleModel obstacle2 = new ObstacleModel(200, 200, widht, height);
    	ObstacleModel obstacle3 = new ObstacleModel(300, 250, widht, height);
    	obstacleList = new ArrayList<ObstacleModel>();
    	obstacleList.add(obstacle1);
    	obstacleList.add(obstacle2);
    	obstacleList.add(obstacle3);
    	return obstacleList;
    }

    public GameModel(double width, double height)
    {
        robot = new RobotModel(100, 100, 0, width, height);
        initListEnemy(width, height);
        initListObstacle(width, height);
        fieldWidth = width;
        fieldHeight = height;
        target = new TargetModel(150, 150);
        
    }

    public GameModel(double robotX, double robotY, double robotDirection,
                     double targetX, double targetY, double width, double height, List<EnemyModel> enemyList)
    {
        robot = new RobotModel(robotX, robotY, robotDirection, width, height);
        this.enemyList = enemyList;
        fieldWidth = width;
        fieldHeight = height;
        target = new TargetModel((int)targetX, (int)targetY);
        initListObstacle(width, height);
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
    
    private void updateRobot()
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
        DoublePoint pointToMove = robot.moveRobot(velocity, angularVelocity, 10);
        for(ObstacleModel obstacle: obstacleList)
        {
        	double distanceToObstacle = Geometry.distance(pointToMove.x, pointToMove.y, obstacle.getX(), 
        			obstacle.getY());
        	if(distanceToObstacle < obstacle.getMinDistance()) {
        		robot.setD(Geometry.asNormalizedRadians(state.direction + Math.PI));
        		pointToMove = robot.moveRobot(velocity, 0, 10);
        		break;
        	}
        }
        robot.setX(pointToMove.x);
        robot.setY(pointToMove.y);
    }
    
    private void updateEnemy()
    {
        for(EnemyModel enemy : enemyList)
        {
        	RobotState state = enemy.getRobotState();
        	EnemyState stateEnemy = enemy.getEnemyState();
        	double angleToPlayer = Geometry.angleTo(stateEnemy.x, stateEnemy.y, state.x, state.y);
        	double distanceToPlayer = Geometry.distance(state.x, 
        			state.y, stateEnemy.x, stateEnemy.y);
        	  if (distanceToPlayer < robot.getMinDistance())
              {
        		  isGameOver = true;
                  return;
              }
        	  double velocityEnemy = enemy.getMaxVelocity();
        	  double angularVelocityEnemy = 0;
              if (angleToPlayer > state.direction)
              {
            	  angularVelocityEnemy = enemy.getMaxAngularVelocity();
              }
              if (angleToPlayer < state.direction)
              {
            	  angularVelocityEnemy = -enemy.getMaxAngularVelocity();
              }
              DoublePoint pointToMove =  enemy.moveEnemy(velocityEnemy, angularVelocityEnemy, 10);
              for(ObstacleModel obstacle: obstacleList)
              {
              	double distanceToObstacle = Geometry.distance(pointToMove.x, pointToMove.y, obstacle.getX(), 
              			obstacle.getY());
              	if(distanceToObstacle < obstacle.getMinDistance()) {
              		enemy.setD(Geometry.asNormalizedRadians(state.direction + Math.PI));
              		pointToMove = enemy.moveEnemy(velocityEnemy, 0, 10);
              		break;
              	}
              }
              enemy.setX(pointToMove.x);
              enemy.setY(pointToMove.y);
              
        }
    }
    
    public void onModelUpdateEvent()
    {
    	updateRobot();
    	updateEnemy();
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
