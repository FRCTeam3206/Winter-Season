package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.Robot;
import org.usfirst.frc3206.FinalClyde2018.RobotMap;

//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Turn extends Command {

	//final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	double GyroAngle;
	double GyroCoefficient = 1.0; // as of 2/19/18 gyro doesn't need coefficient
	int DesiredAngle;
	String DesiredDirection;
    
	public Turn(String Direction, int Angle) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	DesiredDirection = Direction;
    	DesiredAngle = Angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//RobotMap.Gyro.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	/*
    	GyroAngle = Math.abs(GyroCoefficient *RobotMap.Gyro.getAngle());
		SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
		if (DesiredDirection == "left") {
			Robot.driveTrain.Rotate(-RobotMap.TurnSpeed);
		}
		else {
			Robot.driveTrain.Rotate(RobotMap.TurnSpeed);
		}
		*/
		
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (GyroAngle >= DesiredAngle) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();
    	//RobotMap.Gyro.reset();
    	SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}