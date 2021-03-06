package org.usfirst.frc3206.FinalHobbes.commands;

import org.usfirst.frc3206.FinalHobbes.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TimedDrive extends Command {

	double startTime;
	double timePassed;
	double desiredDistance;
	double desiredTime;
    double speed = .5;
    String desiredDirection; 
    double timeperFoot = .725;
	
    public TimedDrive(String Direction, double Distance) {//distance needs to be in inches
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        desiredDistance = Distance;
        desiredDirection = Direction;
        desiredTime = desiredDistance * timeperFoot;// * timeperInch or timeperFoot
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        timePassed = Timer.getFPGATimestamp() - startTime;
        SmartDashboard.putNumber("timePassed", timePassed);

        if (desiredDirection == "forward") {
            Robot.driveTrain.Drive(speed);
        } else if (desiredDirection == "reverse") {
            Robot.driveTrain.Drive(-speed);
        }
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
    	System.out.println("TimedDrive was interrupted");
    }
}
