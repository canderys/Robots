package model;

import gui.RobotState;

public interface TargetObserver {
	public void onTargetCoordinateChange(int x, int y);
}
