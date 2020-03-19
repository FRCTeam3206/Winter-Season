// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3206.FinalArtemis.subsystems;


import org.usfirst.frc3206.FinalArtemis.commands.*;
//import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.PIDOutput;
//import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Joystick;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.kauailabs.navx.frc.*;
import edu.wpi.first.wpilibj.I2C;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class DriveTrain extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX leftTalon;
    private WPI_TalonSRX rightTalon;
    private DifferentialDrive artemisDrive;
    private WPI_VictorSPX leftSlave;
    private WPI_VictorSPX rightSlave;
    private Compressor compressor;
    private DoubleSolenoid gearShifter;
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    private AHRS navxMicro = new AHRS(I2C.Port.kOnboard);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public DriveTrain() {
        //Drivetrain Motors
        leftTalon = new WPI_TalonSRX(5);
        rightTalon = new WPI_TalonSRX(4);
        leftSlave = new WPI_VictorSPX(3);
        rightSlave = new WPI_VictorSPX(7);
        leftSlave.follow(leftTalon);
        rightSlave.follow(rightTalon);
        leftTalon.setInverted(false); // <<<<<< Adjust this until robot drives forward when stick is forward
		rightTalon.setInverted(true); // <<<<<< Adjust this until robot drives forward when stick is forward
		leftSlave.setInverted(InvertType.FollowMaster);
		rightSlave.setInverted(InvertType.FollowMaster);

        //Differential Drive
        artemisDrive = new DifferentialDrive(leftTalon, rightTalon);
        addChild("Differential Drive",artemisDrive);
        artemisDrive.setSafetyEnabled(true);
        artemisDrive.setExpiration(0.1);
        artemisDrive.setMaxOutput(1.0);
        
        //Compressor and Pneumatic Gearbox Shifter
        compressor = new Compressor(0);
        addChild("Compressor",compressor);
        gearShifter = new DoubleSolenoid(0, 0, 1);
        addChild("GearShifter",gearShifter);
        
        //Encoders
        leftEncoder = new Encoder(0, 1, false, EncodingType.k4X);
        addChild("LeftEncoder",leftEncoder);
        leftEncoder.setDistancePerPulse(.00072);
        leftEncoder.setPIDSourceType(PIDSourceType.kRate);
        
        rightEncoder = new Encoder(2, 3, false, EncodingType.k4X);
        addChild("RightEncoder",rightEncoder);
        rightEncoder.setDistancePerPulse(.00072);
        rightEncoder.setPIDSourceType(PIDSourceType.kRate);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TeleopDrive());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void takeJoystickInputs(Joystick left, Joystick right) {
    	artemisDrive.tankDrive(-left.getY() * .75, right.getY()*.75);
    }

    //Drive
    public void Drive(double Speed) {
        //m_robotDrive.arcadeDrive(-Speed, 0);
        artemisDrive.tankDrive(-Speed, -Speed);
    }
    
    public void Rotate(double Speed) { 
        artemisDrive.arcadeDrive(0, Speed);
    }

    public void Brake() {
        leftTalon.setNeutralMode(NeutralMode.Brake);
        rightTalon.setNeutralMode(NeutralMode.Brake);
    }

    public void Coast() {
        leftTalon.setNeutralMode(NeutralMode.Coast);
        rightTalon.setNeutralMode(NeutralMode.Coast);
    }

   public void stop() {
       artemisDrive.tankDrive(0, 0);
   }

   //Gears
   public void LowSpeed() {
    gearShifter.set(Value.kForward);
   }

   public void HighSpeed() {
    gearShifter.set(Value.kReverse);
   }

   //Compressor
   public void On() {
       compressor.setClosedLoopControl(true);
   }

   public void Off() {
       compressor.setClosedLoopControl(false);
   }

   //Encoders
   public double getLeftDistance() {
      return leftEncoder.getDistance();
   }

   public double getRightDistance() {
      return rightEncoder.getDistance();
   }

   public void resetEncoders() {
       leftEncoder.reset();
       rightEncoder.reset();
   }

   //Navx Micro Functions
   public void resetNavX() {
       navxMicro.reset();
   }

   public double getYaw() {
    return navxMicro.getYaw();
   }

}
