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
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Joystick;
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

  private CANSparkMax topLeftMax;
  private CANSparkMax topRightMax;
  private CANSparkMax bottomLeftMax;
  private CANSparkMax bottomRightMax;
  private CANPIDController topLeftController;
  private CANPIDController topRightController;
  private CANEncoder LeftEncoder;
  private CANEncoder RightEncoder;
  public double kPDrive, kIDrive, kDDrive, kIzDrive, kFFDrive, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;

  public static final ADIS16448_IMU imu = new ADIS16448_IMU();

  DifferentialDrive NeoDrive;
  Joystick arcadeStick = new Joystick(0);

  double driveRotations;
  boolean runDriveTrain = false;
  double leftDistance = 0;
  double rightDistance = 0;

  //ADIS
  double GyroAngle;
  double GyroCoefficient = 1.0; 
  int DesiredAngle;
  String DesiredDirection;
  double desiredDistance;
  double distanceTraveled;
  double kPTurn;
  double kITurn;
  double kDTurn;
  int integral, previous_error, setpoint = 0;
  double error;
  double rcw;
  double derivative;
  boolean mode = false;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    topLeftMax = new CANSparkMax(2,MotorType.kBrushless);
    topRightMax = new CANSparkMax(4,MotorType.kBrushless);
    bottomLeftMax = new CANSparkMax(1,MotorType.kBrushless);
    bottomRightMax = new CANSparkMax(3,MotorType.kBrushless);
    NeoDrive = new DifferentialDrive(topLeftMax, topRightMax);
    topLeftMax.restoreFactoryDefaults();
    topRightMax.restoreFactoryDefaults();
    bottomLeftMax.restoreFactoryDefaults();
    bottomRightMax.restoreFactoryDefaults();
    bottomLeftMax.follow(topLeftMax);
    bottomRightMax.follow(topRightMax);

    topLeftController = topLeftMax.getPIDController();
    topRightController = topRightMax.getPIDController();
    LeftEncoder = topLeftMax.getEncoder();
    RightEncoder = topRightMax.getEncoder();

    kPDrive = 0;//.00005;//after testing at 4ft, we don't need a kI because it ramps up just how we like it. need to add velocity control
    kIDrive = 0;//.000006;
    kDDrive = 0;
    kIzDrive = 0;
    kFFDrive = 0.000221987;
    kMaxOutput = 1;
    kMinOutput = -1;
    maxRPM = 5676;

    //SmartMotion coefficients
    maxVel = 2000;//rpm
    minVel = 0;
    maxAcc =5500;
    driveRotations = 48; //setpoint for distance in inches

    topLeftController.setP(kPDrive);
    topLeftController.setI(kIDrive);
    topLeftController.setD(kDDrive);
    topLeftController.setIZone(kIzDrive);
    topLeftController.setFF(kFFDrive);
    topLeftController.setOutputRange(kMinOutput, kMaxOutput);

    topRightController.setP(kPDrive);
    topRightController.setI(kIDrive);
    topRightController.setD(kDDrive);
    topRightController.setIZone(kIzDrive);
    topRightController.setFF(kFFDrive);
    topRightController.setOutputRange(kMinOutput, kMaxOutput);

    int smartMotionSlot = 0;
    topLeftController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    topLeftController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    topLeftController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    topLeftController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    topRightController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    topRightController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    topRightController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    topRightController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
    
    SmartDashboard.putNumber("kPDrive", kPDrive);
    SmartDashboard.putNumber("kIDrive", kIDrive);
    SmartDashboard.putNumber("kDDrive", kDDrive);
    SmartDashboard.putNumber("kIzDrive", kIzDrive);
    SmartDashboard.putNumber("kFFDrive", kFFDrive);
    SmartDashboard.putNumber("kMaxOutput", kMaxOutput);
    SmartDashboard.putNumber("kMinOutput", kMinOutput);
    
    SmartDashboard.putNumber("Max Velocity", maxVel);
    SmartDashboard.putNumber("Min Velocity", minVel);
    SmartDashboard.putNumber("Max Acceleration", maxAcc);
    SmartDashboard.putNumber("Allowed Closed Loop Error", allowedErr);
    SmartDashboard.putNumber("Set Position", 0);
    SmartDashboard.putNumber("Set Velocity", 0);

    //button to toggle between velocity and smart motion modes
    SmartDashboard.putBoolean("Mode", false);

    /*
    SmartDashboard.putNumber("Set Rotations", driveRotations);
    SmartDashboard.putNumber("Left Real Time Distance", leftDistance);
    SmartDashboard.putNumber("Right Real Time Distance", rightDistance);
    */
    LeftEncoder.setPosition(0);
    RightEncoder.setPosition(0);
    
  }

  public void Turn(String Direction, int Angle) {

  GyroAngle = Math.abs(GyroCoefficient * imu.getGyroAngleX());
    
  DesiredDirection = Direction;
  DesiredAngle = Angle;
  imu.reset();
  while( Math.abs(GyroAngle) <= DesiredAngle) {

    GyroAngle = Math.abs(GyroCoefficient * imu.getGyroAngleX());

  if (DesiredDirection == "left") {
    NeoDrive.tankDrive(.6, -.6);
    imu.getGyroAngleX();
    SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
  }
  else if(DesiredDirection == "right") {
  NeoDrive.tankDrive(-.6, .6);
  imu.getGyroAngleX();
  SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
  }
  imu.getGyroAngleX();
  SmartDashboard.putNumber("Gyro Angle Degrees", GyroAngle);
}
 imu.reset();
 NeoDrive.arcadeDrive(0, 0); 
}

