/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.analog.adis16448.frc.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  //XboxController driveController = new XboxController(0);
  Joystick driveController = new Joystick(0);
    
  /* Loop tracker for prints */
int _loops = 0;
 // Joystick weaponController = new Joystick(1);  

//----------Motor Controllers------------
  // Left Drivetrain Motors
    WPI_VictorSPX leftFrontDrive = new WPI_VictorSPX(1);
    WPI_VictorSPX leftBackDrive = new WPI_VictorSPX(2);  
  
  // Right Drivetrain Motors
    WPI_VictorSPX rightFrontDrive = new WPI_VictorSPX(3);
    WPI_VictorSPX rightBackDrive = new WPI_VictorSPX(4);

  // Slide Motor
    WPI_TalonSRX rightShooter = new WPI_TalonSRX(5); 
    WPI_TalonSRX leftShooter = new WPI_TalonSRX(6); 

   // Differential Drive
  DifferentialDrive hermesDrive = new DifferentialDrive(leftFrontDrive , rightFrontDrive);  

   // Drive Variables
    double leftDriveSpeed;
    double rightDriveSpeed;
    double leftDriveCoef   = 1;
    double rightDriveCoef  = -1;
    double triggerDeadzone = .1;
    double driveTopSpeed   = 1;
    double slideMotorSpeed;
    double slideMotorCoef  = 1;

  //Acceleration Limiting Variables
    boolean accelerationLimiting = true;
    double accelLimitedLeftGetY;
    double accelLimitedRightGetY;
    double accelLimitedSlideDrive;
    double accelDriveKonstant = 4; // Change from 2-32. 32 is super slow to react, 2 is little improvement
    double accelSlideKonstant = 10;
  // Shooting Variables
    double rightShooterSpeed;
    double leftShooterSpeed;

      double kPShoot = .4;
      double kIShoot = .0009;
      double kDShoot = 0;
      double kFShoot = 1023/7200;
      double setPoint;
      boolean runShooter = false;

    //Limelight
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");
    NetworkTableEntry tv = table.getEntry("tv");
    double x = 0;

    //PID
    double kPTurn = .04;
    double kITurn = .0004;
    double kDTurn = 0;
    boolean runDriveTrain = false;
    double integral, previous_error, setpoint = 0;
    double error;
    double derivative;
    double rcw;

    ADIS16448_IMU imu = new ADIS16448_IMU();


    /**
     * The target velocity is calculated by taking the desired speed (in RPM) 
     * and multiplying it by the number of encoder counts per revolution (in this case, 4096)
     * you then divide by 600 to convert from minutes to milliseconds
     * This function is called every 100ms, so 100 * 600 = 60000
     * 60000ms/1000 = 60 seconds = 1 minute
     * In this case, with a 10:1 gearbox on the Rs775 Pros, I chose to go for 900RPM (a middle ground value)
     * (900RPM * 4096 encoder counts) / 600ms = 6144 encoder count units per 100ms
     */
    double targetVelocity_UnitsPer100ms = 6144; //900RPM * 4096 encoder counts / 600;
  
  @Override
  public void robotInit() {
    //Slave Motors Together to Allow for Easier Control 
    leftBackDrive.follow(leftFrontDrive);
    rightFrontDrive.follow(rightBackDrive);

      hermesDrive.setRightSideInverted(false); // do not change this 
      rightShooter.setInverted(true);

      rightShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);

      rightShooter.configNominalOutputForward(0, 30);
		  rightShooter.configNominalOutputReverse(0, 30);
	   	rightShooter.configPeakOutputForward(1, 30);
      rightShooter.configPeakOutputReverse(-1, 30);
    
      rightShooter.config_kF(0, kFShoot, 30);
		  rightShooter.config_kP(0, kPShoot, 30);
		  rightShooter.config_kI(0, kIShoot, 30);
      rightShooter.config_kD(0, kDShoot, 30);


      leftShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);

      leftShooter.configNominalOutputForward(0, 30);
		  leftShooter.configNominalOutputReverse(0, 30);
	   	leftShooter.configPeakOutputForward(1, 30);
      leftShooter.configPeakOutputReverse(-1, 30);
    
      leftShooter.config_kF(0, kFShoot, 30);
		  leftShooter.config_kP(0, kPShoot, 30);
		  leftShooter.config_kI(0, kIShoot, 30);
      leftShooter.config_kD(0, kDShoot, 30);

      leftFrontDrive.config_kP(0,kPTurn, 30);
      leftFrontDrive.config_kI(0,kITurn, 30);
      leftFrontDrive.config_kD(0,kDTurn, 30);

      rightBackDrive.config_kP(0,kPTurn, 30);
      rightBackDrive.config_kI(0,kITurn, 30);
      rightBackDrive.config_kD(0,kDTurn, 30);

      SmartDashboard.putNumber("kPShoot", kPShoot);
      SmartDashboard.putNumber("kIShoot", kIShoot);
      //SmartDashboard.putNumber("kD", kDShoot);
      //SmartDashboard.putNumber("kFF", kF);

      SmartDashboard.putNumber("kPTurn", kPTurn);
      SmartDashboard.putNumber("kITurn", kITurn);
      SmartDashboard.putNumber("kDTurn", kDTurn);

      SmartDashboard.putNumber("Set Velocity", targetVelocity_UnitsPer100ms);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("stream").setNumber(0);
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);


  }

  @Override
  public void robotPeriodic() {
      Shuffleboard.startRecording();

    if (driveController.getRawButton(1)) {
      x = tx.getDouble(0);
    }

      hermesDrive.arcadeDrive(0 , -PID(x));
      SmartDashboard.putNumber("output value", rcw);
  
      double p = SmartDashboard.getNumber("kPTurn", 0);
      double i = SmartDashboard.getNumber("kITurn", 0);
      double d = SmartDashboard.getNumber("kDTurn", 0);
      SmartDashboard.putNumber("X", x);
  
  
      if (p != kPTurn) { kPTurn = p;}
      if (i != kITurn) { kITurn = i;}
      if (d != kDTurn) { kDTurn = d;}
      
      //Drive Correction Code
      if (driveController.getRawButton(1)) {
        runDriveTrain = true;
      } else {
        runDriveTrain = false;
      } 
      double x = tx.getDouble(0);
      SmartDashboard.putNumber("X", x);
      double y = ty.getDouble(0);
      double ydist = (98.25-35)/Math.tan((40+y)*Math.PI/180);//d=(portheight-cameraheight)/(tan(cameramountangle+ty))
      SmartDashboard.putNumber("Y angle to target", y);
      SmartDashboard.putNumber("Y dist to target (inches)", ydist);
  
      while (runDriveTrain == true) {
      hermesDrive.arcadeDrive(0 , -PID(x));
      SmartDashboard.putNumber("output value", rcw);
  
      if (p != kPTurn) { kPTurn = p;}
      if (i != kITurn) { kITurn = i;}
      if (d != kDTurn) { kDTurn = d;}
  
      if (driveController.getRawButton(3)) {
        runDriveTrain = false;
      }
    }
    }
    /*
    hermesDrive.arcadeDrive(0, driveController.getX() * .5);

      //Set Velocity Code
      if (driveController.getRawButton(4)) {
        runShooter = true;
      } else {
        runShooter = false;
      } 

    while (runShooter == true) {
			/* Velocity Closed Loop */
			/**
			 * Convert 500 RPM to units / 100ms.
			 * 4096 Units/Rev * 500 RPM / 600 100ms/min in either direction:
			 * velocity setpoint is in units/100ms
			 */
      /* 500 RPM in either direction */
      /*
			rightShooter.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
      SmartDashboard.putNumber("Right Loop Error", rightShooter.getClosedLoopError(0));
      SmartDashboard.putNumber("Right Real Time Velocity", rightShooter.getSelectedSensorVelocity(0));

      leftShooter.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
      SmartDashboard.putNumber("Left Loop Error", leftShooter.getClosedLoopError(0));
      SmartDashboard.putNumber("Left Real Time Velocity", leftShooter.getSelectedSensorVelocity(0));
      
      double p = SmartDashboard.getNumber("kPShoot", 0);
      double i = SmartDashboard.getNumber("kIShoot", 0);
      //double d = SmartDashboard.getNumber("kD", 0);
      //double ff = SmartDashboard.getNumber("kFF", 0);
      
      setPoint = SmartDashboard.getNumber("Set Velocity", 0);

      if ((p != kPShoot)) { rightShooter.config_kP(0, p); kPShoot = p;}
      if ((i != kIShoot)) { rightShooter.config_kI(0, i); kIShoot = i;}
      //if ((d != kDShoot)) { rightShooter.config_kP(0, d); kDShoot = d;}
      //if ((ff != kF)) { rightShooter.config_kF(0, ff); kF = ff;}
      
      if ((setPoint != targetVelocity_UnitsPer100ms)) { rightShooter.set(ControlMode.Velocity, setPoint); targetVelocity_UnitsPer100ms  = setPoint;}
      rightShooter.set(ControlMode.Velocity, setPoint);

      if ((setPoint != targetVelocity_UnitsPer100ms)) { leftShooter.set(ControlMode.Velocity, setPoint); targetVelocity_UnitsPer100ms  = setPoint;}
      leftShooter.set(ControlMode.Velocity, setPoint);

      if (driveController.getRawButton(2)) {
        runShooter = false;
      }      
    }
    */
  @Override
  public void disabledInit() {
    Shuffleboard.stopRecording();
  }

  public double PID(double setpoint) {
    //double x = tx.getDouble(0);
    double x = imu.getAngle();
    error = setpoint - x;
    integral = integral + (error * .02);
    derivative = (error - previous_error) / .02;
    rcw = kPTurn * error + kITurn * integral + kDTurn * derivative;
    previous_error = error;
    return rcw;
  }
}



