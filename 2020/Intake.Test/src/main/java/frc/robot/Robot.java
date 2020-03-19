/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    WPI_VictorSPX intakeMotor = new WPI_VictorSPX(5);
    //DoubleSolenoid intakeSol = new DoubleSolenoid(0, 2, 4);
    //Compressor compressor = new Compressor(0);
    Joystick arcadeStick = new Joystick(0);
    double intakeSpeed = .5;

    //Hatch Toggle Variables
    int toggle = 0;
    boolean toggleStick = arcadeStick.getRawButtonPressed(1);
    int i = 0;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    SmartDashboard.putNumber("Intake Speed", intakeSpeed);
       //Resets Toggle State to Off to Be Safe
       toggle = 0;
       i = 0;
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
    double is = SmartDashboard.getNumber("Intake Speed", .5);
    if (is != intakeSpeed) {intakeSpeed = is;}


    toggleStick = arcadeStick.getRawButtonPressed(1);
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

    //Compressor
    if (arcadeStick.getRawButton(2)) { //Compressor Off
      //compressor.setClosedLoopControl(false);
    } else if (arcadeStick.getRawButton(4)) { //Compressor On
      //compressor.setClosedLoopControl(true);
    } else {
      //compressor.setClosedLoopControl(false);
    }

  }

}
