/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
      //Ball Transport
      WPI_TalonSRX frontBallTransport = new WPI_TalonSRX(10); 
      WPI_TalonSRX backBallTransport = new WPI_TalonSRX(11); 
      DigitalInput ballSwitch = new DigitalInput(3);

      Joystick arcadeStick = new Joystick(0);
    //Ball Transport
    double currentPos = 0;
    double startPos = 0;
    double targetPos = 0;
    double rpmSetpoint = 1000;
    double magScale = .00306636;//(1/4096 * 3.14 * 4) scaled to inches instead
    double desiredDistance;
    double distanceTraveled = 0;
    double motorSpeed = -.5; 
    double topSpeed = -.5;
    double bottomSpeed = -.5;
    WPI_VictorSPX intakeMotor = new WPI_VictorSPX(5);
    //DoubleSolenoid intakeSol = new DoubleSolenoid(0, 2, 3);
    //Compressor compressor = new Compressor(0);
    double intakeSpeed = .5;

    //Hatch Toggle Variables
    int toggle = 0;
    boolean toggleStick = arcadeStick.getRawButtonPressed(2);
    int i = 0;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
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

    SmartDashboard.putNumber("Top Transport Speed", topSpeed);
    SmartDashboard.putNumber("Bottom Transport Speed", bottomSpeed);
    SmartDashboard.putNumber("Intake Speed", intakeSpeed);
    //Resets Toggle State to Off to Be Safe
    toggle = 0;
    i = 0;
  }
  public void Spin (double distance) {
    frontBallTransport.setSelectedSensorPosition(0);
    backBallTransport.setSelectedSensorPosition(0);
    distanceTraveled = 0;
    desiredDistance = distance;
    while (desiredDistance > distanceTraveled) {
      distanceTraveled = backBallTransport.getSelectedSensorPosition() * magScale;
      frontBallTransport.set(ControlMode.PercentOutput, motorSpeed);
      backBallTransport.set(ControlMode.PercentOutput, motorSpeed);
      SmartDashboard.putNumber("Distance Traveled", distanceTraveled); 
    }
    frontBallTransport.set(ControlMode.PercentOutput, 0);
    backBallTransport.set(ControlMode.PercentOutput, 0);
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
    ballSwitch.get();
    //runs the transport if the limit switch is triggered
  if (ballSwitch.get() == false) {
      Spin(7.5);
  } else {
    frontBallTransport.set(ControlMode.PercentOutput, 0);
    backBallTransport.set(ControlMode.PercentOutput, 0);
  }

  //basic way to run the transport
  if (arcadeStick.getRawButton(1)) {
    frontBallTransport.set(ControlMode.PercentOutput, topSpeed);
    backBallTransport.set(ControlMode.PercentOutput, bottomSpeed);
  } else {
    frontBallTransport.set(ControlMode.PercentOutput, 0);
    backBallTransport.set(ControlMode.PercentOutput, 0);
  }

  double ts = SmartDashboard.getNumber("Top Transport Speed", .5);
  double bs = SmartDashboard.getNumber("Bottom Transport Speed", .5);
  if (ts != topSpeed) {topSpeed = ts;}
  if (bs != bottomSpeed) {bottomSpeed = bs;}

  double is = SmartDashboard.getNumber("Intake Speed", .5);
    if (is != intakeSpeed) {intakeSpeed = is;}


    toggleStick = arcadeStick.getRawButtonPressed(2);
    //Set Button to Integer Value
    if (toggleStick == true && toggle == 0) { //First Press
      toggle = 1; //If trigger is pressed and toggle hasn't been set yet/has cycled through then toggle = 1
    } else if (toggleStick == false && toggle == 1) { //First Release
      toggle = 2; //If trigger is  released and toggle = 1 then toggle = 2
    } else if (toggleStick == true && toggle == 2) { //Second Press
      toggle = 3; //If trigger is pressed and toggle = 2 then toggle = 3
    } else if (toggleStick == false && toggle == 3) { //Second Release
      toggle = 0; //If trigger is released and toggle = 3 then toggle = 0
    } else if (toggleStick == false && toggle == 0) { //Completes the Cycle/Redundancy Backup
      toggle = 0; //If trigger is released and toggle = 0 then toggle = 0
    }

    //Determine Piston Position Based on Integer Value
    if (toggle == 1 || toggle == 2) { //Trigger is Pressed
      //intakeSol.set(Value.kForward); //Hatch Arm Down
        if (i < 50) { //50 = 1 second(1 = 20 milliseconds)
          intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
          //System.out.println(i);
          i++;
      } else if (i > 50) {
        intakeMotor.set(ControlMode.PercentOutput, 0);
      }
    } else if (toggle == 3 || toggle == 0) { //Trigger is Released
      //intakeSol.set(Value.kReverse); //Hatch Arm Up
      intakeMotor.set(ControlMode.PercentOutput, 0);
      i = 0;
    } //End Hatch Toggle Code

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
