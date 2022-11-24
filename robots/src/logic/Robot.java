package logic;

public class Robot {
    public double x;
    public double y;
    public double direction;
    public boolean targetAchieved = true;
    public String name;
    public Robot(double x, double y, String name){
        this.x = x;
        this.y = y;
        this.name = name;
        direction = 0;
    }
}
