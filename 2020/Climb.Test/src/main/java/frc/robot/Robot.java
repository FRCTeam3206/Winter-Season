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
  WPI_VictorSPX climbTopMotor = new WPI_VictorSPX(6);
  WPI_VictorSPX climbBottomMotor = new WPI_VictorSPX(7);
  //DoubleSolenoid climbSol = new DoubleSolenoid(0, 3, 5);
  //Compressor compressor = new Compressor(0);
  Joystick arcadeStick = new Joystick(0);
  double climbSpeed = 1;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    SmartDashboard.putNumber("Climb Motor Speed", climbSpeed);
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
    double cs = SmartDashboard.getNumber("Climb Motor Speed", climbSpeed);
    if (cs != climbSpeed) {climbSpeed = cs;}
    if (arcadeStick.getRawButton(1)) {
      climbTopMotor.set(ControlMode.PercentOutput, climbSpeed);
      climbBottomMotor.set(ControlMode.PercentOutput, climbSpeed);
      //climbSol.set(Value.kForward);
    } else if (arcadeStick.getRawButton(2)) {
      climbTopMotor.set(ControlMode.PercentOutput, -climbSpeed);
      climbBottomMotor.set(ControlMode.PercentOutput, -climbSpeed);
    } else {
      climbTopMotor.set(ControlMode.PercentOutput, 0);
      climbBottomMotor.set(ControlMode.PercentOutput, 0);
      //climbSol.set(Value.kReverse);
    }
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
