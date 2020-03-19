/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3206.FinalArtemis.commands;

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
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
    camera.setFPS(10);

    UsbCamera camera2 = CameraServer.getInstance().startAutomaticCapture();
    camera2.setFPS(15);

    UsbCamera camera3 = CameraServer.getInstance().startAutomaticCapture();
    camera3.setFPS(20);
    
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
}
