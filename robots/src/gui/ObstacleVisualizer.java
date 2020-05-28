package gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class ObstacleVisualizer {
    private int fieldWidth;
    private int fieldHeight;
    private static final int[] POLY_X = { 20, 40, 60, 55, 60, 24, 4, 0 };
    private static final int[] POLY_Y = { 0, 7, 20, 40, 80, 70, 40, 0 };
    private static final Color ASTEROID_COLOR = Color.decode("#52575D");
    private Image image;
    private int x;
    private int y;
    

    public ObstacleVisualizer(double width, double height)
    {
        changeSize(width, height);
    }
    
    public int getMinDistance()
    {
    	return 50;
    }
    
    public void changeSize(double width, double height)
    {
    	fieldWidth = (int)width;
        fieldHeight = (int)height;
    }

    public void drawRObstacle(Graphics2D g, int x, int y, double direction)
    {
        Polygon poly = new Polygon(POLY_X, POLY_Y, 8);
        image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ASTEROID_COLOR);
        g2.fill(poly);
        g2.dispose();
        g.drawImage(image, x - 20, y, null);
    }

}
