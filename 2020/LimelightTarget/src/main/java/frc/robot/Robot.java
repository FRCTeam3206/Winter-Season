/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  CANSparkMax topLeftMax = new CANSparkMax(2,MotorType.kBrushless);
  CANSparkMax topRightMax = new CANSparkMax(4,MotorType.kBrushless);
  
  CANSparkMax bottomLeftMax = new CANSparkMax(1,MotorType.kBrushless);
  CANSparkMax bottomRightMax = new CANSparkMax(3,MotorType.kBrushless);

  Joystick arcadeStick = new Joystick(0);
    
  public static final ADIS16448_IMU imu = new ADIS16448_IMU();
  
  DifferentialDrive NeoDrive = new DifferentialDrive(topLeftMax, topRightMax);

  //ADIS
  double GyroAngle;
  double GyroCoefficient = 1.0; 
  double DesiredAngle;
  String DesiredDirection;
  double desiredDistance;
  double distanceTraveled;

  //Limelight
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    bottomLeftMax.follow(topLeftMax);
    bottomRightMax.follow(topRightMax);
    imu.reset();
  }  

  public void Turn(double Angle) {
    imu.reset();
    GyroAngle = GyroCoefficient * imu.getAngle();
    DesiredAngle = -Angle;
    
    while(Math.abs(GyroAngle) <= Math.abs(DesiredAngle)) {
      GyroAngle = Math.abs(GyroCoefficient * imu.getAngle());
      NeoDrive.tankDrive(-.3 * Math.copySign(1, tx.getDouble(0)), .3 * Math.copySign(1, tx.getDouble(0)));
      SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
    }
   NeoDrive.arcadeDrive(0, 0); 
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
    SmartDashboard.putNumber("Gyro Angle Degrees", GyroCoefficient * imu.getAngle());
    double tx = NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0);
    
    SmartDashboard.putNumber("X", tx);
    if (arcadeStick.getRawButton(1)) {
      Turn(tx);
    } else {
      NeoDrive.tankDrive(arcadeStick.getY() * .6, arcadeStick.getRawAxis(3) * .6);
    }
    
    if (arcadeStick.getRawButton(2)) {
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
    } else {
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(0);
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
