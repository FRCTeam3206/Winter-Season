/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot;

import java.lang.Math;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.math.filter.SlewRateLimiter;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution.ModuleType;

import edu.wpi.first.wpilibj.util.Color;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADIS16448_IMU;

/**
 * Button Mappings ----Left Joystick(Logitech Attack 3)----
 * None-------------------------------------------- ----Right Joystick(Logitech
 * Extreme 3D)---- 1-Low Gear 2-High Gear ----Xbox One Controller(Ian) 1-Low
 * Gear 2-High Gear ----Weapons Joystick(Logitech Dual Action)----
 * X(1)-Transport Up A(2)-Intake Piston/Run Toggle B(3)-Transport Down
 * Y(4)-Shooter On & Transport Up Toggle LB(5)-Engage Climb Ratchet Toggle
 * RB(6)-Shoot From One Bot Away & Transport Toggle LT(7)-Reverse Climb
 * RT(8)-Climb START(10)-Compressor Toggle Left Stick Click(11)-Control Panel
 * Rotation Control Right Stick Click(12)-Control Panel Position Control
 */
public class Robot extends TimedRobot {
  // Drive Type
  boolean XboxDrive = false;
  boolean arcadeDrive=true;
  // Joysticks
  Joystick leftStick;
  Joystick rightStick;
  XboxController driveStick;
  XboxController weaponStick;
  // Sendable Chooser
  SendableChooser<String> autoChoices = new SendableChooser<>();
  String autoSelected;
  // DriveTrain
  DifferentialDrive chewbreakaDrive;
  // Drivetrain Motors
  CANSparkMax leftFrontDrive;
  CANSparkMax rightFrontDrive;
  CANSparkMax leftBackDrive;
  CANSparkMax rightBackDrive;
  // DriveTrain Pneumatics
  DoubleSolenoid driveSol = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 0, 1);
  PowerDistribution pdp = new PowerDistribution(0, ModuleType.kCTRE);
  Compressor compressor = new Compressor(0, PneumaticsModuleType.CTREPCM);
  // DriveTrain Encoders and Gyro
  RelativeEncoder leftEncoder;
  RelativeEncoder rightEncoder;
  ADIS16448_IMU imu = new ADIS16448_IMU();
  // ADIS and Drive Variables
  double GyroAngle;
  double GyroCoefficient = 1.0;
  double DesiredAngle;
  String DesiredDirection;
  double desiredDistance;
  double distanceTraveled;
  double tLateDrive = 18;//longest time it takes to drive for any function
  double tLateTurn = 15;//longest time it takes to drive for any function
  // Compressor Toggle Variables
  int compressorToggle = 0;
  boolean compressorToggleStick;
  // Acceleration Limiting Variables
  boolean accelerationLimiting = true;
  double accelLimitedLeftGetY;
  double accelLimitedRightGetY;
  double accelLimitedSlideDrive;
  double accelDriveKonstant = 6; // Change from 2-32. 32 is super slow to react, 2 is little improvement
  double leftDriveCoef = .7;
  double rightDriveCoef = .7;
  double rightStickDeadband = .1;
  double leftStickDeadband = .1;
  double leftAdjusted;
  double rightAdjusted;

  // Intake
  WPI_VictorSPX intakeMotor = new WPI_VictorSPX(5);
  DoubleSolenoid intakeSol = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 3, 4);
  Timer intakeTimer = new Timer();
  // Intake Toggle Variables
  int intakeToggle = 0;
  boolean intakeToggleStick;
  boolean extakeToggleStick;
  int extakeToggle = 0;
  // Motor Speed
  double intakeSpeed = .65;
  // Climb
  WPI_VictorSPX climbFrontMotor = new WPI_VictorSPX(6);
  WPI_VictorSPX climbBackMotor = new WPI_VictorSPX(7);
  DoubleSolenoid climbSol = new DoubleSolenoid(0, PneumaticsModuleType.CTREPCM, 2, 5);
  // Climb Ratchet Toggle Variables
  int ratchetToggle = 0;
  boolean ratchetToggleStick;
  // Motor Speed
  double climbSpeed = .5;

  // Shooter
  WPI_TalonSRX leftShooter = new WPI_TalonSRX(8);
  WPI_TalonSRX rightShooter = new WPI_TalonSRX(9);
  int shooterToggle = 0;
  boolean shooterToggleStick;
  Timer velocityTimer = new Timer();
  boolean shooter2ToggleStick;
  int shooter2Toggle = 0;
  // Ball Transport
  WPI_TalonSRX frontBallTransport = new WPI_TalonSRX(10);
  WPI_TalonSRX backBallTransport = new WPI_TalonSRX(11);
  DigitalInput ballSwitch = new DigitalInput(3);
  // Variables
  double currentPos = 0;
  double startPos = 0;
  double targetPos = 0;
  double rpmSetpoint = 1000;
  double ballMagScale = .00110390625;// (1/4096 * 3.14 * 1.972) scaled to inches instead
  double ballDesiredDistance;
  double ballDistanceTraveled = 0;
  double frontBallSpeed = -1;
  double backBallSpeed = -1;

  // Control Panel
  WPI_TalonSRX WheelSpinner = new WPI_TalonSRX(12);
  /*
   * //Color Sensor I2C.Port i2cPort = I2C.Port.kOnboard; ColorSensorV3
   * m_colorSensor = new ColorSensorV3(i2cPort); ColorMatch m_colorMatcher = new
   * ColorMatch();
   */
  // Colors
  Color kBlueTarget = new Color(0.143, 0.427, 0.429);
  Color kGreenTarget = new Color(0.197, 0.561, 0.240);
  Color kRedTarget = new Color(0.561, 0.232, 0.114);
  Color kYellowTarget = new Color(0.361, 0.524, 0.113);
  // Spin Function Variables
  double spinnerMagScale = .0001259781901041667; // ((1/4096 * 3.14 * 1.972)/12) scaling factor to convert the raw
                                                 // encoder value to distance traveled
  double spinnerDesiredDistance;
  double spinnerDistanceTraveled = 0;
  double spinnerMotorSpeed = 1; // .85 max to get 60rpm with a 25:1 gear ratio
  // Color Match Variables
  String originalV3Color;
  String gameData = "";
  Timer gameDataTimer = new Timer();
  double gameDataTimeOut = 3;

  // Drive Variables
  double leftDriveSpeed;
  double rightDriveSpeed;
  double triggerDeadzone = .1;
  int desiredDirection;
  double powerNumber = 3;
  double primeConstant = .8; // This needs to be between 0 and 1. 0 means that the drive train is just x^3
                             // and 1 means that it is just x. 1 is more sensitive and 0 is less sensitive.
  double rightPrime;
  double leftPrime;

  // Shooting Variables
  double kPShoot = .4;
  double kIShoot = .0009;
  double kDShoot = 0;
  double kFShoot = .0005;
  double motorRPM = 980;// Use this variable to change your Rpm(0-1873 for the Rs775 Pro with a 10:1
                        // gearbox) on the fly
  double encoderCounts = 4096;
  double minute = 600;
  boolean runShooter = false;
  // PID
  double kPTurn = .04;
  double kITurn = .0004;
  double kDTurn = 0;
  boolean runDriveTrain = false;
  double integral, previous_error, setpoint = 0;
  double error;
  double derivative;
  double rcw;
  // Lights
  Alliance LED_pattern;
  int RGB_red = 0; // 0-255
  int RGB_green = 0; // 0-255
  int RGB_blue = 0; // 0-255
  int HSV_hue = 0; // 0-180
  int HSV_sat = 0; // 0-255
  int HSV_val = 0; // 0-255
  private AddressableLED m_ledLeft;
  private AddressableLEDBuffer m_ledBufferLeft;

  /**
   * The target velocity is calculated by taking the desired speed (in RPM) and
   * multiplying it by the number of encoder counts per revolution (in this case,
   * 4096) you then divide by 600 to convert from minutes to milliseconds This
   * function is called every 100ms, so 100 * 600 = 60000 60000ms/1000 = 60
   * seconds = 1 minute In this case, with a 10:1 gearbox on the Rs775 Pros, I
   * chose to go for 900RPM (a middle ground value) (900RPM * 4096 encoder counts)
   * / 600ms = 6144 encoder count units per 100ms
   */
  double targetVelocity = 6500;
  double oneBotVelocity = 6500; // 980RPM * 4096 encoder counts / 600;
  // This onebotvelocity isn't working and is changing the Y speed.
  double autoTargetVelocity = 6400;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    leftFrontDrive = new CANSparkMax(1, MotorType.kBrushless);
    rightFrontDrive = new CANSparkMax(3, MotorType.kBrushless);
    leftBackDrive = new CANSparkMax(2, MotorType.kBrushless);
    rightBackDrive = new CANSparkMax(4, MotorType.kBrushless);
    leftEncoder = leftFrontDrive.getEncoder();
    rightEncoder = rightFrontDrive.getEncoder();

    leftFrontDrive.restoreFactoryDefaults();
    rightFrontDrive.restoreFactoryDefaults();
    leftBackDrive.restoreFactoryDefaults();
    rightBackDrive.restoreFactoryDefaults();
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);

    leftBackDrive.follow(leftFrontDrive);
    rightBackDrive.follow(rightFrontDrive);

    chewbreakaDrive = new DifferentialDrive(leftFrontDrive, rightFrontDrive);

    if (XboxDrive == false) {
      leftStick = new Joystick(0);
      if(!arcadeDrive)
      rightStick = new Joystick(1);
      weaponStick = new XboxController(2);
    } else {
      driveStick = new XboxController(0);
      weaponStick = new XboxController(2);
    }

    m_ledLeft = new AddressableLED(0);
    m_ledBufferLeft = new AddressableLEDBuffer(64);
    m_ledLeft.setLength(m_ledBufferLeft.getLength());
    LED_pattern = DriverStation.getAlliance();
    compressorToggleStick = weaponStick.getStartButtonPressed();
    ratchetToggleStick = weaponStick.getLeftBumperPressed();
    shooterToggleStick = weaponStick.getYButtonPressed();
    intakeToggleStick = weaponStick.getRawButton(2);
    shooter2ToggleStick = weaponStick.getLeftBumperPressed();
    extakeToggleStick = weaponStick.getRawButton(3);
    pdp.clearStickyFaults();

    rightShooter.setInverted(true);
    rightShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    rightShooter.configNominalOutputForward(0, 30);
    rightShooter.configNominalOutputReverse(0, 30);
    rightShooter.configPeakOutputForward(1, 30);
    rightShooter.configPeakOutputReverse(-1, 30);

    leftShooter.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    leftShooter.configNominalOutputForward(0, 30);
    leftShooter.configNominalOutputReverse(0, 30);
    leftShooter.configPeakOutputForward(1, 30);
    leftShooter.configPeakOutputReverse(-1, 30);

    /*
     * m_colorMatcher.addColorMatch(kBlueTarget);
     * m_colorMatcher.addColorMatch(kGreenTarget);
     * m_colorMatcher.addColorMatch(kRedTarget);
     * m_colorMatcher.addColorMatch(kYellowTarget);
     * WheelSpinner.configSelectedFeedbackSensor(FeedbackDevice.
     * CTRE_MagEncoder_Relative, 0, 30);
     */
    gameDataTimer.reset();
    velocityTimer.reset();
    gameDataTimer.start();
    // SmartDashboard.putNumber("Spinner Speed", spinnerMotorSpeed);
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

    UsbCamera goalCam = CameraServer.startAutomaticCapture("goalCam", 1);
    goalCam.setFPS(30);
    UsbCamera groundCam = CameraServer.startAutomaticCapture("groundCam", 0);
    groundCam.setFPS(10);

    WheelSpinner.setSelectedSensorPosition(0);

    autoChoices.setDefaultOption("AutoLine", "AutoLine");
    autoChoices.addOption("Center Shot", "Center Shot");
    autoChoices.addOption("Left Shot", "Left Shot");
    autoChoices.addOption("Right Shot", "Right Shot");
    autoChoices.addOption("4Auto", "4Auto");
    SmartDashboard.putData("Auto Routines", autoChoices);
    SmartDashboard.putNumber("Spinner Feet", WheelSpinner.getSelectedSensorPosition() * spinnerMagScale);
    if (LED_pattern.equals(Alliance.Blue)) {
      blueAllianceZoom();
    } else {
      redAllianceZoom();
    }
    leftEncoder.setPosition(0);
    imu.reset();
  }

  private void redAllianceZoom() {
    int ledOffset = 0; // make offset for brigher led

    // for loop that contians everything
    for (int i = 0; i < m_ledBufferLeft.getLength(); i++) {

      // increment offset by 1 more than loop #
      ledOffset = i + 1;
      if (ledOffset >= m_ledBufferLeft.getLength()) {
        ledOffset = ledOffset - m_ledBufferLeft.getLength();
      } // keep ledOffset in array bounds
      m_ledBufferLeft.setRGB(i, 20, 0, 0); // set all colors other than offset to dim red
      m_ledBufferLeft.setRGB(ledOffset, 255, 0, 0); // set offset to bright red
      m_ledLeft.setData(m_ledBufferLeft);
      m_ledLeft.start();
      Timer.delay(.01); // wait 1 hundreth of a second before looping again
    }
  }

  private void blueAllianceZoom() {
    int ledOffset = 0; // make offset for brigher led

    // for loop that contians everything
    for (int i = 0; i < m_ledBufferLeft.getLength(); i++) {

      // increment offset by 1 more than loop #
      ledOffset = i + 1;
      if (ledOffset >= m_ledBufferLeft.getLength()) {
        ledOffset = ledOffset - m_ledBufferLeft.getLength();
      } // keep ledOffset in array bounds
      m_ledBufferLeft.setRGB(i, 0, 0, 20); // set all colors other than offset to dim red
      m_ledBufferLeft.setRGB(ledOffset, 0, 0, 255); // set offset to bright red
      m_ledLeft.setData(m_ledBufferLeft);
      m_ledLeft.start();
      Timer.delay(.01); // wait 1 hundreth of a second before looping again
    }
  }

  public void Turn(double Angle) {
    GyroAngle = GyroCoefficient * imu.getGyroAngleZ();
    DesiredAngle = Angle + GyroAngle;
    velocityTimer.start();
    TurnLabel: if (Angle < 0) {
      while (GyroAngle >= DesiredAngle) {
        GyroAngle = GyroCoefficient * imu.getGyroAngleZ();
        chewbreakaDrive.tankDrive(.4, -.4);
        if (velocityTimer.get() >= tLateTurn) {
          break TurnLabel;
        }
      }
    } else if (Angle > 0) {
      while (GyroAngle <= DesiredAngle) {
        GyroAngle = GyroCoefficient * imu.getGyroAngleZ();
        chewbreakaDrive.tankDrive(-.4, .4);
        if (velocityTimer.get() >= tLateTurn) {
          break TurnLabel;
        }
      }
    } else {
      chewbreakaDrive.arcadeDrive(0, 0);
      System.out.println("turned " + Angle);
    }
    chewbreakaDrive.arcadeDrive(0, 0);
  }

  public void Drive(double distance) {
    distanceTraveled = leftEncoder.getPosition() * -1 / 12;
    desiredDistance = distance + distanceTraveled;
    velocityTimer.start();
    DriveLabel: if (distance > 0) {
      while (desiredDistance > distanceTraveled) {
        distanceTraveled = leftEncoder.getPosition() * -1 / 12;
        chewbreakaDrive.tankDrive(-.6, -.6);
        if (velocityTimer.get() >= tLateDrive) {
          break DriveLabel;
        }
      }
    } else if (distance < 0) {
      while (desiredDistance < distanceTraveled) {
        distanceTraveled = leftEncoder.getPosition() * -1 / 12;
        chewbreakaDrive.tankDrive(.7, .7);
        if (velocityTimer.get() >= tLateDrive) {
          break DriveLabel;
        }
      }
    } else {
      chewbreakaDrive.tankDrive(0, 0);
    }
    chewbreakaDrive.tankDrive(0, 0);
  }
  /*
   * public void Spin (double distance) {
   * WheelSpinner.setSelectedSensorPosition(0); spinnerDistanceTraveled = 0;
   * spinnerDesiredDistance = distance; while (spinnerDesiredDistance >
   * spinnerDistanceTraveled) { spinnerDistanceTraveled =
   * WheelSpinner.getSelectedSensorPosition() * spinnerMagScale;
   * WheelSpinner.set(ControlMode.PercentOutput, -spinnerMotorSpeed);
   * SmartDashboard.putNumber("Distance Traveled", spinnerDistanceTraveled);
   * leftPrime = primeConstant * driveStick.getLeftY() + (1-primeConstant) *
   * Math.pow(driveStick.getLeftY(), 3); //The formula that this is making
   * is rightPrime = primeConstant * driveStick.getRightY() +
   * (1-primeConstant) * Math.pow(driveStick.getRightY(), 3);
   * 
   * if(accelerationLimiting == true){ accelLimitedLeftGetY = leftPrime /
   * accelDriveKonstant + accelLimitedLeftGetY * (accelDriveKonstant - 1) /
   * accelDriveKonstant; accelLimitedRightGetY = rightPrime / accelDriveKonstant +
   * accelLimitedRightGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
   * chewbreakaDrive.tankDrive(accelLimitedLeftGetY * leftDriveCoef,
   * accelLimitedRightGetY * rightDriveCoef); } }
   * WheelSpinner.set(ControlMode.PercentOutput, 0); }ds
   */

  public void Intake(double distance) {
    frontBallTransport.setSelectedSensorPosition(0);
    backBallTransport.setSelectedSensorPosition(0);
    ballDistanceTraveled = 0;
    ballDesiredDistance = distance;
    while (ballDesiredDistance > ballDistanceTraveled) {
      ballDistanceTraveled = backBallTransport.getSelectedSensorPosition() * ballMagScale;
      frontBallTransport.set(ControlMode.PercentOutput, -.4);
      backBallTransport.set(ControlMode.PercentOutput, -.4);
    }
    frontBallTransport.set(ControlMode.PercentOutput, 0);
    backBallTransport.set(ControlMode.PercentOutput, 0);
  }

  // for auto only
  public void Shoot() {
    velocityTimer.start();
    while (velocityTimer.get() <= 4) {
      rightShooter.set(ControlMode.Velocity, autoTargetVelocity);
      leftShooter.set(ControlMode.Velocity, autoTargetVelocity);
      if (velocityTimer.get() >= 1.5) {
        frontBallTransport.set(ControlMode.PercentOutput, -.9);
        backBallTransport.set(ControlMode.PercentOutput, -.9);
      }
    }
    rightShooter.stopMotor();
    leftShooter.stopMotor();
    frontBallTransport.stopMotor();
    backBallTransport.stopMotor();
  }
  /*
   * public void CenterPanelPosition (String V3Color) {
   * WheelSpinner.setSelectedSensorPosition(0); spinnerDistanceTraveled = 0;
   * originalV3Color = V3Color; while (originalV3Color == V3Color) {
   * spinnerDistanceTraveled = WheelSpinner.getSelectedSensorPosition() *
   * spinnerMagScale; Color detectedColor = m_colorSensor.getColor();
   * ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor); if
   * (match.color == kBlueTarget) {V3Color = "Blue"; } else if (match.color ==
   * kRedTarget) {V3Color = "Red"; } else if (match.color == kGreenTarget)
   * {V3Color = "Green"; } else if (match.color == kYellowTarget) {V3Color =
   * "Yellow"; } else {V3Color = "Unknown"; }
   * WheelSpinner.set(ControlMode.PercentOutput, 0.2); //spin wheel until color
   * changes SmartDashboard.putNumber("Distance Traveled",
   * spinnerDistanceTraveled); } spinnerDesiredDistance = 1.042/2;// center of a
   * color segment spinnerDistanceTraveled = 0; while (spinnerDesiredDistance >
   * spinnerDistanceTraveled) { spinnerDistanceTraveled =
   * WheelSpinner.getSelectedSensorPosition() * spinnerMagScale;
   * WheelSpinner.set(ControlMode.PercentOutput, -0.2);
   * SmartDashboard.putNumber("Distance Traveled", spinnerDistanceTraveled); }
   * WheelSpinner.set(ControlMode.PercentOutput, 0); }
   */

  /*
   * public void 4Auto() { intakeSol.set(Value.kReverse);
   * intakeMotor.set(ControlMode.PercentOutput, intakeSpeed); Drive(11.7);
   * intakeSol.set(Value.kForward); intakeMotor.set(ControlMode.PercentOutput, 0);
   * Drive(-11.7); Turn(155); Drive(9.5); Shoot(); }
   */
  public void CenterShot() {
    Drive(9.8);
    Shoot();
    Drive(-15);
    Turn(135);
  }

  public void LeftShot() {
    Turn(45);
    Drive(8.5);
    Turn(-45);
    Drive(3);
    Shoot();
    Drive(-3);
    Turn(45);
    Drive(-10);
    // Turn(135);
  }

  public void RightShot() {
    Turn(-45);
    Drive(8.5);
    Turn(42);
    Drive(3);
    Shoot();
    Drive(-3);
    Turn(-25);
    Drive(-10);
    // Turn(-130);
  }

  @Override
  public void robotPeriodic() {
    pdp.clearStickyFaults();
    leftBackDrive.clearFaults();
    rightBackDrive.clearFaults();
    leftFrontDrive.clearFaults();
    rightFrontDrive.clearFaults();

  }

  @Override
  public void autonomousInit() {
    autoSelected = autoChoices.getSelected();

    compressor.enableDigital();
    driveSol.set(Value.kReverse);
    switch (autoSelected) {
    case "AutoLine":
      Drive(5);
      break;
    case "Center Shot":
      CenterShot();
      break;
    case "Left Shot":
      LeftShot();
      break;
    case "Right Shot":
      RightShot();
      break;
    case "4Auto":
      // 4Auto();
      break;
    default:
      Drive(5);
      break;
    }
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // compressor.enableDigital();
    compressor.disable();
  }
  private double applyDeadzone(double input, double zone){
    if(input<zone&&input>-zone)return 0;
    return input;
  }

  SlewRateLimiter forwardFilter = new SlewRateLimiter(3.0);

  @Override
  public void teleopPeriodic() {
    /*
     * //Control Panel Color detectedColor = m_colorSensor.getColor(); String
     * colorString; ColorMatchResult match =
     * m_colorMatcher.matchClosestColor(detectedColor); gameData =
     * DriverStation.getGameSpecificMessage(); if (match.color ==
     * kBlueTarget) {colorString = "Blue"; } else if (match.color == kRedTarget)
     * {colorString = "Red"; } else if (match.color == kGreenTarget) {colorString =
     * "Green"; } else if (match.color == kYellowTarget) {colorString = "Yellow"; }
     * else {colorString = "Unknown"; } SmartDashboard.putString("Detected Color",
     * colorString); SmartDashboard.putString("FMS Color", gameData);
     */
    // Acceleration Limiting
    double right,left=0;
    double forward = 0, turn = 0;

    if(true) {
      forward = -0.8 * applyDeadzone(leftStick.getZ(),0);
      turn = 0.8 * applyDeadzone(leftStick.getY(),0);
      chewbreakaDrive.arcadeDrive(forward, forwardFilter.calculate(turn));
    } else {
    if(arcadeDrive){
      if(XboxDrive){
        right=driveStick.getLeftY()-driveStick.getLeftX();
        left=driveStick.getLeftY()+driveStick.getLeftX();
      }else{
        right=applyDeadzone(leftStick.getY(),0)+applyDeadzone(leftStick.getZ()*.5,0);
        left=applyDeadzone(leftStick.getY(),0)-applyDeadzone(leftStick.getZ()*.5,0);
        SmartDashboard.putNumber("Right",right);
        SmartDashboard.putNumber("Left",left);
      }
    }else{
      if(XboxDrive){
        right=driveStick.getRightY();
        left=driveStick.getLeftY();
      }else{
        right=rightStick.getY();
        left=leftStick.getY();
      }
    }
    leftPrime = left;
    rightPrime = -right;
    // leftPrime = primeConstant * left
    //     + (1 - primeConstant) * Math.pow(left, 3); // The formula that this is making is
    // rightPrime = -(primeConstant * right
    //     + (1 - primeConstant) * Math.pow(right, 3));

    if (accelerationLimiting == true) {
      accelLimitedLeftGetY = leftPrime / accelDriveKonstant
          + accelLimitedLeftGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      accelLimitedRightGetY = rightPrime / accelDriveKonstant
          + accelLimitedRightGetY * (accelDriveKonstant - 1) / accelDriveKonstant;
      chewbreakaDrive.tankDrive(accelLimitedLeftGetY * leftDriveCoef, accelLimitedRightGetY * rightDriveCoef);
    } else {
      chewbreakaDrive.tankDrive(driveStick.getLeftY() * leftDriveCoef,
          driveStick.getRightY() * rightDriveCoef); // No acceleration limiting.
    }
  }

    // Right Joystick
    if (XboxDrive == false) {
      if (leftStick.getRawButton(1)) { // Low Speed
        driveSol.set(Value.kForward);
      } else if (leftStick.getRawButton(2)) { // High Speed
        driveSol.set(Value.kReverse);
      } else {
        driveSol.set(Value.kOff); // Ensures Pistons are Off
      }
    } else {
      if (driveStick.getRightBumper()) { // High Speed
        driveSol.set(Value.kReverse);
      } else if (driveStick.getLeftBumper()) { // Low Speed
        driveSol.set(Value.kForward);
      } else {
        driveSol.set(Value.kOff); // Ensures Pistons are Off
      }
    }
    // Weapons Joystick
    // Ball Transport
    ballSwitch.get();
    if (ballSwitch.get() == false && weaponStick.getRawButton(3) == false) {
      Intake(6.2);
    }

    // basic way to run the transport
    if (weaponStick.getRawButton(1)) {// Up
      frontBallTransport.set(ControlMode.PercentOutput, -.7);
      backBallTransport.set(ControlMode.PercentOutput, -.7);
    } 
        else if (weaponStick.getRawButton(3)) {
        frontBallTransport.set(ControlMode.PercentOutput, .7);
        backBallTransport.set(ControlMode.PercentOutput, .7);
        intakeSol.set(Value.kReverse); Timer.delay(.5); if (intakeSol.get() ==
        Value.kReverse && weaponStick.getRawButton(3) == true) {
        intakeMotor.set(ControlMode.PercentOutput, -intakeSpeed); } }
        else
        {// Off
      frontBallTransport.stopMotor();
      backBallTransport.stopMotor();
      intakeMotor.stopMotor();
      intakeSol.set(Value.kForward);
    }

    // Intake Piston/Run Toggle
    intakeToggleStick = weaponStick.getRawButtonPressed(2);
    // Set Button to Integer Value
    if (intakeToggleStick == true && intakeToggle == 0) { // First Press
      intakeToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                        // toggle = 1
    } else if (intakeToggleStick == false && intakeToggle == 1) { // First Release
      intakeToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (intakeToggleStick == true && intakeToggle == 2) { // Second Press
      intakeToggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (intakeToggleStick == false && intakeToggle == 3) { // Second Release
      intakeToggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (intakeToggleStick == false && intakeToggle == 0) { // Completes the Cycle/Redundancy Backup
      intakeToggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }

    // Determine Piston Position Based on Integer Value
    if (intakeToggle == 1 || intakeToggle == 2) { // Trigger is Pressed
      intakeSol.set(Value.kReverse);
      intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
    } else if (intakeToggle == 3 || intakeToggle == 0) { // Trigger is Released
    }
    if (weaponStick.getRightBumper()) {
      intakeMotor.set(ControlMode.PercentOutput, -1);
      intakeSol.set(Value.kReverse);
    }

    // Tentative Backdrive Toggle Function if other mthods does not work
    extakeToggleStick = weaponStick.getRawButtonPressed(3);
    // Set Button to Integer Value
    if (extakeToggleStick == true && extakeToggle == 0) { // First Press
      extakeToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                        // toggle = 1
    } else if (extakeToggleStick == false && extakeToggle == 1) { // First Release
      extakeToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (extakeToggleStick == true && extakeToggle == 2) { // Second Press
      extakeToggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (extakeToggleStick == false && extakeToggle == 3) { // Second Release
      extakeToggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (extakeToggleStick == false && extakeToggle == 0) { // Completes the Cycle/Redundancy Backup
      extakeToggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }
    // Determine Piston Position Based on Integer Value
    if (extakeToggle == 1) {
      velocityTimer.start();
    } else if (extakeToggle == 1 || extakeToggle == 2) { // Trigger is Pressed
      intakeSol.set(Value.kReverse);
      frontBallTransport.set(ControlMode.PercentOutput, -frontBallSpeed);
      backBallTransport.set(ControlMode.PercentOutput, -backBallSpeed);
      if (velocityTimer.get() >= 2) {
        intakeMotor.set(ControlMode.PercentOutput, -intakeSpeed);
      }
    } else if (extakeToggle == 3 || extakeToggle == 0) { // Trigger is Released

    }

    // Shooter & Transport Toggle
    shooterToggleStick = weaponStick.getYButtonPressed();
    // Set Button to Integer Value
    if (shooterToggleStick == true && shooterToggle == 0) { // First Press
      shooterToggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                         // toggle = 1
    } else if (shooterToggleStick == false && shooterToggle == 1) { // First Release
      shooterToggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (shooterToggleStick == true && shooterToggle == 2) { // Second Press
      shooterToggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (shooterToggleStick == false && shooterToggle == 3) { // Second Release
      shooterToggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (shooterToggleStick == false && shooterToggle == 0) { // Completes the Cycle/Redundancy Backup
      shooterToggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }

    // Determine Piston Position Based on Integer Value
    if (shooterToggle == 1) {// makes sure the timer only starts once per cycle
      velocityTimer.start();
    } else if (shooterToggle == 1 || shooterToggle == 2) { // 1st Toggle
      rightShooter.set(ControlMode.Velocity, targetVelocity);
      leftShooter.set(ControlMode.Velocity, targetVelocity);
      if (velocityTimer.get() >= 1.5) {
        frontBallTransport.set(ControlMode.PercentOutput, frontBallSpeed);
        backBallTransport.set(ControlMode.PercentOutput, backBallSpeed);
      }
    } else if (shooterToggle == 3 || shooterToggle == 0) { // 2nd Toggle
        rightShooter.stopMotor();
        leftShooter.stopMotor();
        velocityTimer.reset();
    }

    // Shoot from one bot
    // away----------------------------------------------------------
    shooter2ToggleStick = weaponStick.getLeftBumperPressed();
    // Set Button to Integer Value
    if (shooter2ToggleStick == true && shooter2Toggle == 0) { // First Press
      shooter2Toggle = 1; // If trigger is pressed and toggle hasn't been set yet/has cycled through then
                          // toggle = 1
    } else if (shooter2ToggleStick == false && shooter2Toggle == 1) { // First Release
      shooter2Toggle = 2; // If trigger is released and toggle = 1 then toggle = 2
    } else if (shooter2ToggleStick == true && shooter2Toggle == 2) { // Second Press
      shooter2Toggle = 3; // If trigger is pressed and toggle = 2 then toggle = 3
    } else if (shooter2ToggleStick == false && shooter2Toggle == 3) { // Second Release
      shooter2Toggle = 0; // If trigger is released and toggle = 3 then toggle = 0
    } else if (shooter2ToggleStick == false && shooter2Toggle == 0) { // Completes the Cycle/Redundancy Backup
      shooter2Toggle = 0; // If trigger is released and toggle = 0 then toggle = 0
    }

    // Determine Piston Position Based on Integer Value
    if (shooter2Toggle == 1) {// makes sure the timer only starts once per cycle
      velocityTimer.start();
    } else if (shooter2Toggle == 1 || shooter2Toggle == 2) { // 1st Toggle
      rightShooter.set(ControlMode.Velocity, oneBotVelocity);
      leftShooter.set(ControlMode.Velocity, oneBotVelocity);
      if (velocityTimer.get() >= 1.5) {
        frontBallTransport.set(ControlMode.PercentOutput, frontBallSpeed);
        backBallTransport.set(ControlMode.PercentOutput, backBallSpeed);
      }
    } else if (shooterToggle == 0 && shooter2Toggle == 0) { // 2nd Toggle
      rightShooter.stopMotor();
      leftShooter.stopMotor();
    }

    // Climb Ratchet
    if (weaponStick.getRawButtonPressed(10)) {
      climbSol.set(Value.kReverse);
    } else if (weaponStick.getRawButtonPressed(9)) {
      climbSol.set(Value.kForward);
    }
    // Climb
    if (weaponStick.getRawButton(7) && climbSol.get() == Value.kForward) {// Reverse Climb
      climbFrontMotor.set(ControlMode.PercentOutput, 1);
      climbBackMotor.set(ControlMode.PercentOutput, 1);
    } else if (weaponStick.getRawButton(8)) {// Climb
      climbFrontMotor.set(ControlMode.PercentOutput, -climbSpeed);
      climbBackMotor.set(ControlMode.PercentOutput, -climbSpeed);
    } else {
      climbFrontMotor.set(ControlMode.PercentOutput, 0);
      climbBackMotor.set(ControlMode.PercentOutput, 0);
    }

    // Control Panel
    if (weaponStick.getRawButton(11)) {// Rotation Control
      // Spin(27);//a bit over the 3 full rotations needed
      WheelSpinner.set(ControlMode.PercentOutput, 1);
      SmartDashboard.putNumber("Spinner Feet", WheelSpinner.getSelectedSensorPosition() * spinnerMagScale);
    } else {
      WheelSpinner.set(ControlMode.PercentOutput, 0);
    }
    if (weaponStick.getRawButton(12)) {
      compressor.enableDigital();
    } /*
       * else if (weaponStick.getRawButton(12) && gameData.length() > 0) {//Position
       * Control CenterPanelPosition(colorString);//center the sensor on the panel
       * segment of the current color switch (gameData.charAt(0)){ case 'B' : //Blue
       * case code if(colorString == "Green"){Spin(1.042); } else if(colorString ==
       * "Blue"){Spin(2.083); } else if(colorString == "Yellow"){Spin(3.125); } else
       * {Spin(0);} break; case 'G' : //Green case code if(colorString ==
       * "Red"){Spin(1.042); } else if(colorString == "Green"){Spin(2.083); } else
       * if(colorString == "Blue"){Spin(3.125); } else {Spin(0);} break; case 'R' :
       * //Red case code if(colorString == "Yellow"){Spin(1.042); } else
       * if(colorString == "Red"){Spin(2.083); } else if(colorString ==
       * "Green"){Spin(3.125); } else {Spin(0);} break; case 'Y' : //Yellow case code
       * if(colorString == "Blue"){Spin(1.042); } else if(colorString ==
       * "Yellow"){Spin(2.083); } else if(colorString == "Red"){Spin(3.125); } else
       * {Spin(0);} break; default : //This is corrupt data break; } }else {//stop
       * WheelSpinner.set(ControlMode.PercentOutput, 0); }
       */

  }

  @Override
  public void testPeriodic() {
    /*
     * //looking for color position code from game data FMS Color detectedColor =
     * m_colorSensor.getColor(); String colorString; ColorMatchResult match =
     * m_colorMatcher.matchClosestColor(detectedColor); if (match.color ==
     * kBlueTarget) {colorString = "Blue"; } else if (match.color == kRedTarget)
     * {colorString = "Red"; } else if (match.color == kGreenTarget) {colorString =
     * "Green"; } else if (match.color == kYellowTarget) {colorString = "Yellow"; }
     * else {colorString = "Unknown"; } SmartDashboard.putNumber("Red",
     * detectedColor.red);//tune the RGB values from the colorMatch
     * SmartDashboard.putNumber("Green", detectedColor.green);
     * SmartDashboard.putNumber("Blue", detectedColor.blue);
     * SmartDashboard.putNumber("Confidence", match.confidence);
     * SmartDashboard.putString("Detected Color", colorString);
     * SmartDashboard.putString("FMS Color", gameData);
     * 
     * gameData = DriverStation.getGameSpecificMessage();
     * if(leftStick.getRawButton(3) && gameData.length() > 0){
     * CenterPanelPosition(colorString);//center the sensor on the panel segment of
     * the current color switch (gameData.charAt(0)){ case 'B' : //Blue case code
     * if(colorString == "Green"){Spin(1.042); } else if(colorString ==
     * "Blue"){Spin(2.083); } else if(colorString == "Yellow"){Spin(3.125); } else
     * {Spin(0);} break; case 'G' : //Green case code if(colorString ==
     * "Red"){Spin(1.042); } else if(colorString == "Green"){Spin(2.083); } else
     * if(colorString == "Blue"){Spin(3.125); } else {Spin(0);} break; case 'R' :
     * //Red case code if(colorString == "Yellow"){Spin(1.042); } else
     * if(colorString == "Red"){Spin(2.083); } else if(colorString ==
     * "Green"){Spin(3.125); } else {Spin(0);} break; case 'Y' : //Yellow case code
     * if(colorString == "Blue"){Spin(1.042); } else if(colorString ==
     * "Yellow"){Spin(2.083); } else if(colorString == "Red"){Spin(3.125); } else
     * {Spin(0);} break; default : //This is corrupt data break; } }
     */
  }

}
