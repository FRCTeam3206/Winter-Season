package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TimedCenter extends CommandGroup {

    public TimedCenter() {
        // Add Commands here:
    	RobotMap.gameData = DriverStation.getInstance().getGameSpecificMessage();
    	System.out.println("---------------------Game Data: " + RobotMap.gameData);
    	if (RobotMap.gameData.charAt(0) == 'L') {
    		addParallel(new Camera());
    		addSequential(new TimedDrive(10));
    		addSequential(new TimedTurn(45, "right"));
    		addSequential(new TimedDrive(125));
    		addSequential(new TimedTurn(45, "left"));
    		addSequential(new Lift("up", RobotMap.SwitchTime));
    		addSequential(new TimedDrive(15));
    		addSequential(new Extake());
    		System.out.println("--------------------------------left center");
    	} else if (RobotMap.gameData.charAt(0) == 'R') {
    		addParallel(new Camera());
    		addSequential(new TimedDrive(10));
    		addSequential(new TimedTurn(45, "left"));
    		addSequential(new TimedDrive(125));
    		addSequential(new TimedTurn(45, "right"));
    		addSequential(new Lift("up", RobotMap.SwitchTime));
    		addSequential(new TimedDrive(15));
    		addSequential(new Extake());    		
    		System.out.println("-------------------------------right center");
    	}
    	else {
    		addParallel(new Camera());
    		addSequential(new Lift("up", RobotMap.SwitchTime));
    		addSequential(new TimedDrive(140));
    		addSequential(new Extake());
    		System.out.println("-------------------------else center");
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
