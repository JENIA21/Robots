package logic;

import log.Logger;

import java.awt.*;

public class RobotLogic {
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.004;

    public Robot getNextStep(Robot robot, Point target){
        double distance = distance(target.x, target.y,
                robot.x, robot.y);
        if (distance < 0.5)
        {
            if(!robot.targetAchieved)
                Logger.info(robot.name + ": Цель достигнута");
            robot.targetAchieved = true;
            return robot;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(robot.x, robot.y, target.x, target.y);
        double angularVelocity = 0;
        if (angleToTarget > robot.direction)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robot.direction)
        {
            angularVelocity = -maxAngularVelocity;
        }

        return moveRobot(robot, velocity, angularVelocity, 10);
    }

    private Robot moveRobot(Robot robot, double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robot.x + velocity / angularVelocity *
                (Math.sin(robot.direction  + angularVelocity * duration) -
                        Math.sin(robot.direction));
        if (!Double.isFinite(newX))
        {
            newX = robot.x + velocity * duration * Math.cos(robot.direction);
        }
        double newY = robot.y - velocity / angularVelocity *
                (Math.cos(robot.direction  + angularVelocity * duration) -
                        Math.cos(robot.direction));
        if (!Double.isFinite(newY))
        {
            newY = robot.y + velocity * duration * Math.sin(robot.direction);
        }
        robot.x = newX;
        robot.y = newY;
        robot.direction = asNormalizedRadians(robot.direction + angularVelocity * duration);
        return robot;
    }
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
}
