/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class AutoDrive extends Command {
  double desiredDistance;
  double distanceTraveled;
  int desiredDirection;
  public AutoDrive(int direction, double distance) {
    requires(Robot.driveTrain);
      desiredDistance = distance;
      desiredDirection = direction;//-1 is backwards, +1 is forwards
  }

  // Called just before this Command runs the first time
  protected void initialize() {
    RobotMap.leftEncoder.setPosition(0);
  }

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {	
        distanceTraveled = -RobotMap.leftEncoder.getPosition();
        Robot.driveTrain.Drive(RobotMap.driveSpeed * desiredDirection);
        SmartDashboard.putNumber("Distance Traveled", distanceTraveled);
  }

  // Make this return true when this Command no longer needs to run execute()
  protected boolean isFinished() {
          if ((distanceTraveled * desiredDirection) >= desiredDistance) {
            return true; 
        } else {
            return false;
        }
}

  // Called once after isFinished returns true
  protected void end() { 
    Robot.driveTrain.stop();
    RobotMap.leftEncoder.setPosition(0);
    SmartDashboard.putNumber("Distance Traveled", distanceTraveled);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {
    Robot.driveTrain.stop();
    RobotMap.leftEncoder.setPosition(0);
    SmartDashboard.putNumber("Distance Traveled", distanceTraveled);
  }
}
