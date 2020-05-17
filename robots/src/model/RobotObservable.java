package model;

public interface RobotObservable
{
    public void registerObserver(RobotObserver obs);

    public void unregisterObserver(RobotObserver obs);

    public void notifyObservers();
}
