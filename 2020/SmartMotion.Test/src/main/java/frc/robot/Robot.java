/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


/**
 * Before Running:
 * Open shuffleBoard, select File->Load Layout and select the 
 * shuffleboard.json that is in the root directory of this example
 */

/**
 * REV Smart Motion Guide
 * 
 * The SPARK MAX includes a new control mode, REV Smart Motion which is used to 
 * control the position of the motor, and includes a max velocity and max 
 * acceleration parameter to ensure the motor moves in a smooth and predictable 
 * way. This is done by generating a motion profile on the fly in SPARK MAX and 
 * controlling the velocity of the motor to follow this profile.
 * 
 * Since REV Smart Motion uses the velocity to track a profile, there are only 
 * two steps required to configure this mode:
 *    1) Tune a velocity PID loop for the mechanism
 *    2) Configure the smart motion parameters
 * 
 * Tuning the Velocity PID Loop
 * 
 * The most important part of tuning any closed loop control such as the velocity 
 * PID, is to graph the inputs and outputs to understand exactly what is happening. 
 * For tuning the Velocity PID loop, at a minimum we recommend graphing:
 *
 *    1) The velocity of the mechanism (‘Process variable’)
 *    2) The commanded velocity value (‘Setpoint’)
 *    3) The applied output
 *
 * This example will use ShuffleBoard to graph the above parameters. Make sure to
 * load the shuffleboard.json file in the root of this directory to get the full
 * effect of the GUI layout.
 */
public class Robot extends TimedRobot {
  CANSparkMax leftFrontDrive;
  CANSparkMax rightFrontDrive;
  CANSparkMax leftBackDrive;
  CANSparkMax rightBackDrive;
  private CANPIDController leftFrontController;
  private CANPIDController rightFrontController;
  private CANEncoder leftEncoder;
  private CANEncoder rightEncoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr;
  double setPoint, leftProcessVariable, rightProcessVariable, distanceTraveled, desiredDistance;
  Timer velocityTimer = new Timer();
  double tLateDrive = 10;


