/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.shuffleboard.*;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Shooter Motors
    WPI_TalonSRX leftShooter = new WPI_TalonSRX(8); 
    WPI_TalonSRX rightShooter = new WPI_TalonSRX(9); 

  // Shooting Variables
    double kPLeft = .4;
    double kILeft = .0009;
    double kDLeft = 0;
    double kFLeft = .0005;
    double currentRPM;
    double motorRPM = 980;//Use this variable to change your Rpm(0-1873 for the Rs775 Pro with a 10:1 gearbox) on the fly
    double encoderCounts = 4096;
    double minute = 600;
    boolean runShooter = false;
    boolean velocityControl = true;

    int shooterToggle = 0;
    boolean shooterToggleStick;
    Timer velocityTimer = new Timer();
    /**
     * The target velocity is calculated by taking the desired speed (in RPM) 
     * and multiplying it by the number of encoder counts per revolution (in this case, 4096)
     * you then divide by 600 to convert from minutes to milliseconds
     * This function is called every 100ms, so 100 * 600 = 60000
     * 60000ms/1000 = 60 seconds = 1 minute
     * In this case, with a 10:1 gearbox on the Rs775 Pros, I chose to go for 900RPM (a middle ground value)
     * (900RPM * 4096 encoder counts) / 600ms = 6144 encoder count units per 100ms
     */
    double targetVelocity_UnitsPer100ms = 6700;//6690.133; //980 * 4096 encoder counts / 600;

          //Ball Transport
          WPI_TalonSRX frontBallTransport = new WPI_TalonSRX(10); 
          WPI_TalonSRX backBallTransport = new WPI_TalonSRX(11); 
          DigitalInput ballSwitch = new DigitalInput(3);
    
          XboxController weaponStick = new XboxController(0);
        //Ball Transport
        double currentPos = 0;
        double startPos = 0;
        double targetPos = 0;
        double magScale = .00306636;//(1/4096 * 3.14 * 4) scaled to inches instead
        double desiredDistance;
        double distanceTraveled = 0;
        double transportSpeed = -1;

  @Override
  public void robotInit() {
      rightShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
      rightShooter.configNominalOutputForward(0, 30);
		  rightShooter.configNominalOutputReverse(0, 30);
	   	rightShooter.configPeakOutputForward(1, 30);
      rightShooter.configPeakOutputReverse(-1, 30); 
    
      rightShooter.config_kF(0, kFLeft, 30);
		  rightShooter.config_kP(0, kPLeft, 30);
		  rightShooter.config_kI(0, kILeft, 30);
      rightShooter.config_kD(0, kDLeft, 30);
    
      leftShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
      leftShooter.configNominalOutputForward(0, 30);
		  leftShooter.configNominalOutputReverse(0, 30);
	   	leftShooter.configPeakOutputForward(1, 30);
      leftShooter.configPeakOutputReverse(-1, 30); 
    
      leftShooter.config_kF(0, kFLeft, 30);
		  leftShooter.config_kP(0, kPLeft, 30);
		  leftShooter.config_kI(0, kILeft, 30);
      leftShooter.config_kD(0, kDLeft, 30);
    
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
  
    }
  
  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Realtime Velocity", leftShooter.getSelectedSensorVelocity());
    SmartDashboard.putNumber("kPLeft", kPLeft);
    SmartDashboard.putNumber("kILeft", kILeft);
    SmartDashboard.putNumber("kFLeft", kFLeft);

    //basic way to run the transport
    if (weaponStick.getRawButton(1)) {
      frontBallTransport.set(ControlMode.PercentOutput, transportSpeed);
      backBallTransport.set(ControlMode.PercentOutput, transportSpeed);
    } else {
      frontBallTransport.set(ControlMode.PercentOutput, 0);
      backBallTransport.set(ControlMode.PercentOutput, 0);
    }
    //Shooter & Transport Toggle
    shooterToggleStick = weaponStick.getYButtonPressed();
    //Set Button to Integer Value
    if (shooterToggleStick == true && shooterToggle == 0) { //First Press
      shooterToggle = 1; //If trigger is pressed and toggle hasn't been set yet/has cycled through then toggle = 1
    } else if (shooterToggleStick == false && shooterToggle == 1) { //First Release
      shooterToggle = 2; //If trigger is  released and toggle = 1 then toggle = 2
    } else if (shooterToggleStick == true && shooterToggle == 2) { //Second Press
      shooterToggle = 3; //If trigger is pressed and toggle = 2 then toggle = 3
    } else if (shooterToggleStick == false && shooterToggle == 3) { //Second Release
      shooterToggle = 0; //If trigger is released and toggle = 3 then toggle = 0
    } else if (shooterToggleStick == false && shooterToggle == 0) { //Completes the Cycle/Redundancy Backup
      shooterToggle = 0; //If trigger is released and toggle = 0 then toggle = 0
    }

  //Determine Piston Position Based on Integer Value
      if (shooterToggle == 1) {//makes sure the timer only starts once per cycle
      velocityTimer.start();
    } else if (shooterToggle == 1 || shooterToggle == 2) { //1st Toggle
      //rightShooter.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
      leftShooter.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
      if (velocityTimer.get() >= 1) {
        //frontBallTransport.set(ControlMode.PercentOutput, transportSpeed);
        //backBallTransport.set(ControlMode.PercentOutput, transportSpeed);
      }
    } else if (shooterToggle == 3 || shooterToggle == 0) { //2nd Toggle
      //rightShooter.stopMotor();
      leftShooter.stopMotor();
      //frontBallTransport.stopMotor();
      //backBallTransport.stopMotor();
  }

}
@Override
public void testPeriodic() {
}
  @Override
  public void disabledInit() {
    Shuffleboard.stopRecording();
  }
}


