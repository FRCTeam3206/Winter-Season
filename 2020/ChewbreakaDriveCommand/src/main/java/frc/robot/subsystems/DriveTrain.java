// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot.subsystems;

import frc.robot.RobotMap;
import frc.robot.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.*;

public class DriveTrain extends Subsystem {

    //private final CANSparkMax m_left = RobotMap.driveTrainLeftMotors;
    //private final CANSparkMax m_right = RobotMap.driveTrainRightMotors;
    private final DifferentialDrive m_robotDrive = RobotMap.chewbreakaDrive;


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

	CANEncoder leftEncoder = RobotMap.leftFrontDrive.getEncoder();
	CANEncoder rightEncoder = RobotMap.rightFrontDrive.getEncoder();
	double TotalRotation = 0;
	double MoveError;
	double desiredDistance;
	double distanceTraveled;
	double max = 3925;
	double min = 0;
	double EncoderTolerance = 50;
	double EncoderUnit = 1904.76;//158.73 = 1 inch; 1904.76 = 1ft
	double DistanceMoved = 0;
	
	double leftDeadband = .05;
  	double rightDeadband = .13;

	//Acceleration Limiting
	boolean accelerationLimiting = true;
	double accelLimitedLeftGetY;
	double accelLimitedRightGetY;
	double accelLimitedSlideDrive;
	double accelDriveKonstant = 4; // Change from 2-32. 32 is super slow to react, 2 is little improvement
	double accelSlideKonstant = 10;
	// Shooting Variables
	double rightShooterSpeed;
	double leftShooterSpeed;

    @Override
    public void initDefaultCommand() {
    	setDefaultCommand(new DriveWithJoysticks());
    }

    public void takeJoystickInputs(Joystick left, Joystick right) {
		m_robotDrive.tankDrive(left.getY() + leftDeadband * (-Math.abs(left.getY())/left.getY()), right.getY() + rightDeadband * (-Math.abs(right.getY())/right.getY())); // No acceleration limiting.
    	//m_robotDrive.tankDrive(left.getY() * .75, right.getY()*.75);
    	//m_robotDrive.arcadeDrive(left.getX() * .65, -right.getRawAxis(3) * .65);
	}
	
	public void runAccelLimiting(Joystick left, Joystick right) {
		if(accelerationLimiting == true){
			accelLimitedLeftGetY  =  left.getY()  / accelDriveKonstant + accelLimitedLeftGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
			accelLimitedRightGetY =  right.getRawAxis(3) / accelDriveKonstant + accelLimitedRightGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
			m_robotDrive.tankDrive(accelLimitedLeftGetY * leftDeadband, accelLimitedRightGetY * rightDeadband);
		   }
		   else{  
			m_robotDrive.tankDrive(left.getY() * leftDeadband, left.getRawAxis(3) * rightDeadband); // No acceleration limiting.
		   }
	}

 	public void Drive(double Speed) {
		m_robotDrive.tankDrive(Speed, Speed);
	}
 	
 	public void Rotate(double Speed) { 
 		m_robotDrive.tankDrive(-Speed, Speed);
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

