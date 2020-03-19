package org.usfirst.frc3206.FinalClyde2018.commands;

import org.usfirst.frc3206.FinalClyde2018.RobotMap;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class LeftStart extends CommandGroup {

    public LeftStart() {
        // Add Commands here:
    	
    	if(RobotMap.gameData.charAt(0) == 'L'){
    		//go to switch
        	addParallel(new Camera());
    		addSequential(new MoveNoLoop(RobotMap.Start2Switch));
        	//addSequential(new Turn("right", 90));
        	addSequential(new Lift("up", RobotMap.SwitchTime)); 
        	addSequential(new MoveNoLoop(RobotMap.Turn2Switch));
        	addSequential(new Cube("out"));
    	} 
    	/*else if (RobotMap.gameData.charAt(1) == 'L') {
    		//go to scale
        	addParallel(new Camera());
    		addSequential(new MoveNoLoop(RobotMap.Start2Scale));
    		//addSequential(new Turn("right", 90));
        	addSequential(new MoveNoLoop(RobotMap.Turn2Sccale));
        	addSequential(new Lift("up", RobotMap.ScaleTime)); //make sure we change this to scale height and add parameter to time it 
        	addSequential(new Cube("out")); 
    	} 
    	*/
    	else if(RobotMap.gameData == null){
    		//drive straight
        	addParallel(new Camera());
    		addSequential(new MoveNoLoop(RobotMap.AutoLine));
    	} 
    	else {
        	addParallel(new Camera());
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
