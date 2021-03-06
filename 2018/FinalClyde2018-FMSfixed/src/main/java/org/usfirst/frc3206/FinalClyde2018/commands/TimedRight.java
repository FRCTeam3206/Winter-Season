package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TimedRight extends CommandGroup {

    public TimedRight() {
        // Add Commands here:
    	System.out.println("-----------------------------Game Data: " + RobotMap.gameData);
    	if (RobotMap.gameData.charAt(0) == 'R') {
    		addParallel(new Camera());
    		addSequential(new TimedDrive(110));
    		addSequential(new Lift("up", RobotMap.SwitchTime));
    		addSequential(new TimedDrive(30));
    		addSequential(new Extake());
    	} else {
    		addParallel(new Camera());
    		//addSequential(new Lift("up", RobotMap.SwitchTime));
    		addSequential(new TimedDrive(140));
    	}
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

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
