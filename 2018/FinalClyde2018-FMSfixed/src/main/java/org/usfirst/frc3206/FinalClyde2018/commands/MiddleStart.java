package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class MiddleStart extends CommandGroup {

    public MiddleStart() {
        // Add Commands here:
    	
    	if (RobotMap.gameData.charAt(0) == 'L') {
        	addParallel(new Camera());
    		addSequential(new MoveNoLoop(6));
    		//addSequential(new Turn("left", 58));
    		addSequential(new TimedTurn(58,"left"));
    		addSequential(new MoveNoLoop(RobotMap.Middle2SwitchLeft));
    		//addSequential(new Turn("right", 58));
    		addSequential(new TimedTurn(58, "right"));
    		addSequential(new Lift("up", RobotMap.SwitchTime /*&& RobotMap.Location == (2)*/));
    		addSequential(new MoveNoLoop(15));
    		addSequential(new Cube("out"));
    	}
    	else if (RobotMap.gameData.charAt(0) == 'R') {
        	addParallel(new Camera());
    		addSequential(new MoveNoLoop(18));
    		//addSequential(new Turn("right", 58));
    		addSequential(new TimedTurn(58, "right"));
    		addSequential(new MoveNoLoop(RobotMap.Middle2SwitchRight));
    		//addSequential(new Turn("left", 58));
    		addSequential(new TimedTurn(58, "left"));
    		addSequential(new Lift("up", RobotMap.SwitchTime /*&& RobotMap.Location == (2)*/));
    		addSequential(new MoveNoLoop(20));
    		addSequential(new Cube("out"));
    		addSequential(new Lift("down", RobotMap.SwitchTime /*&& RobotMap.Location == (2)*/));
    	} 
    	else  if(RobotMap.gameData == null){
    		//drive straight
        	addParallel(new Camera());
    		addSequential(new MoveNoLoop(RobotMap.AutoLine));
    	} 
    	else {
        	addParallel(new Camera());
    		addSequential(new Turn("left", 58));
    		addSequential(new MoveNoLoop(RobotMap.AutoLine));
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