  @Override
  public void robotInit() {
    // initialize motor
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

    /**
     * The RestoreFactoryDefaults method can be used to reset the configuration parameters
     * in the SPARK MAX to their factory default state. If no argument is passed, these
     * parameters will not persist between power cycles
     */

    // initialze PID controller and encoder objects
    leftFrontController = leftFrontDrive.getPIDController();
    rightFrontController = rightFrontDrive.getPIDController();
    leftEncoder = leftFrontDrive.getEncoder();
    rightEncoder = rightFrontDrive.getEncoder();

    // PID coefficients
    kP = 5e-5; 
    kI = 1e-6;
    kD = 0; 
    kIz = 0; 
    kFF = 0.000156; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 5700;

    // Smart Motion Coefficients
    maxVel = 2000; // rpm
    maxAcc = 2500;

    // set PID coefficients
    leftFrontController.setP(kP);
    leftFrontController.setI(kI);
    leftFrontController.setD(kD);
    leftFrontController.setIZone(kIz);
    leftFrontController.setFF(kFF);
    leftFrontController.setOutputRange(kMinOutput, kMaxOutput);

    rightFrontController.setP(kP);
    rightFrontController.setI(kI);
    rightFrontController.setD(kD);
    rightFrontController.setIZone(kIz);
    rightFrontController.setFF(kFF);
    rightFrontController.setOutputRange(kMinOutput, kMaxOutput);

    /**
     * Smart Motion coefficients are set on a CANPIDController object
     * 
     * - setSmartMotionMaxVelocity() will limit the velocity in RPM of
     * the pid controller in Smart Motion mode
     * - setSmartMotionMinOutputVelocity() will put a lower bound in
     * RPM of the pid controller in Smart Motion mode
     * - setSmartMotionMaxAccel() will limit the acceleration in RPM^2
     * of the pid controller in Smart Motion mode
     * - setSmartMotionAllowedClosedLoopError() will set the max allowed
     * error for the pid controller in Smart Motion mode
     */
    int smartMotionSlot = 0;
    leftFrontController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    leftFrontController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    leftFrontController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    leftFrontController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    rightFrontController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    rightFrontController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    rightFrontController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    rightFrontController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("P Gain", kP);
    SmartDashboard.putNumber("I Gain", kI);
    SmartDashboard.putNumber("D Gain", kD);
    SmartDashboard.putNumber("I Zone", kIz);
    SmartDashboard.putNumber("Feed Forward", kFF);
    SmartDashboard.putNumber("Max Output", kMaxOutput);
    SmartDashboard.putNumber("Min Output", kMinOutput);

    // display Smart Motion coefficients
    SmartDashboard.putNumber("Max Velocity", maxVel);
    SmartDashboard.putNumber("Min Velocity", minVel);
    SmartDashboard.putNumber("Max Acceleration", maxAcc);
    SmartDashboard.putNumber("Allowed Closed Loop Error", allowedErr);
    SmartDashboard.putNumber("Set Position", 0);
    SmartDashboard.putNumber("Set Velocity", 0);

    // button to toggle between velocity and smart motion modes
    SmartDashboard.putBoolean("Mode", true);
  }
  public void Drive(double desiredDistance) {
    velocityTimer.start();
        leftFrontController.setReference(-desiredDistance, ControlType.kSmartMotion);
        rightFrontController.setReference(desiredDistance, ControlType.kSmartMotion);
        if (velocityTimer.get() >= tLateDrive) {
        leftProcessVariable = leftEncoder.getPosition();
        rightProcessVariable = rightEncoder.getPosition();
        SmartDashboard.putNumber("Left Process Variable", leftProcessVariable * -1);
        SmartDashboard.putNumber("Right Process Variable", rightProcessVariable);
        }
      /*ballToggleButton = ballSwitch.get();
        if (ballToggleButton == false && ballToggle == 0) {
          ballToggle = 1; 

        } else if (ballToggleButton == true && ballToggle == 1) {
          ballToggle = 2;
        }
        if (ballToggle == 1 || ballToggle == 2) {
          if (frontBallTransport.getSelectedSensorPosition() * ballMagScale < ballDesiredDistance) {
            frontBallTransport.set(ControlMode.PercentOutput, -.7);
            backBallTransport.set(ControlMode.PercentOutput, -.7);
          } else if (frontBallTransport.getSelectedSensorPosition() * ballMagScale > ballDesiredDistance) {
            ballToggle = 0;
            frontBallTransport.stopMotor();
            backBallTransport.stopMotor();
            frontBallTransport.setSelectedSensorPosition(0);
          }
        }*/
  }
  @Override
  public void autonomousInit() {
  Drive(100);
  Drive(-100);
  }
  @Override
  public void autonomousPeriodic() {
    
  }
@Override
public void teleopInit() {
  leftEncoder.setPosition(0);
  rightEncoder.setPosition(0);
}
  @Override
  public void teleopPeriodic() {
    // read PID coefficients from SmartDashboard
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    double maxV = SmartDashboard.getNumber("Max Velocity", 0);
    double minV = SmartDashboard.getNumber("Min Velocity", 0);
    double maxA = SmartDashboard.getNumber("Max Acceleration", 0);
    double allE = SmartDashboard.getNumber("Allowed Closed Loop Error", 0);

    // if PID coefficients on SmartDashboard have changed, write new values to controller
    if((p != kP)) { leftFrontController.setP(p); rightFrontController.setP(p); kP = p; }
    if((i != kI)) { leftFrontController.setI(i); rightFrontController.setI(i); kI = i; }
    if((d != kD)) { leftFrontController.setD(d); rightFrontController.setD(d); kD = d; }
    if((iz != kIz)) { leftFrontController.setIZone(iz);rightFrontController.setIZone(iz); kIz = iz; }
    if((ff != kFF)) { leftFrontController.setFF(ff); kFF = ff; }
    if((max != kMaxOutput) || (min != kMinOutput)) { 
      leftFrontController.setOutputRange(min, max); rightFrontController.setOutputRange(min, max);
      kMinOutput = min; kMaxOutput = max; 
    }
    if((maxV != maxVel)) { leftFrontController.setSmartMotionMaxVelocity(maxV,0); rightFrontController.setSmartMotionMaxVelocity(maxV,0); maxVel = maxV; }
    if((minV != minVel)) { leftFrontController.setSmartMotionMinOutputVelocity(minV,0); rightFrontController.setSmartMotionMinOutputVelocity(minV,0); minVel = minV; }
    if((maxA != maxAcc)) { leftFrontController.setSmartMotionMaxAccel(maxA,0); rightFrontController.setSmartMotionMaxAccel(maxA,0); maxAcc = maxA; }
    if((allE != allowedErr)) { leftFrontController.setSmartMotionAllowedClosedLoopError(allE,0); rightFrontController.setSmartMotionAllowedClosedLoopError(allE,0); allowedErr = allE; }

    //double setPoint, leftProcessVariable, rightProcessVariable;
    boolean mode = SmartDashboard.getBoolean("Mode", false);
    if(mode) {
      setPoint = SmartDashboard.getNumber("Set Velocity", 0);
      leftFrontController.setReference(-setPoint, ControlType.kSmartVelocity);
      rightFrontController.setReference(setPoint, ControlType.kSmartVelocity);

      leftProcessVariable = leftEncoder.getVelocity();
      rightProcessVariable = rightEncoder.getVelocity();
    } else {
      //setPoint = SmartDashboard.getNumber("Set Position", 0);
      /**
       * As with other PID modes, Smart Motion is set by calling the
       * setReference method on an existing pid object and setting
       * the control type to kSmartMotion
       */
      /*leftFrontController.setReference(setPoint, ControlType.kSmartMotion);
      rightFrontController.setReference(-setPoint, ControlType.kSmartMotion);

      leftProcessVariable = leftEncoder.getPosition();
      rightProcessVariable = rightEncoder.getPosition();*/
      //Drive(100);
      //Drive(-100);
    }

  }
}