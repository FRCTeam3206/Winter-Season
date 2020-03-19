// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3206.FinalArtemis.subsystems;


//import org.usfirst.frc3206.FinalArtemis.commands.*;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.PIDOutput;
//import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Climb extends Subsystem {

    private DoubleSolenoid climbSol1;
    private Solenoid climbSol2;
    private Spark climbMotor;

    public Climb() {
        
        climbSol1 = new DoubleSolenoid(0, 3, 4);
        addChild("ClimbSol1",climbSol1);
        
        climbSol2 = new Solenoid(0, 5);
        addChild("ClimbSol2",climbSol2);

        climbMotor = new Spark(1);//may need to be changed
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void Clamp() {
        climbSol1.set(Value.kForward);
        climbSol2.set(true);
    }

    public void Unclamp() {
        climbSol1.set(Value.kReverse);
        climbSol2.set(false);
    }

    public void Deploy() {
        climbMotor.set(.5);
    }

    public void Retract() {
        climbMotor.set(-.5);
    }

    public void stop() {
        climbMotor.set(0);
    }
}

