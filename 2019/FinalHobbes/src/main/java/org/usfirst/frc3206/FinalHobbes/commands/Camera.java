/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3206.FinalHobbes.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.cameraserver.*;
import edu.wpi.cscore.*;

public class Camera extends Command {
  public Camera() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    UsbCamera sideCamera = CameraServer.getInstance().startAutomaticCapture();
      sideCamera.setFPS(15);

      UsbCamera groundCamera = CameraServer.getInstance().startAutomaticCapture();
      groundCamera.setFPS(7);

      UsbCamera frontCamera = CameraServer.getInstance().startAutomaticCapture();
      frontCamera.setFPS(15);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
      
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
