/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3206.FinalHobbes.commands;

import edu.wpi.first.wpilibj.command.ConditionalCommand;

import org.usfirst.frc3206.FinalHobbes.Robot;
import org.usfirst.frc3206.FinalHobbes.commands.AutoDrive;
public class Drive extends ConditionalCommand {
  double DistanceMoved = Robot.driveTrain.DistanceMoved();

  public Drive(String Direction, double Distance) {
    super(new TimedDrive(Direction, Distance), new AutoDrive(Direction, Distance));

  // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR

  }

  @Override
  protected boolean condition(){
    Robot.driveTrain.DistanceMoved();
    
    if (DistanceMoved == 0) {
      return true;
    } else {
      return false;
  }
 }
}
