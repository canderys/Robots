package model;

import gui.RobotState;

public interface RobotObserver
{
    public void onCoordinateChange(RobotState state);
}
