/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3206.FinalHobbes.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DoubleHatch extends CommandGroup {
  /**
   * Add your docs here.
   */
  public DoubleHatch() {
    addSequential(new AutoDrive("forward", 5));
    addSequential(new AutoTurn("right", 30));
    addSequential(new AutoDrive("forward", 6));
    addSequential(new AutoTurn("left", 30));
    addSequential(new AutoDrive("forward", 1.1));
    //add hatch deploy
    addSequential(new AutoDrive("reverse", 1));
    addSequential(new AutoTurn("left", 125));
    addSequential(new AutoDrive("forward", 14));
    addSequential(new AutoTurn("left", 40));
    addSequential(new AutoDrive("forward", 3.2));
    //something something hatch
    addSequential(new AutoDrive("reverse", 3));
    addSequential(new AutoTurn("left", 141));
    addSequential(new AutoDrive("forward", 16));
    addSequential(new AutoTurn("right", 63));







  }
}
