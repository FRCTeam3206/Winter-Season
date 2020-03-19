/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//Motor Controllers & Drive Things
  import com.ctre.phoenix.motorcontrol.InvertType;
  import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
  import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
  import edu.wpi.first.wpilibj.drive.DifferentialDrive;
  import edu.wpi.first.wpilibj.Spark;
  import edu.wpi.first.wpilibj.Joystick;

//Pneumatics
  import edu.wpi.first.wpilibj.Compressor;
  import edu.wpi.first.wpilibj.Solenoid;
  import edu.wpi.first.wpilibj.DoubleSolenoid;
  import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

//Cameras
  import edu.wpi.first.cameraserver.CameraServer;
  import edu.wpi.cscore.UsbCamera;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

//Joysticks
  Joystick leftJoystick = new Joystick(0);
  Joystick rightJoystick = new Joystick(1);
  Joystick weaponsStick = new Joystick(2);

  /*Weapons Button Assignments
    A: Elevator Down
    B: 
    X: Hatch Arm
    Y: Elevator Up
    LT: Ball Extake 
    LB: 
    RT: Ball Intake
    RB:
    Left Knob X-Axis: Hatch Extake 
    Left Knob Y-Axis: Hatch Intake
    Right Knob X-Axis:
    Right Knob Y-Axis:
    D-Pad Up: Climb Release
    D-Pad Right: Climb Lower
    D-Pad Down: Climb Clamp
    D-Pad Left: Climb Raise
  */

//----------Motor Controllers------------
  //Left Drivetrain Motors
    WPI_TalonSRX leftTalon = new WPI_TalonSRX(5);
    WPI_VictorSPX leftSlave = new WPI_VictorSPX(3);

  //Right Drivetrain Motors
    WPI_TalonSRX rightTalon = new WPI_TalonSRX(4);
    WPI_VictorSPX rightSlave = new WPI_VictorSPX(7);

  // Left and Right Intake Motors 
    WPI_VictorSPX leftIntake = new WPI_VictorSPX(6);
    WPI_VictorSPX rightIntake = new WPI_VictorSPX(2);

  // Window Motor
    WPI_VictorSPX hatchIntake = new WPI_VictorSPX(8);

  //Spark Motors
    Spark elevatorSpark = new Spark(0);
    Spark climbSpark = new Spark(1);
  
//Differential Drives
  DifferentialDrive artemisDrive = new DifferentialDrive(leftTalon, rightTalon);
  DifferentialDrive intakeDrive = new DifferentialDrive(leftIntake, rightIntake);

//Pneumatics 
  Compressor comp1 = new Compressor(0);
  DoubleSolenoid driveSol = new DoubleSolenoid(0, 0, 1);
  DoubleSolenoid climbSol = new DoubleSolenoid(0, 4, 5);
  Solenoid hatchDeploy = new Solenoid(0, 2);

//Acceleration Limiting Variables
  boolean accelerationLimiting = false;
  double accelLimitedLeftGetY;
  double accelLimitedRightGetY;
  double accelKonstant = 6; // Change from 2-32. 32 is super slow to react, 2 is little improvement

//Intake and Drive Coefficients
  double intakeSpeed = .7;
  double extakeSpeed = -.7;
  double drivecoefficientL = .9;
  double drivecoefficientR = .9;

