/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.wpilibj.shuffleboard.*;
import java.util.Random;

public class Robot extends TimedRobot {
  private AddressableLED m_ledLeft;
  private AddressableLEDBuffer m_ledBufferLeft;
  // Store what the last hue of the first pixel is

  SendableChooser<String> LED_chooser = new SendableChooser<String>();
  private int m_rainbowFirstPixelHue;
  String LED_pattern;
  int RGB_red =0; //0-255
  int RGB_green =0; //0-255
  int RGB_blue =0; //0-255
  int HSV_hue =0; //0-180
  int HSV_sat =0; //0-255
  int HSV_val =0; //0-255

  @Override
  public void robotInit() {
    
   	LED_chooser.setDefaultOption("Rainbow", "Rainbow");
    LED_chooser.addOption("twinkle", "twinkle"); 
    LED_chooser.addOption("black", "black");
    LED_chooser.addOption("blueAlliance", "blueAlliance"); 
    LED_chooser.addOption("redAlliance", "redAlliance");
    LED_chooser.addOption("green", "green");
    LED_chooser.addOption("totalRandom", "totalRandom");
    LED_chooser.addOption("redAllianceZoom", "redAllianceZoom");
    LED_chooser.addOption("blueAllianceZoom", "blueAllianceZoom");
    LED_chooser.addOption("greenZoom", "greenZoom");

    
    SmartDashboard.putData("Which Defense?", LED_chooser);
    ShuffleboardTab LED = Shuffleboard.getTab("LED");
    LED.add("RGB Red", RGB_red).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 255)).getEntry();
    LED.add("RGB Green", RGB_green).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 255)).getEntry();
    LED.add("RGB Blue", RGB_blue).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 255)).getEntry();
    LED.add("HSV Hue", HSV_hue).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 180)).getEntry();
    LED.add("RGB Saturation", HSV_sat).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 255)).getEntry();
    LED.add("RGB Value", HSV_val).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", 0, "max", 255)).getEntry();
      
    //int rand1 = rand.nextInt(71);
    // PWM port 9
    // Must be a PWM header, not MXP or DIO
    m_ledLeft = new AddressableLED(0);
    //m_ledRight = new AddressableLED(1);

    // Reuse buffer
    // Default to a length of 60, start empty output
    // Length is expensive to set, so only set it once, then just update data
    m_ledBufferLeft = new AddressableLEDBuffer(64);
    m_ledLeft.setLength(m_ledBufferLeft.getLength());
    //m_ledRight.setLength(m_ledBufferRight.getLength());

    // Set the data
    //m_led.setData(m_ledBuffer);
    //m_led.start();
  }

  @Override
  public void robotPeriodic() {
    //Rainbow();
    //twinkle();
    //black();
    //blueAlliance();
    //redAlliance();
    //green();
    //totalRandom();
    //redAllianceZoom();
    //blueAllianceZoom();
    //greenZoom();

    //

    LED_pattern = LED_chooser.getSelected();
    //write a chooser to select one fo the following:
    switch (LED_pattern){
       case "Rainbow": Rainbow();// LED becomes rainbow
       break;

       case "twinkle": twinkle();// LED twinkles green
       break;

       case "black": black();// Turns LED off
       break;

       case "blueAlliance": blueAlliance();// Makes LED blue
       break;

       case "redAlliance": redAlliance();// Makes LED red
       break;

       case "green": green();// Makes LED green
       break;

       case "totalRandom": totalRandom();// Induces seizures with LED
       break;

       case "redAllianceZoom": redAllianceZoom();// Makes LED red, with 1 moving brighter pixel
       break;

       case "blueAllianceZoom": blueAllianceZoom();// Makes LED blue, with 1 moving brighter pixel
       break;

       case "greenZoom": greenZoom();// Makes LED green, with 1 moving brighter pixel
       break;
    }
    
  }

  private void Rainbow() {
    // For every pixel
    for (var i = 0; i < m_ledBufferLeft.getLength(); i++) {
      // Calculate the hue - hue is easier for rainbows because the color
      // shape is a circle so only one value needs to precess
      final var hue = (m_rainbowFirstPixelHue + (i * 180 / m_ledBufferLeft.getLength())) % 180;
      // Set the value
      m_ledBufferLeft.setHSV(i, hue, 255, 128);
    }
    // Increase by to make the rainbow "move"
    m_rainbowFirstPixelHue += 3;
    // Check bounds
    m_rainbowFirstPixelHue %= 180;
    m_ledLeft.setData(m_ledBufferLeft);
    m_ledLeft.start();
  }

  private void black() {
    // For every pixel
    for (var i = 0; i < m_ledBufferLeft.getLength(); i++) {
      // Set the value
      m_ledBufferLeft.setHSV(i, 0, 0, 0);
    }
    m_ledLeft.setData(m_ledBufferLeft);
    m_ledLeft.start();
  }
  
  private void blueAlliance() {
    // For every pixel
    for (var i = 0; i < m_ledBufferLeft.getLength(); i++) {
      // Set the value
      m_ledBufferLeft.setRGB(i, 0, 0, 255);
    }
    m_ledLeft.setData(m_ledBufferLeft);
    m_ledLeft.start();
  }
  
  private void redAlliance() {
    // For every pixel
    for (var i = 0; i < m_ledBufferLeft.getLength(); i++) {
      // Set the value
      m_ledBufferLeft.setRGB(i, 255, 0, 0);
    }
    m_ledLeft.setData(m_ledBufferLeft);
    m_ledLeft.start();
  }
  
  private void green() {
    // For every pixel
    for (var i = 0; i < m_ledBufferLeft.getLength(); i++) {
      // Set the value
      m_ledBufferLeft.setRGB(i, 0, 255, 0);
    }
    m_ledLeft.setData(m_ledBufferLeft);
    m_ledLeft.start();
    }

    Random rand = new Random(System.currentTimeMillis());
    private void twinkle() {
      //Random rand = new Random(System.currentTimeMillis());

      // For every pixel
      for (var i = 0; i < 25; i++) {
        // get random number from seed
        int rand1 = rand.nextInt(70);

        // Set the value
        m_ledBufferLeft.setRGB(rand1, 0, 255, 0);
      }
      // set color of led
      m_ledLeft.setData(m_ledBufferLeft);
      m_ledLeft.start();

      Timer.delay(0.1);
      for(int i = 0; i < m_ledBufferLeft.getLength(); i++) {
        m_ledBufferLeft.setRGB(i, 0, 0, 0);
      }
      // set values to 0
      m_ledLeft.setData(m_ledBufferLeft);
      m_ledLeft.start();
      }

