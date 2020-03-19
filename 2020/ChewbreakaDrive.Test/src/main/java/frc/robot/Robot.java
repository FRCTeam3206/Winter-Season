/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
//Motor Imports & Auto Chooser
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//Pneumatics and PDP
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

//Joysticks
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

//Camera, Math
import java.lang.Math;
import edu.wpi.first.wpilibj.Timer;

//Gyro, & Limit Switch
import com.analog.adis16448.frc.ADIS16448_IMU;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * ----Right Joystick(Logitech Extreme 3D)---- 1-Low Gear 2-High Gear ----Xbox
 * One Controller(Ian) 1-Low Gear 2-High Gear ----Weapons Joystick(Logitech Dual
 * Action)---- X(1)-Transport Up A(2)-Intake Piston/Run Toggle Y(4)-Shooter On &
 * Transport Up Toggle LB(5)-Shoot From One Bot Away & Transport Toggle
 * RB(6)-Extake Toggle LT(7)-Reverse Climb RT(8)-Climb BACK(9)-Ratchet Engage
 * START(10)-Ratchet Disengage Left Stick Click(11)-Control Panel Spin Right
 * Stick Click(12)-Start Compressor
 */
public class Robot extends TimedRobot {
  // Drive Type
  boolean XboxDrive = true;;
  // Joysticks
  Joystick leftStick;
  Joystick rightStick;
  XboxController driveStick;
  XboxController weaponStick;
  // Sendable Chooser
  SendableChooser<String> autoChoices = new SendableChooser<>();
  String autoSelected;
  // DriveTrain
  DifferentialDrive chewbreakaDrive;
  // Drivetrain Motors
  CANSparkMax leftFrontDrive;
  CANSparkMax rightFrontDrive;
  CANSparkMax leftBackDrive;
  CANSparkMax rightBackDrive;
  // DriveTrain Pneumatics
  DoubleSolenoid driveSol = new DoubleSolenoid(0, 0, 1);
  PowerDistributionPanel pdp = new PowerDistributionPanel(0);
  Compressor compressor = new Compressor(0);
  // DriveTrain Encoders and Gyro
  CANEncoder leftEncoder;
  CANEncoder rightEncoder;
  ADIS16448_IMU imu = new ADIS16448_IMU();
  // ADIS and Drive Variables
  double GyroAngle;
  double GyroCoefficient = 1.0;
  double DesiredAngle;
  double desiredDistance;
  double distanceTraveled;
  double tLateDrive = 10;// longest time it takes to drive for any function
  double tLateTurn = 5;// longest time it takes to drive for any function

  // PID Control
  double kPShoot = .4;
  double kIShoot = .0009;
  double kDShoot = 0;
  double kFShoot = .0005;
  double kPTurn = .04;
  double kITurn = .0004;
  double kDTurn = 0;

  // Acceleration Limiting Variables
  boolean accelerationLimiting = true;
  double accelLimitedLeftGetY;
  double accelLimitedRightGetY;
  double accelDriveKonstant = 12;// Change from 2-32. 32 is super slow to react, 2 is little improvement
  double leftDriveCoef = .8;
  double rightDriveCoef = .8;
  double primeConstant = .8; // This needs to be between 0 and 1. 0 means that the drive train is just x^3
  // and 1 means that it is just x. 1 is more sensitive and 0 is less sensitive.
  double rightPrime;
  double leftPrime;
  // Intake
  WPI_VictorSPX intakeMotor = new WPI_VictorSPX(5);
  DoubleSolenoid intakeSol = new DoubleSolenoid(0, 3, 4);
  // Intake Toggle Variables
  int intakeToggle = 0;
  boolean intakeToggleStick;
  boolean extakeToggleStick;
  int extakeToggle = 0;
  double intakeSpeed = 1;
  // Climb
  WPI_VictorSPX climbFrontMotor = new WPI_VictorSPX(6);
  WPI_VictorSPX climbBackMotor = new WPI_VictorSPX(7);
  DoubleSolenoid climbSol = new DoubleSolenoid(0, 2, 5);
  double climbSpeed = 1;

