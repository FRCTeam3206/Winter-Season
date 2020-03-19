package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.Robot;
import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveNoLoop extends Command {

	//private final AnalogInput Encoder = RobotMap.driveTrainAnalogEncoder;
	double LastEncoder = RobotMap.driveTrainAnalogEncoder.getValue();
	double CurrentEncoder = RobotMap.driveTrainAnalogEncoder.getValue();
	double CurrentRotation = CurrentEncoder - LastEncoder;
	double TotalRotation = 0;
	double MoveError;
	double PreviousRotation = CurrentRotation;
	double max = 3925;
	double min = 0;
	double EncoderTolerance = 210;
	double EncoderUnitsPerInch = 166.58217377;//208.2277172119 encoder units per inch(use 3925 / 18.849555921539 to get more exact answer)
	double EncoderUnitsPerFt = 2498.7326065428;// not right //2498.7326065428 encoder units per foot(use 3925 / 18.84955592 * 12 to get more exact answer)
	double EncoderUnit = EncoderUnitsPerInch;//set to either "EncoderUnitsPerInch" or "EncoderUnitsPerFt"
	
	double DistanceMoved = 0;
	double DesiredDistance;
	
    public MoveNoLoop(double distance) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	DesiredDistance = distance;
    	requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	DistanceMoved = 0;
    	TotalRotation = 0;
    	PreviousRotation = 0;

    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	DistanceMoved = Math.abs(TotalRotation / EncoderUnit);
		CurrentEncoder = RobotMap.driveTrainAnalogEncoder.getValue();

		//calculate current rotation and add it to TotalRotation 
		if (LastEncoder<=(min+EncoderTolerance) && CurrentEncoder>=(max-EncoderTolerance)) {
			CurrentRotation = CurrentEncoder - LastEncoder - max; // account for crossing the absolute position reset
		} else if(LastEncoder>=(max-EncoderTolerance) && CurrentEncoder<=(min+EncoderTolerance)) {
			CurrentRotation = CurrentEncoder - LastEncoder + max; // account for crossing the absolute position reset
		} else {
			CurrentRotation = CurrentEncoder - LastEncoder;
		}
		TotalRotation = CurrentRotation + PreviousRotation;
		PreviousRotation = TotalRotation; //shift rotational sum to be the previous for the next iteration
		LastEncoder = CurrentEncoder;
		
		
	
		SmartDashboard.putNumber("Current Encoder", CurrentEncoder);
		SmartDashboard.putNumber("Distance Moved", DistanceMoved);
		SmartDashboard.putNumber("Total Rotation", TotalRotation);
	
		
		Robot.driveTrain.Drive(RobotMap.DriveSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (DistanceMoved >= DesiredDistance) {
        	return true;
        }
        else {
        	return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();
    	
    	
    	TotalRotation = 0;
    	PreviousRotation = 0;
    	DistanceMoved = 0;
    	
    	SmartDashboard.putNumber("Current Encoder", CurrentEncoder);
		SmartDashboard.putNumber("Total Rotation", TotalRotation);
		SmartDashboard.putNumber("Previous Rotation", PreviousRotation);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
