/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc3206.FinalArtemis.commandgroups;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc3206.FinalArtemis.commands.*;

public class LeftDoubleHatch extends CommandGroup {
  /**
   * Add your docs here.
   */
  public LeftDoubleHatch() {
    addSequential(new AutoTurn("right", 30));
    addSequential(new AutoDrive("forward", 5));
    addSequential(new AutoDrive("reverse", 1));
    addSequential(new AutoTurn("left", 120));
    addSequential(new AutoDrive("forward", 10));
    addSequential(new AutoTurn("left", 15));
    addSequential(new AutoDrive("forward", 2));
    addSequential(new ElevatorUp(6));

    // Add Commands here:
    // e.g. addSequential(new Command1());
    // addSequential(new Command2());
    // these will run in order.

    // To run multiple commands at the same time,
    // use addParallel()
    // e.g. addParallel(new Command1());
    // addSequential(new Command2());
    // Command1 and Command2 will run in parallel.

    // A command group will require all of the subsystems that each member
    // would require.
    // e.g. if Command1 requires chassis, and Command2 requires arm,
    // a CommandGroup containing them would require both the chassis and the
    // arm.
  }
}
