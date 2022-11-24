package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import log.Logger;
import logic.Robot;
import logic.RobotLogic;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();
    
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    private volatile List<Robot> m_robots = new ArrayList<>();
    private volatile Point m_targetPosition = new Point(150, 100);
    private final RobotLogic m_robotLogic = new RobotLogic();
    public GameVisualizer()
    {
        m_robots.add(new Robot(100, 100, "Robot 1"));
        m_robots.add(new Robot(100, 200, "Robot 2"));
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 5);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                m_targetPosition = e.getPoint();
                Logger.debug("Позиция цели изменена:" +
                        "\nx: " + m_targetPosition.x +
                        "\ny: " + m_targetPosition.y);
                for (var robot : m_robots)
                    robot.targetAchieved = false;
                repaint();
            }
        });
        setDoubleBuffered(true);
    }
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    protected void onModelUpdateEvent()
    {
        for(int i = 0; i < m_robots.size(); i++)
            m_robots.set(i, m_robotLogic.getNextStep(m_robots.get(i), m_targetPosition));
    }
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        for (var robot: m_robots)
            drawRobot(g2d, round(robot.x), round(robot.y), robot.direction);
        drawTarget(g2d, m_targetPosition.x, m_targetPosition.y);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x + 10, y, 5, 5);
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
}