  // Shooter
  WPI_TalonSRX leftShooter = new WPI_TalonSRX(8);
  WPI_TalonSRX rightShooter = new WPI_TalonSRX(9);
  int shooterToggle = 0;
  boolean shooterToggleStick;
  Timer velocityTimer = new Timer();
  boolean shooter2ToggleStick;
  int shooter2Toggle = 0;
  // Ball Transport
  WPI_TalonSRX frontBallTransport = new WPI_TalonSRX(10);
  WPI_TalonSRX backBallTransport = new WPI_TalonSRX(11);
  DigitalInput ballSwitch = new DigitalInput(3);
  // Transport Variables
  double ballMagScale = .00110390625;// (1/4096 * 3.14 * 1.972) scaled to inches instead
  double ballDesiredDistance = 5.5;
  double ballDistanceTraveled;
  double frontBallSpeed = -1;
  double backBallSpeed = -1;
  int ballToggle = 0;
  boolean ballToggleButton;
  /**
   * The target velocity is calculated by taking the desired speed (in RPM) and
   * multiplying it by the number of encoder counts per revolution (in this case,
   * 4096) you then divide by 600 to convert from minutes to milliseconds This
   * function is called every 100ms, so 100 * 600 = 60000 60000ms/1000 = 60
   * seconds = 1 minute In this case, with a 10:1 gearbox on the Rs775 Pros, I
   * chose to go for 900RPM (a middle ground value) (900RPM * 4096 encoder counts)
   * / 600ms = 6144 encoder count units per 100ms
   */
  double targetVelocity = 6500;
  double oneBotVelocity = 6500;// needs tuning
  double autoTargetVelocity = 6400;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    leftFrontDrive = new CANSparkMax(1, MotorType.kBrushless);
    rightFrontDrive = new CANSparkMax(3, MotorType.kBrushless);
    leftBackDrive = new CANSparkMax(2, MotorType.kBrushless);
    rightBackDrive = new CANSparkMax(4, MotorType.kBrushless);
    leftEncoder = leftFrontDrive.getEncoder();
    rightEncoder = rightFrontDrive.getEncoder();