private void totalRandom() {

  // randomize amount of LEDs to be light
  int rand2 = rand.nextInt(m_ledBufferLeft.getLength());
  for(int r = 0; r < 10; r++) {
  rand2 = rand.nextInt(m_ledBufferLeft.getLength());
  }

  // For every pixel
  for (var i = 0; i <= rand2; i++) {
    // get random number from seed
    int rand1 = rand.nextInt(m_ledBufferLeft.getLength());

    int randRed = rand.nextInt(255);
    int randBlue = rand.nextInt(255);
    int randGreen = rand.nextInt(255);

    // Set the value
    m_ledBufferLeft.setRGB(rand1, randRed, randGreen, randBlue);
  }
  // set color of led
  m_ledLeft.setData(m_ledBufferLeft);
  m_ledLeft.start();

  Timer.delay(rand2 % .01);
  for(int i = 0; i < m_ledBufferLeft.getLength(); i++) {
    m_ledBufferLeft.setRGB(i, 0, 0, 0);
  
  }
  // set values to 0
  m_ledLeft.setData(m_ledBufferLeft);
  m_ledLeft.start();
  
  }

  private void redAllianceZoom() {
    int ledOffset = 0; // make offset for brigher led

    // for loop that contians everything
    for(int i = 0; i < m_ledBufferLeft.getLength(); i++) {

      // increment offset by 1 more than loop #
      ledOffset = i + 1;
      if(ledOffset >= m_ledBufferLeft.getLength()) {ledOffset = ledOffset - m_ledBufferLeft.getLength();} // keep ledOffset in array bounds
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
    for(int i = 0; i < m_ledBufferLeft.getLength(); i++) {

      // increment offset by 1 more than loop #
      ledOffset = i + 1;
      if(ledOffset >= m_ledBufferLeft.getLength()) {ledOffset = ledOffset - m_ledBufferLeft.getLength();} // keep ledOffset in array bounds
      m_ledBufferLeft.setRGB(i, 0, 0, 20); // set all colors other than offset to dim red
      m_ledBufferLeft.setRGB(ledOffset, 0, 0, 255); // set offset to bright red
      m_ledLeft.setData(m_ledBufferLeft);
      m_ledLeft.start();
      Timer.delay(.01); // wait 1 hundreth of a second before looping again
    }
  }

  private void greenZoom() {
    int ledOffset = 0; // make offset for brigher led

    // for loop that contians everything
    for(int i = 0; i < m_ledBufferLeft.getLength(); i++) {

      // increment offset by 1 more than loop #
      ledOffset = i + 1;
      if(ledOffset >= m_ledBufferLeft.getLength()) {ledOffset = ledOffset - m_ledBufferLeft.getLength();} // keep ledOffset in array bounds
      m_ledBufferLeft.setRGB(i, 0, 20, 0); // set all colors other than offset to dim red
      m_ledBufferLeft.setRGB(ledOffset, 0, 255, 0); // set offset to bright red
      m_ledLeft.setData(m_ledBufferLeft);
      m_ledLeft.start();
      Timer.delay(.01); // wait 1 hundreth of a second before looping again
    }
  }
}
