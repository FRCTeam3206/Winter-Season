package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.Robot;
import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Lift extends Command {
	
	String ElevatorDirection;
	double ElevatorTime;
    public Lift(String Direction, double Time) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.elevator);
    	//ElevatorSpeed = Speed;
    	ElevatorDirection = Direction;
    	ElevatorTime = Time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if(ElevatorDirection ==  "down") {
        	Robot.elevator.Lift(RobotMap.ElevatordownSpeed);
        	Timer.delay(ElevatorTime);
    	}
    	else {
    		Robot.elevator.Lift(-RobotMap.ElevatorupSpeed);
    		Timer.delay(ElevatorTime);
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.elevator.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
