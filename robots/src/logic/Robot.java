package logic;

import java.awt.*;

public class Robot {
    public double x;
    public double y;
    public double direction;
    public TargetPoint target = null;
    public boolean targetAchieved = true;
    public String name;
    public Robot(double x, double y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
        direction = 0;
    }
    public void setTarget(TargetPoint target){
        this.target = target;
    }
}
