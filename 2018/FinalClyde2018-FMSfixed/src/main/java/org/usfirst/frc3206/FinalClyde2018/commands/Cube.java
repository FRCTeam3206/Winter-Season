package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.Robot;
import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Cube extends Command {
	
	String CubeDirection;
	
    public Cube(String Direction) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.intake);

    	CubeDirection = Direction;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (CubeDirection == "in") {
    		Robot.intake.CubeIn();
    		//Timer.delay(RobotMap.IntakeTime);
       	}
    	else if(CubeDirection == "out") {
    		Robot.intake.CubeOut();
    		//Timer.delay(RobotMap.IntakeTime);
    	} else {
    		Robot.intake.stop();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.intake.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
