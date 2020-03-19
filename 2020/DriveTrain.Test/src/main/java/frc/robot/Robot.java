/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    //Joysticks
    XboxController driveStick;
    XboxController weaponStick;
    //DriveTrain
      DifferentialDrive chewbreakaDrive; 
      //Drivetrain Motors
        CANSparkMax leftFrontDrive;//= new CANSparkMax(1, MotorType.kBrushless);
        CANSparkMax rightFrontDrive;// = new CANSparkMax(3, MotorType.kBrushless);
        CANSparkMax leftBackDrive;// = new CANSparkMax(2, MotorType.kBrushless);  
        CANSparkMax rightBackDrive;// = new CANSparkMax(4, MotorType.kBrushless);

      //Acceleration Limiting Variables
      boolean accelerationLimiting = true;
      double accelLimitedLeftGetY;
      double accelLimitedRightGetY;
      double accelLimitedSlideDrive;
      double accelDriveKonstant = 24; // Change from 2-32. 32 is super slow to react, 2 is little improvement
      double leftDriveCoef = 1;
      double rightDriveCoef = 1;

      // Drive Variables
double leftDriveSpeed;
double rightDriveSpeed;
double triggerDeadzone = .1;
int desiredDirection;
double powerNumber = 3;
double primeConstant = .8; // This needs to be between 0 and 1. 0 means that the drive train is just x^3 and 1 means that it is just x. 1 is more sensitive and 0 is less sensitive.
double rightPrime;
double leftPrime;
boolean XboxDrive = true;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           ;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    leftFrontDrive = new CANSparkMax(1, MotorType.kBrushless);
    rightFrontDrive = new CANSparkMax(3, MotorType.kBrushless);
    leftBackDrive = new CANSparkMax(2, MotorType.kBrushless);  
    rightBackDrive = new CANSparkMax(4, MotorType.kBrushless);

    leftFrontDrive.restoreFactoryDefaults();
    rightFrontDrive.restoreFactoryDefaults();
    leftBackDrive.restoreFactoryDefaults();
    rightBackDrive.restoreFactoryDefaults();

    leftBackDrive.follow(leftFrontDrive);
    rightBackDrive.follow(rightFrontDrive);

    chewbreakaDrive = new DifferentialDrive(leftFrontDrive, rightFrontDrive);
    driveStick = new XboxController(0);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  //Acceleration Limiting
  
    leftPrime  = primeConstant * driveStick.getY(Hand.kLeft)  + (1-primeConstant) * Math.pow(driveStick.getY(Hand.kLeft), 3); //The formula that this is making is 
    rightPrime = primeConstant * driveStick.getY(Hand.kRight) + (1-primeConstant) * Math.pow(driveStick.getY(Hand.kRight), 3);
   
    if(accelerationLimiting == true){
      accelLimitedLeftGetY  =  leftPrime  / accelDriveKonstant + accelLimitedLeftGetY  * (accelDriveKonstant - 1) / accelDriveKonstant;
      accelLimitedRightGetY =  rightPrime / accelDriveKonstant + accelLimitedRightGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      chewbreakaDrive.tankDrive(accelLimitedLeftGetY * leftDriveCoef, accelLimitedRightGetY * rightDriveCoef);
    }
    else{  
      chewbreakaDrive.tankDrive(driveStick.getY(Hand.kLeft) * leftDriveCoef, driveStick.getY(Hand.kRight) * rightDriveCoef); // No acceleration limiting.
    }
    
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