    leftFrontDrive.restoreFactoryDefaults();
    rightFrontDrive.restoreFactoryDefaults();
    leftBackDrive.restoreFactoryDefaults();
    rightBackDrive.restoreFactoryDefaults();
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);

    leftBackDrive.follow(leftFrontDrive);
    rightBackDrive.follow(rightFrontDrive);

    chewbreakaDrive = new DifferentialDrive(leftFrontDrive, rightFrontDrive);

    if (XboxDrive == false) {
      leftStick = new Joystick(0);
      rightStick = new Joystick(1);
      weaponStick = new XboxController(2);
    } else {
      driveStick = new XboxController(0);
      weaponStick = new XboxController(2);
    }
    shooterToggleStick = weaponStick.getYButtonPressed();
    intakeToggleStick = weaponStick.getRawButton(2);
    shooter2ToggleStick = weaponStick.getBumperPressed(Hand.kLeft);
    extakeToggleStick = weaponStick.getBumperPressed(Hand.kRight);
    pdp.clearStickyFaults();
    compressor.clearAllPCMStickyFaults();

    rightShooter.setInverted(true);
    rightShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    rightShooter.configNominalOutputForward(0, 30);
    rightShooter.configNominalOutputReverse(0, 30);
    rightShooter.configPeakOutputForward(1, 30);
    rightShooter.configPeakOutputReverse(-1, 30);

    leftShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    leftShooter.configNominalOutputForward(0, 30);
    leftShooter.configNominalOutputReverse(0, 30);
    leftShooter.configPeakOutputForward(1, 30);
    leftShooter.configPeakOutputReverse(-1, 30);

    velocityTimer.reset();
    backBallTransport.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    backBallTransport.configNominalOutputForward(0, 30);
    backBallTransport.configNominalOutputReverse(0, 30);
    backBallTransport.configPeakOutputForward(1, 30);
    backBallTransport.configPeakOutputReverse(-1, 30);

    frontBallTransport.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    frontBallTransport.configNominalOutputForward(0, 30);
    frontBallTransport.configNominalOutputReverse(0, 30);
    frontBallTransport.configPeakOutputForward(1, 30);
    frontBallTransport.configPeakOutputReverse(-1, 30);

    autoChoices.setDefaultOption("AutoLine", "AutoLine");
    autoChoices.addOption("Center Shot", "Center Shot");
    autoChoices.addOption("Left Shot", "Left Shot");
    autoChoices.addOption("Right Shot", "Right Shot");
    autoChoices.addOption("Left Cycle", "Left Cycle");
    autoChoices.addOption("Right Cycle", "Right Cycle");
    SmartDashboard.putData("Auto Routines", autoChoices);
    // SmartDashboard.putNumber("Spinner Feet",
    // wheelSpinner.getSelectedSensorPosition() * spinnerMagScale);
    leftEncoder.setPosition(0);
    // wheelSpinner.setSelectedSensorPosition(0);
    imu.reset();
  }

  public void Turn(double Angle) {
    GyroAngle = GyroCoefficient * imu.getGyroAngleZ();
    DesiredAngle = Angle + GyroAngle;
    velocityTimer.start();
    TurnLabel: if (Angle < 0) {
      while (GyroAngle >= DesiredAngle) {
        GyroAngle = GyroCoefficient * imu.getGyroAngleZ();
        chewbreakaDrive.tankDrive(.6, -.6);
        if (velocityTimer.get() >= tLateTurn) {
          break TurnLabel;
        }
      }
    } else if (Angle > 0) {
      while (GyroAngle <= DesiredAngle) {
        GyroAngle = GyroCoefficient * imu.getGyroAngleZ();
        chewbreakaDrive.tankDrive(-.6, .6);
        if (velocityTimer.get() >= tLateTurn) {
          break TurnLabel;
        }
      }
    } else {
      chewbreakaDrive.arcadeDrive(0, 0);
      System.out.println("turned " + Angle);
    }
    chewbreakaDrive.arcadeDrive(0, 0);
  }

  public void Drive(double distance) {
    distanceTraveled = leftEncoder.getPosition() * -1 / 12;
    desiredDistance = distance + distanceTraveled;
    velocityTimer.start();
    DriveLabel: if (distance > 0) {
      while (desiredDistance > distanceTraveled) {
        distanceTraveled = leftEncoder.getPosition() * -1 / 12;
        ballToggleButton = ballSwitch.get();
        // Set Button to Integer Value
        if (ballToggleButton == false && ballToggle == 0) { // First Press
          ballToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                          // toggle = 1

        } else if (ballToggleButton == true && ballToggle == 1) { // First Release
          ballToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
        }
        // Determine Piston Position Based on Integer Value
        if (ballToggle == 1 || ballToggle == 2) { // Trigger is Pressed
          if (frontBallTransport.getSelectedSensorPosition() * ballMagScale < ballDesiredDistance) {
            frontBallTransport.set(ControlMode.PercentOutput, -.7);
            backBallTransport.set(ControlMode.PercentOutput, -.7);
          } else if (frontBallTransport.getSelectedSensorPosition() * ballMagScale > ballDesiredDistance) {
            ballToggle = 0;
            frontBallTransport.stopMotor();
            backBallTransport.stopMotor();
            frontBallTransport.setSelectedSensorPosition(0);
          }
        }
        chewbreakaDrive.tankDrive(-.7, -.7);
        if (velocityTimer.get() >= tLateDrive) {
          break DriveLabel;
        }
      }
    } else if (distance < 0) {
      while (desiredDistance < distanceTraveled) {
        distanceTraveled = leftEncoder.getPosition() * -1 / 12;
        ballToggleButton = ballSwitch.get();
        // Set Button to Integer Value
        if (ballToggleButton == false && ballToggle == 0) { // First Press
          ballToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                          // toggle = 1

        } else if (ballToggleButton == true && ballToggle == 1) { // First Release
          ballToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
        }
        // Determine Piston Position Based on Integer Value
        if (ballToggle == 1 || ballToggle == 2) { // Trigger is Pressed
          if (frontBallTransport.getSelectedSensorPosition() * ballMagScale < ballDesiredDistance) {
            frontBallTransport.set(ControlMode.PercentOutput, -.7);
            backBallTransport.set(ControlMode.PercentOutput, -.7);
          } else if (frontBallTransport.getSelectedSensorPosition() * ballMagScale > ballDesiredDistance) {
            ballToggle = 0;
            frontBallTransport.stopMotor();
            backBallTransport.stopMotor();
            frontBallTransport.setSelectedSensorPosition(0);
          }
        }
        chewbreakaDrive.tankDrive(.8, .8);
        if (velocityTimer.get() >= tLateDrive) {
          break DriveLabel;
        }

      }
    } else {
      chewbreakaDrive.tankDrive(0, 0);
    }
    chewbreakaDrive.tankDrive(0, 0);
  }
 
  // for auto only
  public void Shoot() {
    velocityTimer.start();
    while (velocityTimer.get() <= 5) {
      rightShooter.set(ControlMode.Velocity, autoTargetVelocity);
      leftShooter.set(ControlMode.Velocity, autoTargetVelocity);
      if (velocityTimer.get() >= 1.5) {
        frontBallTransport.set(ControlMode.PercentOutput, -.9);
        backBallTransport.set(ControlMode.PercentOutput, -.9);
      }
    }
    rightShooter.stopMotor();
    leftShooter.stopMotor();
    frontBallTransport.stopMotor();
    backBallTransport.stopMotor();
  }

  public void Extendo(double direction) {
    if (direction == 1) {
      intakeSol.set(Value.kReverse);
      intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
    } else if (direction == -1) {
      intakeSol.set(Value.kForward);
      intakeMotor.set(ControlMode.PercentOutput, 0);
    }

  }

  public void RightCycle() {
    Extendo(1);
    Drive(12.2);
    Extendo(-1);
    Drive(-17.2);
    Turn(100);
    Drive(8);
    Turn(50);
    Drive(1);
    Shoot();
  }

  public void LeftCycle() {
    Extendo(1);
    Drive(10.86);
    Extendo(-1);
    Drive(-3.6);
    Turn(-120);
    Drive(15);
    Shoot();
  }

  public void CenterShot() {
    Drive(9.8);
    Shoot();
    Drive(-15);
    Turn(135);
  }

  public void LeftShot() {
    Turn(45);
    Drive(8.5);
    Turn(-45);
    Drive(3);
    Shoot();
    Drive(-3);
    Turn(45);
    Drive(-10);
  }

  public void RightShot() {
    Turn(-45);
    Drive(8.5);
    Turn(42);
    Drive(3);
    Shoot();
    Drive(-3);
    Turn(-25);
    Drive(-10);
  }

  @Override
  public void robotPeriodic() {
    pdp.clearStickyFaults();
    compressor.clearAllPCMStickyFaults();
    leftBackDrive.clearFaults();
    rightBackDrive.clearFaults();
    leftFrontDrive.clearFaults();
    rightFrontDrive.clearFaults();

    if (isOperatorControl() == true) {
      ballToggleButton = ballSwitch.get();
      // Set Button to Integer Value
      if (extakeToggle == 0 || extakeToggle == 3) {
        if (ballToggleButton == false && ballToggle == 0) { // First Press
          ballToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through the toggle = 1
        } else if (ballToggleButton == true && ballToggle == 1) { // First Release
          ballToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
        }
        // Determine Piston Position Based on Integer Value
        if (ballToggle == 1 || ballToggle == 2) { // Trigger is Pressed
          if (frontBallTransport.getSelectedSensorPosition() * ballMagScale < ballDesiredDistance) {
            frontBallTransport.set(ControlMode.PercentOutput, -.7);
            backBallTransport.set(ControlMode.PercentOutput, -.7);
          } else if (frontBallTransport.getSelectedSensorPosition() * ballMagScale > ballDesiredDistance) {
            ballToggle = 0;
            frontBallTransport.setSelectedSensorPosition(0);
          }
        }
      } else {

      }
    }

  }

  @Override
  public void autonomousInit() {
    autoSelected = autoChoices.getSelected();

    compressor.start();
    driveSol.set(Value.kReverse);
    switch (autoSelected) {
    case "AutoLine":
      Drive(5);
      break;
    case "Center Shot":
      CenterShot();
      break;
    case "Left Shot":
      LeftShot();
      break;
    case "Right Shot":
      RightShot();
      break;
    case "Left Cycle":
      LeftCycle();
      break;
    case "Right Cycle":
      RightCycle();
      break;
    default:
      Drive(5);
      break;
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    compressor.start();
  }

  @Override
  public void teleopPeriodic() {
    // Acceleration Limiting
    leftPrime = primeConstant * driveStick.getY(Hand.kLeft)
        + (1 - primeConstant) * Math.pow(driveStick.getY(Hand.kLeft), 3); // The formula that this is making is
    rightPrime = primeConstant * driveStick.getY(Hand.kRight)
        + (1 - primeConstant) * Math.pow(driveStick.getY(Hand.kRight), 3);
    if (accelerationLimiting == true) {
      accelLimitedLeftGetY = leftPrime / accelDriveKonstant
          + accelLimitedLeftGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      accelLimitedRightGetY = rightPrime / accelDriveKonstant
          + accelLimitedRightGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      if (driveSol.get() == Value.kReverse) {
        chewbreakaDrive.tankDrive(accelLimitedRightGetY * rightDriveCoef, accelLimitedRightGetY * rightDriveCoef);
      } else {
        chewbreakaDrive.tankDrive(accelLimitedLeftGetY * leftDriveCoef, accelLimitedRightGetY * rightDriveCoef);
      }
    } else {
      chewbreakaDrive.tankDrive(driveStick.getY(Hand.kLeft) * leftDriveCoef,
          driveStick.getY(Hand.kRight) * rightDriveCoef); // No acceleration limiting.
    }

    // Right Joystick
    if (XboxDrive == false) {
      if (rightStick.getRawButton(1)) { // Low Speed
        driveSol.set(Value.kForward);
      } else if (rightStick.getRawButton(2)) { // High Speed
        driveSol.set(Value.kReverse);
      } else {
        driveSol.set(Value.kOff); // Ensures Pistons are Off
      }
    } else {
      if (driveStick.getBumper(Hand.kRight)) { // High Speed
        driveSol.set(Value.kReverse);
      } else if (driveStick.getBumper(Hand.kLeft)) { // Low Speed
        driveSol.set(Value.kForward);
      } else {
        driveSol.set(Value.kOff); // Ensures Pistons are Off
      }
    }
    // Weapons Joystick
    // Ball Transport

    // basic way to run the transport
    if (weaponStick.getRawButton(1)) {// Up
      frontBallTransport.set(ControlMode.PercentOutput, -.7);
      backBallTransport.set(ControlMode.PercentOutput, -.7);
    } else if (weaponStick.getRawButton(3)) {
      frontBallTransport.set(ControlMode.PercentOutput, .7);
      backBallTransport.set(ControlMode.PercentOutput, .7);
    } else {// Off
      frontBallTransport.stopMotor();
      backBallTransport.stopMotor();
      intakeMotor.stopMotor();
      intakeSol.set(Value.kForward);
    }

    // Intake Piston/Run Toggle
    intakeToggleStick = weaponStick.getRawButtonPressed(2);
    // Set Button to Integer Value
    if (intakeToggleStick == true && intakeToggle == 0) { // First Press
      intakeToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                        // toggle = 1
    } else if (intakeToggleStick == false && intakeToggle == 1) { // First Release
      intakeToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (intakeToggleStick == true && intakeToggle == 2) { // Second Press
      intakeToggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (intakeToggleStick == false && intakeToggle == 3) { // Second Release
      intakeToggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (intakeToggleStick == false && intakeToggle == 0) { // Completes the Cycle/Redundancy Backup
      intakeToggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }

    // Determine Piston Position Based on Integer Value
    if (intakeToggle == 1) {
      velocityTimer.start();
    } else if (intakeToggle == 1 || intakeToggle == 2) { // Trigger is Pressed
      intakeSol.set(Value.kReverse);
      if (velocityTimer.get() >= .5) {
        intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
      }
    } else if (intakeToggle == 3 || intakeToggle == 0) { // Trigger is Released
    }

    // Tentative Backdrive Toggle Function if other mthods does not work
    extakeToggleStick = weaponStick.getBumperPressed(Hand.kRight);
    // Set Button to Integer Value
    if (extakeToggleStick == true && extakeToggle == 0) { // First Press
      extakeToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                        // toggle = 1
    } else if (extakeToggleStick == false && extakeToggle == 1) { // First Release
      extakeToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (extakeToggleStick == true && extakeToggle == 2) { // Second Press
      extakeToggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (extakeToggleStick == false && extakeToggle == 3) { // Second Release
      extakeToggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (extakeToggleStick == false && extakeToggle == 0) { // Completes the Cycle/Redundancy Backup
      extakeToggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }
    // Determine Piston Position Based on Integer Value
    if (extakeToggle == 1) {
      velocityTimer.start();
    } else if (intakeToggle == 0 && extakeToggle == 1 || extakeToggle == 2) { // Trigger is Pressed
      intakeSol.set(Value.kReverse);
      frontBallTransport.set(ControlMode.PercentOutput, -frontBallSpeed);
      backBallTransport.set(ControlMode.PercentOutput, -backBallSpeed);
      if (velocityTimer.get() >= .5) {
        intakeMotor.set(ControlMode.PercentOutput, -intakeSpeed);
      }
    } else if (extakeToggle == 3 || extakeToggle == 0) { // Trigger is Released
    }

    // Shooter & Transport Toggle
    shooterToggleStick = weaponStick.getYButtonPressed();
    // Set Button to Integer Value
    if (shooterToggleStick == true && shooterToggle == 0) { // First Press
      shooterToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                         // toggle = 1
    } else if (shooterToggleStick == false && shooterToggle == 1) { // First Release
      shooterToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (shooterToggleStick == true && shooterToggle == 2) { // Second Press
      shooterToggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (shooterToggleStick == false && shooterToggle == 3) { // Second Release
      shooterToggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (shooterToggleStick == false && shooterToggle == 0) { // Completes the Cycle/Redundancy Backup
      shooterToggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }

    // Determine Piston Position Based on Integer Value
    if (shooterToggle == 1) {// makes sure the timer only starts once per cycle
      velocityTimer.start();
    } else if (shooterToggle == 1 || shooterToggle == 2) { // 1st Toggle
      rightShooter.set(ControlMode.Velocity, targetVelocity);
      leftShooter.set(ControlMode.Velocity, targetVelocity);
      if (velocityTimer.get() >= 1.5) {
        frontBallTransport.set(ControlMode.PercentOutput, frontBallSpeed);
        backBallTransport.set(ControlMode.PercentOutput, backBallSpeed);
      }
    } else if (shooterToggle == 3 || shooterToggle == 0) { // 2nd Toggle
    }

    // Shoot from one bot away
    shooter2ToggleStick = weaponStick.getBumperPressed(Hand.kLeft);
    // Set Button to Integer Value
    if (shooter2ToggleStick == true && shooter2Toggle == 0) { // First Press
      shooter2Toggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                          // toggle = 1
    } else if (shooter2ToggleStick == false && shooter2Toggle == 1) { // First Release
      shooter2Toggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (shooter2ToggleStick == true && shooter2Toggle == 2) { // Second Press
      shooter2Toggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (shooter2ToggleStick == false && shooter2Toggle == 3) { // Second Release
      shooter2Toggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (shooter2ToggleStick == false && shooter2Toggle == 0) { // Completes the Cycle/Redundancy Backup
      shooter2Toggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }

    // Determine Piston Position Based on Integer Value
    if (shooter2Toggle == 1) {// makes sure the timer only starts once per cycle
      velocityTimer.start();
    } else if (shooter2Toggle == 1 || shooter2Toggle == 2) { // 1st Toggle
      rightShooter.set(ControlMode.Velocity, oneBotVelocity);
      leftShooter.set(ControlMode.Velocity, oneBotVelocity);
      if (velocityTimer.get() >= 1.5) {
        frontBallTransport.set(ControlMode.PercentOutput, frontBallSpeed);
        backBallTransport.set(ControlMode.PercentOutput, backBallSpeed);
      }
    } else if (shooterToggle == 0 && shooter2Toggle == 0) { // 2nd Toggle
      rightShooter.stopMotor();
      leftShooter.stopMotor();
    }

    // Climb Ratchet
    if (weaponStick.getRawButtonPressed(10)) {
      climbSol.set(Value.kReverse);
    } else if (weaponStick.getRawButtonPressed(9)) {
      climbSol.set(Value.kForward);
    }
    // Climb
    if (weaponStick.getRawButton(7) && climbSol.get() == Value.kForward) {// Reverse Climb
      climbFrontMotor.set(ControlMode.PercentOutput, climbSpeed);
      climbBackMotor.set(ControlMode.PercentOutput, climbSpeed);
    } else if (weaponStick.getRawButton(8)) {// Climb
      climbFrontMotor.set(ControlMode.PercentOutput, -climbSpeed);
      climbBackMotor.set(ControlMode.PercentOutput, -climbSpeed);
    } else {
      climbFrontMotor.set(ControlMode.PercentOutput, 0);
      climbBackMotor.set(ControlMode.PercentOutput, 0);
    }
  }

  @Override
  public void testPeriodic() {
  }
}
