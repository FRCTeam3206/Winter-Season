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
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.PIDOutput;
//import edu.wpi.first.wpilibj.PIDSource;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Solenoid;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Intake extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_VictorSPX leftIntake = new WPI_VictorSPX(6);
    private WPI_VictorSPX rightIntake = new WPI_VictorSPX(2);
    private DifferentialDrive intakeDrive = new DifferentialDrive(leftIntake, rightIntake);
    private Solenoid hatchDeploy;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Intake() {
        leftIntake = new WPI_VictorSPX(6);
        rightIntake = new WPI_VictorSPX(2);
        hatchDeploy = new Solenoid(0, 2);
        addChild("HatchDeploy",hatchDeploy);
    }

    @Override
    public void initDefaultCommand() {

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void IntakeDrive(double Speed) {	
    	intakeDrive.tankDrive(Speed, Speed);
    }
    
    public void BallIn() {
    	intakeDrive.tankDrive(-.7, -.7);
    }
    
    public void BallOut() {
    	intakeDrive.tankDrive(1, 1);
    }

    public void HatchDeploy() {
        hatchDeploy.set(true);
    }

    public void HatchRetract() {
        hatchDeploy.set(false);
    }
    
    public void stop() {
    	intakeDrive.tankDrive(0, 0);
    }
}

