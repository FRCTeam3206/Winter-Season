/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3206.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kLeft = "Left";
	private static final String kRight = "Right";
	private static final String kCenter = "Center";
	private static final String kAutoLine = "Auto Line";
	

	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();	

	VictorSP m_left = new VictorSP(0);
	 VictorSP m_right = new VictorSP(1);
	 VictorSP m_intakeleft = new VictorSP(3);
	 VictorSP m_intakeright = new VictorSP(4);
	 VictorSP m_climb = new VictorSP(5);
	 Spark m_elevator = new Spark(2);

		DifferentialDrive m_intakeDrive = new DifferentialDrive(m_intakeleft, m_intakeright);
	DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);
	
	private Joystick m_stickleft = new Joystick(0); //the input may need to be changed on the driver station
	private Joystick m_stickright = new Joystick(1); //make sure that the USB input matches the joystick input number in the code
	
	double ElevatorUpSpeed = - 1;//moves the elevator [if <0 then down else >0 up] at ElevatorUpSpeed
	double ElevatorDownSpeed = .5;//moves the elevator [if <0 then down else >0 up] at ElevatorDownSpeed
	double IntakeInSpeed = -.9;//-(m_stickleft.getRawAxis(2) - 1) / 2;
	double IntakeOutSpeed = .9;//-(m_stickright.getRawAxis(3) - 1) / 2;
	double ClimbUpSpeed = -1;
	double ClimbDownSpeed = 0.3;
	double ConstantIntakeSpeed = .2;
	double DriveSpeed = -.5;
	double RotationSpeed = .5;
	double RunTimer = 0;
	String gameData = "   ";
	
	//CameraServer Camera1;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Left", kLeft);
		m_chooser.addDefault("Left", kRight);
		m_chooser.addObject("Center", kCenter);
		SmartDashboard.putData("Auto Location", m_chooser);
		
		m_robotDrive.setExpiration(2);
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
		
		try {
    		gameData = DriverStation.getInstance().getGameSpecificMessage();
    		SmartDashboard.putString("Game Data", gameData);
    	} catch(Exception e) {
    		System.out.println("Error Game Data Null");
    	}
		m_autoSelected = m_chooser.getSelected();
		m_autoSelected = SmartDashboard.getString("Auto Selector", kAutoLine);
		SmartDashboard.putString("Auto Selected: ", m_autoSelected);//System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
		switch (m_autoSelected) {
			case kCenter:
				//Case Left Cube Switch
				if (gameData.charAt(0) == 'L') {
					RunTimer = System.currentTimeMillis();
					
					while (false || (System.currentTimeMillis()-RunTimer) <1500) {
						SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
						m_elevator.set(ElevatorUpSpeed);
					}
					m_elevator.set(0);
					while (false || (System.currentTimeMillis()-RunTimer) <5500) {
						SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
						m_robotDrive.tankDrive(DriveSpeed, DriveSpeed);
					}
					m_robotDrive.tankDrive(0, 0);
					while (false || (System.currentTimeMillis()-RunTimer) <3000) {
						SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
						m_intakeDrive.tankDrive(IntakeOutSpeed, IntakeOutSpeed);
					}
					m_intakeDrive.tankDrive(0, 0);
					
					//Case Right-Drive Straight
				} else if (gameData.charAt(0) == 'R') {
					RunTimer = System.currentTimeMillis();
					
					while (false || (System.currentTimeMillis()-RunTimer) <7000) {
						SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
						m_robotDrive.tankDrive(DriveSpeed, DriveSpeed);
					}
					m_robotDrive.tankDrive(0, 0);
				}
				break;
			case kRight:
					RunTimer = System.currentTimeMillis();
					
					while (false || (System.currentTimeMillis()-RunTimer) <7000) {
						SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
						m_robotDrive.tankDrive(DriveSpeed, DriveSpeed);
					}
					m_robotDrive.tankDrive(0, 0);
					//crude code for auto line
					//m_robotDrive.tankDrive(DriveSpeed, DriveSpeed);
					//Timer.delay(5);
				break;
			case kLeft:
				RunTimer = System.currentTimeMillis();
				
				while (false || (System.currentTimeMillis()-RunTimer) <7000) {
					SmartDashboard.putNumber("Current Time", System.currentTimeMillis());
					m_robotDrive.tankDrive(DriveSpeed, DriveSpeed);
				}
				m_robotDrive.tankDrive(DriveSpeed, 0);
				
				//crude code for auto line
				//m_robotDrive.tankDrive(DriveSpeed, DriveSpeed);
				//Timer.delay(5);
				
				/*
				m_robotDrive.arcadeDrive(0, RotationSpeed);
				
				m_robotDrive.tankDrive(-DriveSpeed, -DriveSpeed);
				Timer.delay(4);
				m_robotDrive.tankDrive(-DriveSpeed, -DriveSpeed);
				Timer.delay(4);
				m_robotDrive.tankDrive(-DriveSpeed, -DriveSpeed);
				Timer.delay(4)
				*/
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		m_robotDrive.setSafetyEnabled(true);
		
		while (isOperatorControl() && isEnabled()) {

			m_robotDrive.tankDrive (m_stickleft.getY()*0.75, m_stickright.getY()*0.75); 
			//SmartDashboard.putNumber("Intake", IntakeInSpeed);
			//SmartDashboard.putNumber("Outtake", IntakeOutSpeed);	
			//IntakeInSpeed = -(m_stickleft.getRawAxis(2) - 1) / 2;
			//IntakeOutSpeed = -(m_stickright.getRawAxis(3) - 1) / 2;

			
			//Intake Buttons Below
			
			
			if (m_stickleft.getRawButton(1)) { //In Button
				m_intakeDrive.tankDrive(IntakeInSpeed, IntakeInSpeed);
				
			} else if (m_stickleft.getRawButton(4)) { //Out Button
				m_intakeDrive.tankDrive(IntakeOutSpeed, IntakeOutSpeed);
				
			} else if(m_stickleft.getRawButton(5)) {
				m_intakeDrive.tankDrive(IntakeOutSpeed * .6, IntakeOutSpeed * .6);
				
			} 
			
			//Elevator Buttons Below
			
			
			if (m_stickright.getRawButton(1)) { //make sure the elevator doesn't kill itself
				m_elevator.set(ElevatorUpSpeed); //The elevator motor must be set to PWM pin 2

				
			}else if (m_stickright.getRawButton(3)) {
				m_elevator.set(ElevatorDownSpeed);

			} else {
			m_elevator.set(0);
		}
			
			//Climb Buttons Below
			
			
			if(m_stickright.getRawButton(6)) {
				m_climb.set(-ClimbUpSpeed);
				
			} else if(m_stickright.getRawButton(5)) {
				m_climb.set(ClimbDownSpeed);
				
			} else {
				m_climb.set(0);
			}
			
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
