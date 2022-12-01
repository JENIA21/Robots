package utilities.parsedclass;

import logic.Robot;

import java.awt.*;
import java.util.List;
import java.util.Vector;

public class GameVisualizerData {
    public volatile List<Robot> robots = new Vector<>();
    public volatile int selectedRobot;
    public volatile List<Rectangle> rectangles = new Vector<>();
    public GameVisualizerData(List<Robot> robots, int selectedRobot, List<Rectangle> rectangles){
        this.robots.clear();
        this.robots.addAll(robots);
        this.selectedRobot = selectedRobot;
        this.rectangles.clear();
        this.rectangles.addAll(rectangles);
    }
}
