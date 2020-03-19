/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.*;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Timer;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

  public static Compressor compressor;
  public static DoubleSolenoid driveSolenoid;
  public static Solenoid leftIntake;
  public static Solenoid rightIntake;

//DriveTrain
  //Left Drivetrain Motors
  public static CANSparkMax leftFrontDrive;
  public static CANSparkMax leftBackDrive;  
  //Right Drivetrain Motors
  public static CANSparkMax rightFrontDrive;
  public static CANSparkMax rightBackDrive;
    //DriveTrain Encoders
    public static CANEncoder leftEncoder;
    public static CANEncoder rightEncoder;
    //Gyro
      public static ADIS16448_IMU imu;

//Intake 
public static WPI_VictorSPX intakeMotor;
//Motor Speed
public static double intakeSpeed;

//Climb 
public static WPI_VictorSPX climbTopMotor;
public static WPI_VictorSPX climbBottomMotor;
//Motor Speed
public static double climbSpeed;

//Shooter 
public static WPI_TalonSRX leftShooter; 
public static WPI_TalonSRX rightShooter; 

//Ball Transport
public static WPI_TalonSRX frontBallTransport; 
public static WPI_TalonSRX backBallTransport; 
public static DigitalInput ballSwitch;
//Variables
public static double currentPos;
public static double startPos;
public static double targetPos;
public static double rpmSetpoint;
public static double ballMagScale;
public static double ballDesiredDistance;
public static double ballDistanceTraveled;
public static double frontBallSpeed;
public static double backBallSpeed;

//Control Panel 
public static WPI_TalonSRX WheelSpinner;
//Color Sensor
public static I2C.Port i2cPort = I2C.Port.kOnboard;
public static ColorSensorV3 m_colorSensor;
public static ColorMatch m_colorMatcher;
//Colors
public static Color kBlueTarget;
public static Color kGreenTarget;
public static Color kRedTarget;
public static Color kYellowTarget;
//Spin Function Variables
public static double spinnerMagScale;
public static double spinnerDesiredDistance;
public static double spinnerDistanceTraveled;
public static double spinnerMotorSpeed; //.85 max to get 60rpm with a 25:1 gear ratio
//Color Match Variables
public static String originalV3Color;
public static String gameData;
public static Timer gameDataTimer;
public static double gameDataTimeOut;

//Joysticks
public static Joystick leftStick;
public static Joystick rightStick;
public static Joystick weaponStick;

//Drive Systems
public static DifferentialDrive chewbreakaDrive; 
public static DifferentialDrive transportDrive;
public static DifferentialDrive shooterDrive;
public static DifferentialDrive climbDrive;

// Drive Variables
public static double leftDriveSpeed;
public static double rightDriveSpeed;
public static double leftDriveCoef;
public static double rightDriveCoef;
public static double triggerDeadzone;
public static double driveTopSpeed;
public static double slideMotorSpeed;
public static double slideMotorCoef;
public static double turnSpeed;
public static double driveSpeed;

//Acceleration Limiting Variables
public static boolean accelerationLimiting;
public static double accelLimitedLeftGetY;
public static double accelLimitedRightGetY;
public static double accelLimitedSlideDrive;
public static double accelDriveKonstant; // Change from 2-32. 32 is super slow to react, 2 is little improvement
public static double accelSlideKonstant;
// Shooting Variables
public static double rightShooterSpeed;
public static double leftShooterSpeed;

public static double kPShoot;
public static double kIShoot;
public static double kDShoot;
public static double kFShoot;
public static double currentRPM;
public static double motorRPM;//Use this variable to change your Rpm(0-1873 for the Rs775 Pro with a 10:1 gearbox) on the fly
public static double encoderCounts;
public static double minute;
public static boolean runShooter;
public static double targetVelocity_UnitsPer100ms; 

