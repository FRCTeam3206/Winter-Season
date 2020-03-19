/*---------------------------------------------------------
-------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.*;

public class Robot extends TimedRobot {

  public static OI oi;
  public static DriveTrain driveTrain;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    RobotMap.init();
    driveTrain = new DriveTrain();
    oi = new OI();
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
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    Robot.oi.autonomousCommand = Robot.oi.autoChooser.getSelected();
    if (Robot.oi.autonomousCommand != null) {
      Robot.oi.autonomousCommand.start();
    }
  }

  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (Robot.oi.autonomousCommand != null) {
      Robot.oi.autonomousCommand.cancel();
    }
  }
  
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  
    //----------Left Joystick Buttons----------
    if(Robot.oi.logitechAttack3.getRawButton(1)) {//Encoder Reset
      RobotMap.leftEncoder.setPosition(0);
      RobotMap.rightEncoder.setPosition(0);
    }

    if (Robot.oi.logitechAttack3.getRawButton(4)) { //Compressor On
      RobotMap.compressor.setClosedLoopControl(true);
    } else if (Robot.oi.logitechAttack3.getRawButton(5)) { //Compressor Off
      RobotMap.compressor.setClosedLoopControl(false);
    } else {
      RobotMap.compressor.setClosedLoopControl(false);
    }

    //----------Right Joystick Buttons----------
    if (Robot.oi.logitechExtreme3D.getRawButton(1)) { //High Speed
      RobotMap.driveSolenoid.set(Value.kForward);
    } else if (Robot.oi.logitechExtreme3D.getRawButton(2)) { //Low Speed
      RobotMap.driveSolenoid.set(Value.kReverse);
    } else {
      RobotMap.driveSolenoid.set(Value.kOff); //Ensures Pistons are Off
    }    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
