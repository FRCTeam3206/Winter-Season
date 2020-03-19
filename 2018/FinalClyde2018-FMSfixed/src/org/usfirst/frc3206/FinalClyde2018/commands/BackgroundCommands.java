package org.usfirst.frc3206.FinalClyde2018.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class BackgroundCommands extends CommandGroup {

    public BackgroundCommands() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.
    	addParallel(new Camera());
    	addParallel(new DriveWithJoysticks());
    	//addParallel(new GameData());

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
