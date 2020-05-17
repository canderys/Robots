package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class RobotVisualiser
{
    private int fieldWidth;
    private int fieldHeight;

    public RobotVisualiser(double width, double height)
    {
        fieldWidth = (int)width;
        fieldHeight = (int)height;
    }

    public void drawRobot(Graphics2D g, int robotX, int robotY, double direction)
    {
        int robotCenterX = round(robotX);
        int robotCenterY = round(robotY);
        for (int i = -1; i <= 1; ++i)
            for (int j = -1; j <= 1; ++j)
            {
                AffineTransform t = AffineTransform.getRotateInstance(
                        direction, robotCenterX + i * fieldWidth, robotCenterY + j * fieldHeight);
                g.setTransform(t);
                g.setColor(Color.MAGENTA);
                fillOval(g, robotCenterX + i * fieldWidth, robotCenterY + j * fieldHeight, 30, 10);
                g.setColor(Color.BLACK);
                drawOval(g, robotCenterX + i * fieldWidth, robotCenterY + j * fieldHeight, 30, 10);
                g.setColor(Color.WHITE);
                fillOval(g, robotCenterX  + 10 + i * fieldWidth, robotCenterY + j * fieldHeight, 5, 5);
                g.setColor(Color.BLACK);
                drawOval(g, robotCenterX  + 10 + i * fieldWidth, robotCenterY + j * fieldHeight, 5, 5);
            }
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
}
