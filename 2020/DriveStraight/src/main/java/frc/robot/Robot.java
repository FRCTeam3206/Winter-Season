/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    //DriveTrain
    DifferentialDrive chewbreakaDrive; 
    //Drivetrain Motors
      CANSparkMax leftFrontDrive;//= new CANSparkMax(1, MotorType.kBrushless);
      CANSparkMax rightFrontDrive;// = new CANSparkMax(3, MotorType.kBrushless);
      CANSparkMax leftBackDrive;// = new CANSparkMax(2, MotorType.kBrushless);  
      CANSparkMax rightBackDrive;// = new CANSparkMax(4, MotorType.kBrushless);

  private CANEncoder leftEncoder;
  private CANEncoder rightEncoder;

  public static final ADIS16448_IMU imu = new ADIS16448_IMU();
  double kP = 0.1;
  double kAngleSetpoint;
  XboxController driveStick;
  double desiredDistance;
  double distanceTraveled;

    //Acceleration Limiting Variables
    boolean accelerationLimiting = true;
    double accelLimitedLeftGetY;
    double accelLimitedRightGetY;
    double accelLimitedSlideDrive;
    double accelDriveKonstant = 32;//12 Change from 2-32. 32 is super slow to react, 2 is little improvement
    double leftDriveCoef = .8;
    double rightDriveCoef = .8;
    double rightStickDeadband = .05;
    double leftStickDeadband = .05;
    double leftAdjusted;
    double rightAdjusted;

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
    driveStick = new XboxController(0);

    leftEncoder = new CANEncoder(leftFrontDrive);
    rightEncoder = new CANEncoder(rightFrontDrive);
    chewbreakaDrive = new DifferentialDrive(leftFrontDrive, rightFrontDrive);
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    kAngleSetpoint = imu.getGyroAngleZ();
  }

  public void Drive (double distance) {
    distanceTraveled = ((-leftEncoder.getPosition() + rightEncoder.getPosition()) / 2)* 1/12;
    desiredDistance = distance + distanceTraveled;
    if (distance > 0) {
    while (desiredDistance > distanceTraveled) {
      distanceTraveled = ((-leftEncoder.getPosition() + rightEncoder.getPosition()) / 2)* 1/12;
      SmartDashboard.putNumber("Distance Traveled", distanceTraveled); //Encoder value scaled per foot for low gear
      accelLimitedLeftGetY  =  -.6 / accelDriveKonstant + accelLimitedLeftGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      accelLimitedRightGetY =  -.6 / accelDriveKonstant + accelLimitedRightGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      double error = -leftEncoder.getPosition() - rightEncoder.getPosition();
      chewbreakaDrive.tankDrive(accelLimitedLeftGetY * leftDriveCoef + (kP * error), accelLimitedRightGetY * rightDriveCoef - (kP * error));
    }
    } else if (distance < 0) {
      while (desiredDistance < distanceTraveled) {
      distanceTraveled = leftEncoder.getPosition() * -1/12;
      SmartDashboard.putNumber("Distance Traveled", distanceTraveled); //Encoder value scaled per foot for low gear
      double error = -leftEncoder.getPosition() - rightEncoder.getPosition();
      chewbreakaDrive.tankDrive(1 * leftDriveCoef + (kP * error), 1 * rightDriveCoef - (kP * error));
    }
    } else {
    chewbreakaDrive.tankDrive(0,0);
    }
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
    SmartDashboard.putNumber("right side", rightEncoder.getPosition());
    SmartDashboard.putNumber("left side", -leftEncoder.getPosition());
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
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Drive(5);
  }

  @Override
  public void teleopInit() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }
  
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    /*  
        double turningValue = (kAngleSetpoint - imu.getGyroAngleZ()) * kP;
        // Invert the direction of the turn if we are going backwards
        turningValue = Math.copySign(turningValue, arcadeStick.getY());
        artemisDrive.arcadeDrive(arcadeStick.getY(), turningValue);
    */
    if(accelerationLimiting == true){
      accelLimitedLeftGetY  =  driveStick.getY(Hand.kLeft)  / accelDriveKonstant + accelLimitedLeftGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      accelLimitedRightGetY =  driveStick.getY(Hand.kRight) / accelDriveKonstant + accelLimitedRightGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      double error = -leftEncoder.getPosition() - rightEncoder.getPosition();
      chewbreakaDrive.tankDrive(accelLimitedLeftGetY * leftDriveCoef + (kP * error), accelLimitedRightGetY * rightDriveCoef - (kP * error));
    }
    else{  
      chewbreakaDrive.tankDrive(driveStick.getY(Hand.kLeft) * leftDriveCoef, driveStick.getY(Hand.kRight) * rightDriveCoef); // No acceleration limiting.
    }

    if (driveStick.getRawButton(2)) {
      leftEncoder.setPosition(0);
      rightEncoder.setPosition(0);
    }
    }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
