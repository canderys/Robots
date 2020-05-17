package gui;

import model.GameModel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();

    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile GameModel model;
    private volatile RobotVisualiser robotVisualiser;

    public GameVisualizer()
    {
        double width = getWidth();
        double height = getHeight();
        model = new GameModel(width, height);
        robotVisualiser = new RobotVisualiser(width, height);
        setUp();
    }

    public GameVisualizer(double robotX, double robotY, double direction, double targerX, double targerY)
    {
        double width = getWidth();
        double height = getHeight();
        model = new GameModel(robotX, robotY, direction, targerX, targerY, width, height);
        robotVisualiser = new RobotVisualiser(width, height);
        setUp();
    }

    public RobotState getRobotState()
    {
        return model.getRobotState();
    }

    public Point2D.Double getTargetCoordinates()
    {
        return model.getTargetCoordinates();
    }

    protected void setTargetPosition(Point p)
    {
        model.setTargetPosition(p);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        RobotState robotState = model.getRobotState();
        Point2D.Double targetPosition = model.getTargetCoordinates();
        Graphics2D g2d = (Graphics2D)g;
        robotVisualiser.drawRobot(g2d, round(robotState.x), round(robotState.y), robotState.direction);
        drawTarget(g2d, (int)targetPosition.x, (int)targetPosition.y);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private void setUp()
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                model.onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }
}
