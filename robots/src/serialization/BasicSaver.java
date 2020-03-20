package serialization;

import gui.GameWindow;
import gui.LogWindow;
import gui.RobotState;
import log.LogEntry;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class BasicSaver
{
    public LogFieldInfo getLogFieldInfo(LogWindow window)
    {
        int x = window.getX();
        int y = window.getY();
        Dimension size = window.getSize();
        boolean isIcon = window.isIcon();
        boolean isMaximum = window.isMaximum();

        LinkedList<LogEntry> logInfo = new LinkedList<LogEntry>();
        Iterable<LogEntry> iter = window.getLogInfo();
        for (LogEntry current : iter)
        {
            logInfo.add(current);
        }

        return new LogFieldInfo(x, y, size.height, size.width, isIcon, isMaximum, logInfo);
    }

    public GameFieldInfo getGameFieldInfo(GameWindow window)
    {
        int x = window.getX();
        int y = window.getY();
        Dimension size = window.getSize();
        boolean isIcon = window.isIcon();
        boolean isMaximum = window.isMaximum();

        RobotState state = window.getRobotState();
        Point2D.Double target = window.getTargerCoordinates();

        return new GameFieldInfo(x, y, size.height, size.width, isIcon,
                isMaximum, state.x, state.y, state.direction, target.x, target.y);
    }

}