//PID
public static double kPTurn;
public static double kITurn;
public static double kDTurn;
public static boolean runDriveTrain;
public static double integral, error, derivative, rcw, previous_error, setpoint;
public static boolean velocityControl;

    public static void init() {

      compressor = new Compressor(0);
      driveSolenoid = new DoubleSolenoid(0,0,1);
      leftIntake = new Solenoid(0,2);
      rightIntake = new Solenoid(0,3);

//DriveTrain
  //Left Drivetrain Motors
  leftFrontDrive = new CANSparkMax(1, MotorType.kBrushless);
  leftBackDrive = new CANSparkMax(2, MotorType.kBrushless);  
  //Right Drivetrain Motors
    rightFrontDrive = new CANSparkMax(3, MotorType.kBrushless);
    rightBackDrive = new CANSparkMax(4, MotorType.kBrushless);
      leftBackDrive.follow(leftFrontDrive);
      rightBackDrive.follow(rightFrontDrive);
        imu = new ADIS16448_IMU();
      
      leftEncoder = new CANEncoder(leftFrontDrive);
      rightEncoder = new CANEncoder(rightFrontDrive);
      leftEncoder.setPositionConversionFactor(.083333);//scaled to feet
      rightEncoder.setPositionConversionFactor(.083333);//scaled to feet

//Intake 
  intakeMotor = new WPI_VictorSPX(5);
  //Motor Speed
    intakeSpeed = .5;

//Climb 
  climbTopMotor = new WPI_VictorSPX(6);
  climbBottomMotor = new WPI_VictorSPX(7);
    //Motor Speed
      climbSpeed = .3;

//Shooter 
leftShooter = new WPI_TalonSRX(8); 
rightShooter = new WPI_TalonSRX(9); 

//Ball Transport
  frontBallTransport = new WPI_TalonSRX(10); 
  backBallTransport = new WPI_TalonSRX(11); 
  ballSwitch = new DigitalInput(0);
  //Variables
    currentPos = 0;
    startPos = 0;
    targetPos = 0;
    rpmSetpoint = 1000;
    ballMagScale = .00306636;//(1/4096 * 3.14 * 4) scaled to inches instead
    ballDesiredDistance = 0;
    ballDistanceTraveled = 0;
    frontBallSpeed = .5;
    backBallSpeed = .5;

//Control Panel 
 WheelSpinner = new WPI_TalonSRX(12);
//Color Sensor
  i2cPort = I2C.Port.kOnboard;
  m_colorSensor = new ColorSensorV3(i2cPort);
  m_colorMatcher = new ColorMatch();
  //Colors
    kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
//Spin Function Variables
spinnerMagScale = .00025553;//((1/4096 * 3.14 * 4)/12) scaling factor to convert the raw encoder value to distance traveled
spinnerDesiredDistance = 0;
spinnerDistanceTraveled = 0;
spinnerMotorSpeed = .8; //.85 max to get 60rpm with a 25:1 gear ratio
//Color Match Variables
  originalV3Color = "";
  gameData = "";
  gameDataTimer = new Timer();
  gameDataTimeOut = 3;

//Joysticks
  leftStick = new Joystick(0);
  rightStick = new Joystick(1);
  weaponStick = new Joystick(2);

//Drive Systems
  chewbreakaDrive = new DifferentialDrive(leftFrontDrive , rightFrontDrive); 
  transportDrive = new DifferentialDrive(frontBallTransport, backBallTransport);
  shooterDrive = new DifferentialDrive(leftShooter, rightShooter);
  climbDrive = new DifferentialDrive(climbTopMotor, climbBottomMotor);

// Drive Variables
leftDriveSpeed = 0;
rightDriveSpeed = 0;
leftDriveCoef   = 1;
rightDriveCoef  = -1;
triggerDeadzone = .1;
driveTopSpeed   = 1;
slideMotorSpeed = 0;
slideMotorCoef  = 1;
turnSpeed = .5;
driveSpeed = .5;

//Acceleration Limiting Variables
 accelerationLimiting = true;
accelLimitedLeftGetY = 0;
accelLimitedRightGetY = 0;
accelLimitedSlideDrive = 0;
accelDriveKonstant = 4; // Change from 2-32. 32 is super slow to react, 2 is little improvement
accelSlideKonstant = 10;
// Shooting Variables
rightShooterSpeed = 0;
leftShooterSpeed = 0;

kPShoot = .4;
kIShoot = .0009;
kDShoot = 0;
kFShoot = 1023/7200;
currentRPM = 0;
motorRPM = 900;//Use this variable to change your Rpm(0-1873 for the Rs775 Pro with a 10:1 gearbox) on the fly
encoderCounts = 4096;
minute = 600;
 runShooter = false;
/**
* The target velocity is calculated by taking the desired speed (in RPM) 
* and multiplying it by the number of encoder counts per revolution (in this case, 4096)
* you then divide by 600 to convert from minutes to milliseconds
* This function is called every 100ms, so 100 * 600 = 60000
* 60000ms/1000 = 60 seconds = 1 minute
* In this case, with a 10:1 gearbox on the Rs775 Pros, I chose to go for 900RPM (a middle ground value)
* (900RPM * 4096 encoder counts) / 600ms = 6144 encoder count units per 100ms
*/
targetVelocity_UnitsPer100ms = 6144; //900RPM * 4096 encoder counts / 600;

//PID
kPTurn = .04;
kITurn = .0004;
kDTurn = 0;
runDriveTrain = false;
integral =0;
error = 0;
rcw = 0;
derivative = 0;
previous_error = 0;
setpoint = 0;
 velocityControl = false;
  }
}

