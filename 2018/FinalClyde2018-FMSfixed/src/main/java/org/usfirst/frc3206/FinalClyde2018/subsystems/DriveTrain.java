// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3206.FinalClyde2018.subsystems;

import org.usfirst.frc3206.FinalClyde2018.RobotMap;
import org.usfirst.frc3206.FinalClyde2018.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class DriveTrain extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final VictorSP m_left = RobotMap.driveTrainLeftMotors;
    private final VictorSP m_right = RobotMap.driveTrainRightMotors;
    private final DifferentialDrive m_robotDrive = RobotMap.driveTrainDifferentialDrive;
    
    

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	
    
    
    //final ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	double GyroAngle;
	int counter = 0;
	int Stop = 0;
	double DriveSpeed = .4; //anything higher than .5 is probably insanely fast
	double RotationSpeed = 0.49; // needs to be .6 for carpet
	double TurnTolerance = 0; // stops pretty accurately within 2 degrees
	double GyroCoefficient = 1.0; // as of 2/19/18 gyro doesn't need coefficient
	double WaitCoefficient = 0.1;
	String right = "right";
	String left = "left";
	String forward = "forward";
	String reverse = "reverse";
	
	double LastEncoder = RobotMap.driveTrainAnalogEncoder.getValue();
	double CurrentEncoder = RobotMap.driveTrainAnalogEncoder.getValue();
	double CurrentRotation = CurrentEncoder - LastEncoder;
	double TotalRotation = 0;
	double MoveError;
	double PreviousRotation = CurrentRotation;
	double max = 3925;
	double min = 0;
	double EncoderTolerance = 50;
	double EncoderUnit = 1904.76;//158.73 = 1 inch; 1904.76 = 1ft
	double DistanceMoved = 0;
	
	double RightCal = 1.05;
	

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    	setDefaultCommand(new DriveWithJoysticks());
    	//setDefaultCommand(new BackgroundCommands());

    }

    public void takeJoystickInputs(Joystick left, Joystick right) {
    	m_robotDrive.tankDrive(left.getY() * .75, right.getY()*.75);
    	//m_robotDrive.arcadeDrive(left.getX() * .65, -right.getRawAxis(3) * .65);
    	
    }

 	public void Drive(double Speed) {
 		//m_robotDrive.arcadeDrive(-Speed, 0);
 		m_robotDrive.tankDrive(-Speed, -Speed);
 	}
 	
 	public void Rotate(double Speed) { 
 		//m_robotDrive.arcadeDrive(Speed, 0);
 		m_robotDrive.tankDrive(-Speed, Speed*RightCal);

 	}

    public void stop() {
    	m_robotDrive.tankDrive(0, 0);
    }
 	
    @Override
    public void periodic() {
        // Put code here to be run every loop
    	
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

