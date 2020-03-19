/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3206.FinalArtemis.commands;

import org.usfirst.frc3206.FinalArtemis.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class HatchDeploy extends InstantCommand {
  public HatchDeploy() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.intake.HatchDeploy();
  }
}
