package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.Robot;
import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TimedTurn extends Command {

	double startTime;
	double timePassed;
	double desiredAngle;
	double desiredTime;
	String direction;
	double leftSpeed = -.49;
	double rightSpeed = .49;
	double speed;
	
    public TimedTurn(double Angle, String Direction) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        desiredAngle = Angle;
        desiredTime = desiredAngle*RobotMap.timePerAngle;
        direction = Direction;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(direction == "left") {
    		speed = leftSpeed;
    	} else {
    		speed = rightSpeed;
    	}
    	startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	timePassed = Timer.getFPGATimestamp() - startTime;
    	Robot.driveTrain.Rotate(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(timePassed >= desiredTime) {
        	return true;
        } else {
        	return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();
    	SmartDashboard.putNumber("TimePassed", timePassed);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    	System.out.println("TimedTurn was interrupted");
    }
}
