package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class EnemyVisualizer {
    private int fieldWidth;
    private int fieldHeight;

    public EnemyVisualizer(double width, double height)
    {
        changeSize(width, height);
    }
    
    public void changeSize(double width, double height)
    {
    	fieldWidth = (int)width;
        fieldHeight = (int)height;
    }

    public void drawEnemy(Graphics2D g, int x, int y, double direction)
    {
    	Ellipse2D.Double circle = new Ellipse2D.Double(x, y, 15, 15);
    	g.setColor(Color.gray);
    	g.fill(circle);
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
