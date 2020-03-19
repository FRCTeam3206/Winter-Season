package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class RightStart extends CommandGroup {

    public RightStart() {
        // Add Commands here:
    	
    	//collect gameData
    	//addSequential(new GameData());
    	addParallel(new Camera());
    	
    	if(RobotMap.gameData.charAt(0) == 'R'){
    		//RightSwitchRight
    		addSequential(new MoveNoLoop(RobotMap.Start2Switch));
        	//addSequential(new Turn("left", 90));
    		addSequential(new TimedTurn(90, "left"));
        	addSequential(new Lift("up", RobotMap.SwitchTime));
        	addSequential(new MoveNoLoop(RobotMap.Turn2Switch));
        	addSequential(new Cube("out"));
    	} /*else if (RobotMap.gameData.charAt(1) == 'R') {
    		//go to scale
    		addSequential(new MoveNoLoop(RobotMap.Start2Scale));
    		//addSequential(new Turn("left", 90));
    		addSequential(new TimedTurn(90, "left"));
    		addSequential(new Lift("up", RobotMap.ScaleTime));
        	addSequential(new MoveNoLoop(RobotMap.Turn2Sccale)); 
        	addSequential(new Cube("out"));

    	} */else  if(RobotMap.gameData == null){
    		//drive straight
    		addSequential(new MoveNoLoop(RobotMap.AutoLine));
    	} else {
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