public void TurnPID(){
  error = setpoint - imu.getGyroAngleX(); // Error = Target - Actual
  this.integral += (error*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
  derivative = (error - this.previous_error) / .02;
  this.rcw = kPTurn*error + kITurn*this.integral + kDTurn*derivative;
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
  //-----------------------------------Using auto to run the drive function
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  //----------------------------Turn function
/*
  @Override
  public void teleopInit() {
    LeftEncoder.setPosition(0);
    RightEncoder.setPosition(0);
  }
  */
  @Override
  public void teleopPeriodic() {
    //double rotations = SmartDashboard.getNumber("Set Rotations", 0);
      
    double p = SmartDashboard.getNumber("kPDrive", 0);
    double i = SmartDashboard.getNumber("kIDrive", 0);
    double d = SmartDashboard.getNumber("kDDrive", 0);
    double iz = SmartDashboard.getNumber("kIzDrive", 0);
    double ff = SmartDashboard.getNumber("kFFDrive", 0);
    double max = SmartDashboard.getNumber("kMaxOutput", 0);
    double min = SmartDashboard.getNumber("kMinOutput", 0);
    double maxV = SmartDashboard.getNumber("Max Velocity", 0);
    double minV = SmartDashboard.getNumber("Min Velocity", 0);
    double maxA = SmartDashboard.getNumber("Max Acceleration", 0);
    double allE = SmartDashboard.getNumber("Allowed Closed Loop Error", 0);
    
    if ((p != kPDrive)) { topLeftController.setP(p); topRightController.setP(p); kPDrive = p;}
    if ((i != kIDrive)) { topLeftController.setI(i); topRightController.setI(i); kIDrive = i;}
    if ((d != kDDrive)) { topLeftController.setD(d); topRightController.setD(d); kDDrive = d;}
    if ((iz != kIzDrive)) { topLeftController.setIZone(iz); topRightController.setIZone(iz); kIzDrive = iz;}
    if ((ff != kFFDrive)) { topLeftController.setFF(ff); topRightController.setIZone(ff); kFFDrive = ff;}
    if ((max != kMaxOutput) || (min != kMinOutput)){
      topLeftController.setOutputRange(min, max); topRightController.setOutputRange(min, max); kMinOutput = min; kMaxOutput = max;
    }
    if((maxV != maxVel)) { topLeftController.setSmartMotionMaxVelocity(maxV,0); topRightController.setSmartMotionMaxVelocity(maxV, 0); maxVel = maxV; }
    if((minV != minVel)) { topLeftController.setSmartMotionMinOutputVelocity(minV,0);  topRightController.setSmartMotionMinOutputVelocity(minV,0); minVel = minV; }
    if((maxA != maxAcc)) { topLeftController.setSmartMotionMaxAccel(maxA,0); topRightController.setSmartMotionMaxAccel(maxA,0); maxAcc = maxA; }
    if((allE != allowedErr)) { topLeftController.setSmartMotionAllowedClosedLoopError(allE,0); topRightController.setSmartMotionAllowedClosedLoopError(allE,0); allowedErr = allE; }
    
    double setPoint, leftProcessVariable, rightProcessVariable;
    boolean mode =SmartDashboard.getBoolean("Mode", false);


    //if (rotations != driveRotations) {driveRotations = rotations;}
    if (arcadeStick.getRawButton(1)) {
      LeftEncoder.setPosition(0);
      RightEncoder.setPosition(0);
    }
    
    rightDistance = RightEncoder.getPosition() / 12;
    leftDistance = -LeftEncoder.getPosition() / 12;
    SmartDashboard.putNumber("Left Real Time Distance", leftDistance);
    SmartDashboard.putNumber("Right Real Time Distance", rightDistance);

    if(mode) {
      System.out.println("Velocity Control Mode");
      setPoint = SmartDashboard.getNumber("Set Velocity", 0);
      topLeftController.setReference(-setPoint, ControlType.kVelocity);
      topRightController.setReference(setPoint, ControlType.kVelocity);
      leftProcessVariable = -LeftEncoder.getVelocity();
      rightProcessVariable = RightEncoder.getVelocity();
    } else {
      setPoint = SmartDashboard.getNumber("Set Position", 0) * 12;
      System.out.println("Position Control Mode");
      /**
       * As with other PID modes, Smart Motion is set by calling the
       * setReference method on an existing pid object and setting
       * the control type to kSmartMotion
       */
      topLeftController.setReference(-setPoint, ControlType.kSmartMotion);
      topRightController.setReference(setPoint, ControlType.kSmartMotion);
      leftProcessVariable = -LeftEncoder.getPosition() / 12;
      rightProcessVariable = RightEncoder.getPosition() / 12;
    }
    //SmartDashboard.putNumber("SetPoint", setPoint);
    SmartDashboard.putNumber("Left Process Variable", leftProcessVariable);
    SmartDashboard.putNumber("Right Process Variable", rightProcessVariable);
    SmartDashboard.putNumber("Left Output", topLeftMax.getAppliedOutput());
    SmartDashboard.putNumber("Right Output", topRightMax.getAppliedOutput());
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    TurnPID();
    NeoDrive.arcadeDrive(0, rcw);
  }
  /*
  @Override
  public void disabledInit() {
    LeftEncoder.setPosition(0);
    RightEncoder.setPosition(0);
  }
*/
}
