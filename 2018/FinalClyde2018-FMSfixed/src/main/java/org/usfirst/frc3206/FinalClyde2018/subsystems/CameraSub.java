package org.usfirst.frc3206.FinalClyde2018.subsystems;

import org.usfirst.frc3206.FinalClyde2018.commands.Camera;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class CameraSub extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Camera());
    }
}