//Hatch Toggle Variables
  int toggle = 0;
  boolean toggleStick = weaponsStick.getRawButtonPressed(1);
  int i = 0;

  @Override
  public void robotInit() {
    //Resets Toggle State to Off to Be Safe
      toggle = 0;
      i = 0;
    
    //Camera Instantiation and Framerate Settings
      UsbCamera groundCam = CameraServer.getInstance().startAutomaticCapture();
      groundCam.setFPS(8);
      UsbCamera hatchCam = CameraServer.getInstance().startAutomaticCapture();
      hatchCam.setFPS(18);

    //Configure Motor Controllers to Default as a Safe Practice
      leftTalon.configFactoryDefault();
      rightTalon.configFactoryDefault();
      leftSlave.configFactoryDefault();
      rightSlave.configFactoryDefault();

      leftIntake.configFactoryDefault();
      rightIntake.configFactoryDefault();
  
    //Slave Motors Together to Allow for Easier Control
      leftSlave.follow(leftTalon);
      rightSlave.follow(rightTalon);

    //Inverts Motor if Needed
      leftTalon.setInverted(true);
      rightTalon.setInverted(false);
      leftSlave.setInverted(InvertType.FollowMaster);
      rightSlave.setInverted(InvertType.FollowMaster);

		/* differential drive assumes (by default) that 
			right side must be negative to move forward.
			Change to 'false' so positive/green-LEDs moves robot forward  */
      artemisDrive.setRightSideInverted(false); // do not change this
   
    }

  @Override
  public void robotPeriodic() {
    //This attempts to reduce the drastic acceleration changes and not brown out the robot
    //In declaration change accelKonstant from 2-32, 32 is super slow to react, 2 is little improvement.
    if(accelerationLimiting == true){
      accelLimitedLeftGetY =  leftJoystick.getY()  / accelKonstant + accelLimitedLeftGetY * (accelKonstant - 1) / accelKonstant;
      accelLimitedRightGetY =  rightJoystick.getY() / accelKonstant + accelLimitedRightGetY * (accelKonstant - 1) / accelKonstant;
      artemisDrive.tankDrive(accelLimitedLeftGetY * drivecoefficientL, accelLimitedRightGetY * drivecoefficientR);
     }
     else{  
      artemisDrive.tankDrive(leftJoystick.getY() * drivecoefficientL, rightJoystick.getY() * drivecoefficientR); // No acceleration limiting.
     }
     
    //-----------Left Joystick Buttons---------- 
    // Basic Hatch Deploy (As Backup)
    /*
    if (leftJoystick.getRawButton(1)) {
      hatchDeploy.set(true);
    } else {
      hatchDeploy.set(false);
    }
    */
    
    //Hatch Toggle
    toggleStick = weaponsStick.getRawButtonPressed(1); //Reads the Trigger State
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
      hatchDeploy.set(true); //Hatch Arm Down
        if (i < 50) { //50 = 1 second(1 = 20 milliseconds)
          hatchIntake.set(.65); //Runs Hatch Intake for 1 second
          //System.out.println(i);
          i++;
      } else if (i > 50) {
        hatchIntake.set(0);
      }
    } else if (toggle == 3 || toggle == 0) { //Trigger is Released
      hatchDeploy.set(false); //Hatch Arm Up
      hatchIntake.set(0);
      i = 0;
    } //End Hatch Toggle Code

    //Elevator Up and Down
    if (weaponsStick.getRawButton(2)) { //Elevator Down
      elevatorSpark.set(-.4);
    } else if (weaponsStick.getRawButton(4)) { //Elevator Up
      elevatorSpark.set(.75);
    } else {
      elevatorSpark.set(0); //Stops Elevator When Nothing is Pressed
    }
  
    //Hatch Intake/Extake
    if(weaponsStick.getRawButton(8)){ //Hatch Intake
      hatchIntake.set(.65);
    } else if (weaponsStick.getRawButton(7)){ //Hatch Extake
      hatchIntake.set(-.65);
    } else {
      hatchIntake.set(0); //Stops Motors if Not Being Used
    }
     
    //Compressor
    if (weaponsStick.getRawButton(5)) { //Compressor On
      comp1.setClosedLoopControl(true);
    } else if (weaponsStick.getRawButton(6)) { //Compressor Off
      comp1.setClosedLoopControl(false);
    }

    //Climb Motor
    //Motor positions
    int POV = weaponsStick.getPOV();
    if (POV == 270) { //Retract Arms
    climbSpark.set(1);    
    } else if(POV == 90) { //Extend Arms
    climbSpark.set(-1);
    } else {
    climbSpark.set(0); //Stops Motors if not being used
    }

    //----------Right Joystick Buttons----------
    //Gear Shifting buttons
    if (rightJoystick.getRawButton(1)) { //High Speed
      driveSol.set(Value.kForward);
    } else if (rightJoystick.getRawButton(2)) { //Low Speed
      driveSol.set(Value.kReverse);
    } else {
      driveSol.set(Value.kOff); //Ensures Pistons are Off
    }

    //Climb Pneumatics
    if (POV == 0) { //Unclamp Pistons
      climbSol.set(Value.kReverse);
    } else if(POV == 180) { //Clamp Pistons
      climbSol.set(Value.kForward);
    } else {
      climbSol.set(Value.kOff); //Pistons Don't Move
    }

    //Intake and Extake Buttons
    if(rightJoystick.getRawButton(4)){ //Extake
      intakeDrive.tankDrive(extakeSpeed, extakeSpeed);
    } else if(rightJoystick.getRawButton(6)){ //Intake
      intakeDrive.tankDrive(intakeSpeed, intakeSpeed); 
    } else {
      intakeDrive.tankDrive(0, 0); //Stops Motors if Not Being Used
    } 
  }
}