package org.usfirst.frc3206.FinalClyde2018.commands;


import org.usfirst.frc3206.FinalClyde2018.Robot;
import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Extake extends Command {

	double startTime;
	double timePassed;
	double extakeTime = 3;
	
    public Extake() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	timePassed = Timer.getFPGATimestamp() - startTime;
    	Robot.intake.IntakeDrive(0.9);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (timePassed >= extakeTime) {
        	return true;
        } else {
        	return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    	System.out.println("Extake command was interrupted");
    }
}
