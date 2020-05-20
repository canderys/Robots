package model;

import java.awt.Point;
import java.util.ArrayList;

import gui.RobotState;

public class TargetModel implements TargetObservable{
	
	private int targetX;
	private int targetY;
	
	private final ArrayList<TargetObserver> observers;
	
	public TargetModel(int x, int y)
	{
		targetX = x;
		targetY = y;
		observers = new ArrayList<TargetObserver>();
	}
	
	public void setPosition(int x, int y)
	{
		targetX = x;
		targetY = y;
		notifyObservers();
	}
	
	public Point getPosition()
	{
		return new Point(targetX, targetY);
	}
	
	@Override
    public void registerObserver(TargetObserver obs)
    {
        observers.add(obs);
    }

    @Override
    public void unregisterObserver(TargetObserver obs)
    {
        observers.remove(obs);
    }

    @Override
    public void notifyObservers()
    {
        for (TargetObserver obs : observers)
        {
            obs.onTargetCoordinateChange(targetX, targetY);
        }
    }
}
