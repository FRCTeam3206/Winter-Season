// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3206.FinalHobbes.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc3206.FinalHobbes.Robot;

public class AutoDrive extends Command {
    double startTime;
    double timePassed;
    double timeperFoot = .725;
    double desiredTime;
    String desiredDirection;
    double DistanceMoved;
    double DesiredDistance;
    //compare last encoder value to new encoder value
    double LastDistance;
    double MovePerPulse;
   
    public AutoDrive(String Direction, double Distance) {
        DesiredDistance = Distance;
        desiredDirection = Direction;
        desiredTime = DesiredDistance * timeperFoot;
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    Robot.driveTrain.resetEncoders();
    startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {

          if (desiredDirection == "forward") {
              
            DistanceMoved = Robot.driveTrain.getLeftDistance() + Robot.driveTrain.getRightDistance();
            Robot.driveTrain.getLeftDistance();
            Robot.driveTrain.getRightDistance();

            Robot.driveTrain.Drive(.5);

            SmartDashboard.putNumber("DistanceMoved", DistanceMoved);
            //possible way to cut to timed drive if need be
            if (MovePerPulse == 0.0) {
                DistanceMoved = Timer.getFPGATimestamp() - startTime;
                Robot.driveTrain.Drive(-.5);
                SmartDashboard.putNumber("Runtime", DistanceMoved);
                desiredTime = DesiredDistance;
              }
          } 
          
          if (desiredDirection == "reverse") {
            LastDistance = DistanceMoved;
            DistanceMoved = -1 * (Robot.driveTrain.getLeftDistance() + Robot.driveTrain.getRightDistance());
            Robot.driveTrain.getLeftDistance();
            Robot.driveTrain.getRightDistance();

            //compare last encoder value to new encoder value to see if they stopped working
            MovePerPulse = DistanceMoved - LastDistance;

            Robot.driveTrain.Drive(-.5);

            SmartDashboard.putNumber("DistanceMoved", DistanceMoved);
            
            //possible way to cut to timed drive if need be
            if (MovePerPulse == 0.0) {
            DistanceMoved = Timer.getFPGATimestamp() - startTime;
            Robot.driveTrain.Drive(-.5);
            SmartDashboard.putNumber("Runtime", DistanceMoved);
            desiredTime = DesiredDistance;
          }
          } 
        }
    

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        if (DistanceMoved >= DesiredDistance) {
        	return true;
        }
        else {
        	return false;
        }
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
