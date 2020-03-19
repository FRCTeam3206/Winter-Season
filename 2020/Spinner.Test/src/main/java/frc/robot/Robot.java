/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  /**
   * A Rev Color Sensor V3 object is constructed with an I2C port as a 
   * parameter. The device will be automatically initialized with default 
   * parameters.
   */
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  /**
   * A Rev Color Match object is used to register and detect known colors. This can 
   * be calibrated ahead of time or during operation.
   * 
   * This object uses a simple euclidian distance to estimate the closest match
   * with given confidence range.
   */
  private final ColorMatch m_colorMatcher = new ColorMatch();
  /**
   * Note: Any example colors should be calibrated as the user needs, these
   * are here as a basic example.
   */
  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  Joystick arcadeStick = new Joystick(0);
  WPI_TalonSRX WheelSpinner = new WPI_TalonSRX(12);

  double magScale = .00025553;//((1/4096 * 3.14 * 4)/12) scaling factor to convert the raw encoder value to distance traveled
  double desiredDistance;
  String originalV3Color;
  double distanceTraveled = 0;
  double motorSpeed = .8; //.85 max to get 60rpm with a 25:1 gear ratio

  String gameData = "";
  Timer gameDataTimer = new Timer();
  double gameDataTimeOut = 3;

  @Override
  public void robotInit() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);    
    WheelSpinner.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 30);
    gameDataTimer.reset();
    gameDataTimer.start();      
    SmartDashboard.putNumber("Spinner Speed", motorSpeed);
    WheelSpinner.setInverted(true);
    }

  public void Spin (double distance) {
    WheelSpinner.setSelectedSensorPosition(0);//reset encoder
    distanceTraveled = 0;//reset distance traveled
    desiredDistance = distance;//set desired distance to input distance setpoint passed to method
    while (desiredDistance > distanceTraveled) {
      WheelSpinner.set(ControlMode.PercentOutput, -.6);//spin wheel desired distance
      distanceTraveled = WheelSpinner.getSelectedSensorPosition() * magScale;//update distance traveled
      SmartDashboard.putNumber("Distance Traveled", distanceTraveled); //display updated distance traveled
    }
    WheelSpinner.set(ControlMode.PercentOutput, 0);//turn motor off
  }

  public void CenterPanelPosition (String V3Color, double ss) {
    WheelSpinner.setSelectedSensorPosition(0);//reset encoder
    distanceTraveled = 0;//reset distance traveled
    Color detectedColor = m_colorSensor.getColor();//detect color under v3 sensor
    ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);// match detected color code to 1 of 4 colors and assign color string to V3Color
    if (match.color == kBlueTarget) {V3Color = "Blue";
    } else if (match.color == kRedTarget) {V3Color = "Red";
    } else if (match.color == kGreenTarget) {V3Color = "Green";
    } else if (match.color == kYellowTarget) {V3Color = "Yellow";
    } else {V3Color = "Unknown";
    }
    originalV3Color = V3Color;//set original start color to color detected by v3sensor
    while (originalV3Color == V3Color) {
      WheelSpinner.set(ControlMode.PercentOutput, ss); //spin wheel until color changes
      distanceTraveled = WheelSpinner.getSelectedSensorPosition() * magScale;//update encoder position
      SmartDashboard.putNumber("Distance Traveled", distanceTraveled);//display distance traveled
      detectedColor = m_colorSensor.getColor();//detect color under v3 sensor
      match = m_colorMatcher.matchClosestColor(detectedColor);// match detected color code to 1 of 4 colors and assign color string to V3Color
      if (match.color == kBlueTarget) {V3Color = "Blue";
      } else if (match.color == kRedTarget) {V3Color = "Red";
      } else if (match.color == kGreenTarget) {V3Color = "Green";
      } else if (match.color == kYellowTarget) {V3Color = "Yellow";
      } else {V3Color = "Unknown";
      }
    }
    WheelSpinner.setSelectedSensorPosition(0);//reset encoder position
    desiredDistance = 1.042/2;// set center of a color segment
    distanceTraveled = 0;//reset distance traveled
    while (desiredDistance > distanceTraveled) {
      WheelSpinner.set(ControlMode.PercentOutput, -ss);//rotate forward to center of original color
      distanceTraveled = WheelSpinner.getSelectedSensorPosition() * magScale;//update encoder position
      SmartDashboard.putNumber("Distance Traveled", distanceTraveled); //display distance traveled
    }
    WheelSpinner.set(ControlMode.PercentOutput, 0);//stop motor
  }

  @Override
  public void robotPeriodic() {
    //encoder outputs
    SmartDashboard.putNumber("Encoder Value", WheelSpinner.getSelectedSensorPosition());//raw
    SmartDashboard.putNumber("Distance Traveled", distanceTraveled);//scaled
  }
  
  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
//looking for color position code from game data FMS
Color detectedColor = m_colorSensor.getColor();
String colorString;
ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);

if (match.color == kBlueTarget) {colorString = "Blue";
} else if (match.color == kRedTarget) {colorString = "Red";
} else if (match.color == kGreenTarget) {colorString = "Green";
} else if (match.color == kYellowTarget) {colorString = "Yellow";
} else {colorString = "Unknown";
}

SmartDashboard.putNumber("Red", detectedColor.red);//tune the RGB values from the colorMatch
SmartDashboard.putNumber("Green", detectedColor.green);
SmartDashboard.putNumber("Blue", detectedColor.blue);
SmartDashboard.putNumber("Confidence", match.confidence);
SmartDashboard.putString("Detected Color", colorString);
SmartDashboard.putString("FMS Color", gameData);

gameData = DriverStation.getInstance().getGameSpecificMessage();
if(arcadeStick.getRawButton(3) && gameData.length() > 0){
  CenterPanelPosition(colorString, 0.2);//center the sensor on the panel segment of the current color
  switch (gameData.charAt(0)){
    case 'B' :
      //Blue case code
      if(colorString == "Green"){Spin(3.125);
      } else if(colorString == "Blue"){Spin(2.083);
      } else if(colorString == "Yellow"){Spin(1.042);
      } else {Spin(0);}
      break;
    case 'G' :
      //Green case code
      if(colorString == "Red"){Spin(3.125);
      } else if(colorString == "Green"){Spin(2.083);
      } else if(colorString == "Blue"){Spin(1.042);
      } else {Spin(0);}
      break;
    case 'R' :
      //Red case code
      if(colorString == "Yellow"){Spin(3.125);
      } else if(colorString == "Red"){Spin(2.083);
      } else if(colorString == "Green"){Spin(1.042);
      } else {Spin(0);}
      break;
    case 'Y' :
      //Yellow case code
      if(colorString == "Blue"){Spin(3.125);
      } else if(colorString == "Yellow"){Spin(2.083);
      } else if(colorString == "Red"){Spin(1.042);
      } else {Spin(0);}
      break;
    default :
      //This is corrupt data
      break;
  }
}
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
       if (arcadeStick.getRawButton(2)) {
         Spin(28);//a bit over the 3 full rotations needed
       } else if (arcadeStick.getRawButton(4)) {
         Spin(1);
       } else if (arcadeStick.getRawButton(1)){
        WheelSpinner.set(ControlMode.PercentOutput, .5);
      } else {
        WheelSpinner.set(ControlMode.PercentOutput, 0);
      }
  }

}
