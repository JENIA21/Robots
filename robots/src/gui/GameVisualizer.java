package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;

import javax.swing.JPanel;

import log.Logger;
import logic.Robot;
import logic.RobotLogic;
import logic.TargetPoint;
import utilities.parsedclass.GameVisualizerData;

public class GameVisualizer extends JPanel {
    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile List<Robot> m_robots = new Vector<>();
    private volatile int m_selectedRobot;
    private volatile List<Rectangle> m_rectangles = new Vector<>();
    private volatile Point m_firthCorner = null;

    public GameVisualizer() {
        m_robots.add(new Robot(100, 100, "Robot 0"));
        m_selectedRobot = 0;
        Timer m_timer = initTimer();
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
                onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClickerHandler(e);
            }
        });
        setDoubleBuffered(true);
    }

    public void addRobot() {
        var robot = new Robot(100, 100, "Robot" + m_robots.size());
        m_robots.add(robot);
    }
    public GameVisualizerData getModel(){
        var gvd = new GameVisualizerData(m_robots, m_selectedRobot, m_rectangles);
        return gvd;
    }
    public void setModel(GameVisualizerData gvd){
        m_robots.clear();
        m_robots.addAll(gvd.robots);
        m_selectedRobot = gvd.selectedRobot;
        m_rectangles.clear();
        m_rectangles.addAll(gvd.rectangles);
    }
    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    protected void onModelUpdateEvent() {
        m_robots.replaceAll(robot -> {
            if (robot.target != null)
                return RobotLogic.getNextStep(robot);
            return robot;
        });
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < m_robots.size(); i++) {
            var robot = m_robots.get(i);
            drawRobot(g2d, robot.x, robot.y, robot.direction);
            if (robot.target != null)
                drawTarget(g2d, robot.target.x, robot.target.y);
        }
        for (var rect : m_rectangles)
            drawRect(g2d, rect);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, double dx, double dy, double direction) {
        int x = (int)dx;
        int y = (int)dy;
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

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private void drawRect(Graphics2D g, Rectangle rect) {
        g.setColor(Color.BLACK);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    private void mouseClickerHandler(MouseEvent e) {
        var mousePoint = e.getPoint();
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (e.isShiftDown()) {
                if (m_firthCorner == null)
                    m_firthCorner = mousePoint;
                else {
                    Rectangle rect = new Rectangle();
                    rect.x = Math.min(m_firthCorner.x, mousePoint.x);
                    rect.y = Math.min(m_firthCorner.y, mousePoint.y);
                    rect.width = Math.abs(m_firthCorner.x - mousePoint.x);
                    rect.height = Math.abs(m_firthCorner.y - mousePoint.y);
                    m_rectangles.add(rect);
                    repaint();
                    Logger.info("Добавлен прямоугольник");
                    m_firthCorner = null;
                }
                return;
            }

            Logger.debug("Новая цель для " + m_robots.get(m_selectedRobot).name + ":" +
                    "\nx: " + mousePoint.x +
                    "\ny: " + mousePoint.y);
            if(m_robots.get(m_selectedRobot).target == null)
                m_robots.get(m_selectedRobot).target = new TargetPoint(0,0);
            m_robots.get(m_selectedRobot).target.x = mousePoint.x;
            m_robots.get(m_selectedRobot).target.y = mousePoint.y;
            m_robots.get(m_selectedRobot).targetAchieved = false;
            repaint();
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            if (e.isShiftDown()) {
                for(var rect : m_rectangles) {
                    if (rect.contains(mousePoint)) {
                        m_rectangles.remove(rect);
                        Logger.info("Прямоугольник удален");
                        return;
                    }
                }
                return;
            }
            double minDistance = RobotLogic.getDistanceToMouse(m_robots.get(0), mousePoint);
            m_selectedRobot = 0;
            for (int i = 1; i < m_robots.size(); i++) {
                var robot = m_robots.get(i);
                double distance = RobotLogic.getDistanceToMouse(robot, mousePoint);
                if (RobotLogic.getDistanceToMouse(robot, mousePoint) < minDistance) {
                    minDistance = distance;
                    m_selectedRobot = i;
                }
            }
            Logger.info("Выбран " + m_robots.get(m_selectedRobot).name);
        }
    }
}
