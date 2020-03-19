package org.usfirst.frc3206.FinalHobbes.commands;

import org.usfirst.frc3206.FinalHobbes.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TimedTurn extends Command {

	double startTime;
	double timePassed;
	int desiredAngle;
	double desiredTime;
	String direction;
	double leftSpeed = .5;
	double rightSpeed = -.5;
  double speed;
  double timePerDegree = .02531888;
	
    public TimedTurn(String Direction, int Angle) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        desiredAngle = Angle;
        desiredTime = desiredAngle * timePerDegree;
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
        SmartDashboard.putNumber("timePassed", timePassed);
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
