package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.MagazineControls;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.XboxController;
// import frc.robot.subsystems.interfaces.BlingInterface;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

/**
 * Allow the commands running in the robot to express themselves visually.
 */
public class Bling extends SubsystemBase {
  public AddressableLED m_led;
  public AddressableLEDBuffer m_ledBuffer;
  public XboxController driverController;
  Collector collector;
  MagazineControls magazine;
  PowerCellTracker portTracker;

  private int counter_rainbow_right = 0;
  private int move_rainbow_right = 0;

  private int counter_rainbow_left = 0;
  private int move_rainbow_left = 0;

  private double brightness = 0.5;

  static Color red;
  int burst_done;
  int gameDataBlinkCount;
  int burstCount;
  int time;
  int time_burst;
  int time_blinkyLEDs;
  int leds_from_middle;
  double match_time;
  int move;
  String gameData;
  int i_mag;
  int dash_num;
  int dash_time;
  int gameR;
  int gameG;
  int gameB;

  public int[] rgbArr = { 0, 0, 0 };

  public boolean cleared = false;

  public Bling() {
    m_led = new AddressableLED(0);
    m_ledBuffer = new AddressableLEDBuffer(80);
    m_led.setLength(m_ledBuffer.getLength());
    m_led.setData(m_ledBuffer);
    m_led.start();
  }

  public void initialize() {
    time_burst = 0;
    burst_done = 0;
    time = 0;
    time_blinkyLEDs = 0;
    leds_from_middle = 0;
    move = 0;
    gameDataBlinkCount = 0;
    dash_num = 0;
    dash_time = 0;
    gameR = 0;
    gameG = 0;
    gameB = 0;
    // SmartDashboard.putBoolean("Winch", winch.isWinchEngaged());
  }

  @Override
  public void periodic() {
    if (!cleared) {

      // gameData = DriverStation.getInstance().getGameSpecificMessage();

      // if (burst_done == 0) {
      // burst(m_ledBuffer.getLength(), 0, 0, 255);
      // // setColorRGBAll(0, 0, 0);
      // } else {

// <<<<<<< HEAD
//       LEDRainbow(15, 25, 5000);
//       LEDRainbowReverse(40, 25, 5000);

//       batteryBling(0, 10, 8.0, 12.5);

//       m_led.setData(m_ledBuffer);

// =======
//       LEDRainbow(10, m_ledBuffer.getLength() - 10, 250);

// >>>>>>> main
      // dashing(20, 10, 255, 192, 203);

      // if (collector.isDeployed()) {
      // rangeRGB(6, 6, 0, 255, 0);
      // } else {
      // rangeRGB(6, 6, 255, 0, 0);
      // }

      // if (gameData.equals("R") && gameDataBlinkCount < 5) {
      // gameR = 255;
      // gameG = 0;
      // gameB = 0;

      // if (gameDataBlinkCount < 5) {
      // blinkyLights(0, m_ledBuffer.getLength(), gameR, gameG, gameB, true);
      // } else {
      // rangeRGB(70, 10, gameR, gameG, gameB);
      // }
      // } else if (gameData.equals("G") && gameDataBlinkCount < 5) {
      // gameR = 0;
      // gameG = 255;
      // gameB = 0;

      // } else if (gameData.equals("B") && gameDataBlinkCount < 5) {
      // gameR = 0;
      // gameG = 0;
      // gameB = 255;

      // } else if (gameData.equals("Y") && gameDataBlinkCount < 5) {
      // gameR = 252;
      // gameG = 227;
      // gameB = 0;
      // }

      // if (gameDataBlinkCount < 5) {
      // blinkyLights(0, m_ledBuffer.getLength(), gameR, gameG, gameB, true);
      // } else {
      // rangeRGB(70, 10, gameR, gameG, gameB);
      // }

      // LEDRainbow();
      // rangeRGB(6, 20, 0, 0, 255);
      // setColorRGBAll(0, 0, 255);
      // }

      // setColorRGBAll(0, 0, 255);

    } else {
      clearLEDs();
    }
    // rangeRGB(0, 5, 0, 0, 255);
    // gameData = DriverStation.getInstance().getGameSpecificMessage();
    // match_time = DriverStation.getInstance().getMatchTime();

    // if (burst_done == 0) {
    // // burst(bling.getM_LEDBuffer().getLength(), 0, 0, 255);
    // // bling.setColorRGBAll(0, 0, 0);
    // } else {
    // if (gameData.equals("R") && gameDataBlinkCount < 5) {
    // blinkyLights(0, m_ledBuffer.getLength(), 255, 0, 0);

    // } else if (gameData.equals("G") && gameDataBlinkCount < 5) {
    // blinkyLights(0, m_ledBuffer.getLength(), 0, 255, 0);

    // } else if (gameData.equals("B") && gameDataBlinkCount < 5) {
    // blinkyLights(0, m_ledBuffer.getLength(), 0, 0, 255);

    // } else if (gameData.equals("Y") && gameDataBlinkCount < 5) {
    // blinkyLights(0, m_ledBuffer.getLength(), 252, 227, 0);

    // } else {

    // // The first two LEDs turn white if the winch is engaged
    // // if (winch.isWinchEngaged()) {
    // // rangeRGB(0, 2, 255, 255, 255);
    // // } else {
    // // rangeRGB(0, 2, 0, 0, 0);
    // // }

    // // Changes the number and color of LEDS 3-9 based on the battery voltage
    // // batteryBling(2, 6, 8.0, 12.5);

    // // magazineBallCountBling(8, 252, 227, 0);
    // LEDRainbow();
    // }
    // }
  }

  public void setLEDData() {
    m_led.setData(m_ledBuffer);
  }

  public void clearLEDs() {
    setColorRGBAll(0, 0, 0);
  }

  public void cleared() {
    cleared = true;
  }

  public void uncleared() {
    cleared = false;
  }

  public void setArray(String color) {
    if (color.equals("red")) {
      rgbArr[0] = 85;
      rgbArr[1] = 0;
      rgbArr[2] = 0;

    } else if (color.equals("orange")) {
      rgbArr[0] = 85;
      rgbArr[1] = 55;
      rgbArr[2] = 0;

    } else if (color.equals("yellow")) {
      rgbArr[0] = 85;
      rgbArr[1] = 85;
      rgbArr[2] = 0;

    } else if (color.equals("green")) {
      rgbArr[0] = 0;
      rgbArr[1] = 85;
      rgbArr[2] = 0;

    } else if (color.equals("blue")) {
      rgbArr[0] = 0;
      rgbArr[1] = 0;
      rgbArr[2] = 85;

    } else if (color.equals("purple")) {
      rgbArr[0] = 42;
      rgbArr[1] = 0;
      rgbArr[2] = 42;

    } else if (color.equals("black")) {
      rgbArr[0] = 0;
      rgbArr[1] = 0;
      rgbArr[2] = 0;

    } else if (color.equals("white")) {
      rgbArr[0] = 85;
      rgbArr[1] = 85;
      rgbArr[2] = 85;
    }
  }

  // setColorRGBAll sets the LEDs all to one color
  public void setColorRGBAll(int r, int g, int b) {
    for (var i = 0; i < (m_ledBuffer.getLength()); i++) {
      m_ledBuffer.setRGB(i, r, g, b);
    }
    m_led.setData(m_ledBuffer);
  }

  // alternateRGB sets a range of LEDs where the even are one color and the odd
  // are another
  public void alternateRGB(int min, int number, int r1, int g1, int b1, int r2, int g2, int b2) {
    int max = min + number;
    for (int i = min; i < (max); i = i + 2) {
      m_ledBuffer.setRGB(i, r1, g1, b1);
    }

    for (int j = min + 1; j < (max); j = j + 2) {
      m_ledBuffer.setRGB(j, r2, g2, b2);
    }
    m_led.setData(m_ledBuffer);
  }

  // rangeRGB() sets a range of LEDs to one color
  public void rangeRGB(int min, int number, int r, int g, int b) {
    int max = min + number;
    for (int i = min; i < (max); i++) {
      m_ledBuffer.setRGB(i, r, g, b);
    }
    m_led.setData(m_ledBuffer);
  }

  // setPatternHSVAll() sets all of the LEDs to one color using HSV
  public void setPatternHSVAll(int h, int s, int v) {
    for (var i = 0; i < (m_ledBuffer.getLength()); i++) {
      m_ledBuffer.setHSV(i, h, s, v);
    }
    m_led.setData(m_ledBuffer);
  }

  // alternateHSV() has the same functionality as alternateRGB() except with HSV
  // (hue, saturation + value)
  public void alternateHSV(int min, int number, int h1, int s1, int v1, int h2, int s2, int v2) {
    int max = min + number;
    for (int i = min; i < (max); i = i + 2) {
      m_ledBuffer.setHSV(i, h1, s1, v1);
    }

    for (int j = min + 1; j < (max); j = j + 2) {
      m_ledBuffer.setHSV(j, h2, s2, v2);
    }
    m_led.setData(m_ledBuffer);
  }

  // rangeHSV() same as rangeRGB() except using HSV values
  public void rangeHSV(int min, int number, int h, int s, int v) {
    int max = min + number;

    for (int i = min; i < (max); i++) {
      m_ledBuffer.setHSV(i, h, s, v);
    }

    m_led.setData(m_ledBuffer);
  }

  public void setLED(int i, int r, int g, int b) {
    m_ledBuffer.setRGB(i, r, g, b);
    m_led.setData(m_ledBuffer);
  }

  // This sets two leds with the same color
  public void setLEDs2(int i, int i2, int r, int g, int b) {
    m_ledBuffer.setRGB(i, r, g, b);
    m_ledBuffer.setRGB(i2, r, g, b);
    m_led.setData(m_ledBuffer);
  }

  public AddressableLEDBuffer getM_LEDBuffer() {
    return m_ledBuffer;
  }

// //   public void LEDRainbow(int startLEDs, int numLEDs, int time) {
// // <<<<<<< HEAD

// //     if (counter_rainbow_right < time) {
// //       move_rainbow_right++;
// //       counter_rainbow_right = 0;

// //       // for (int i = startLEDs; i < (startLEDs + numLEDs); i++) {
// //       //   m_ledBuffer.setRGB(i, 0, 0, 0);
// //       // }

// //       for (int i = startLEDs; i < (startLEDs + numLEDs); i++) {

// //         if (((i + move_rainbow_right) % 12) == 0 || ((i + move_rainbow_right) % 12) == 1) {
// //           // Sets first LED, then sets every 6 after it "red"
// //           m_ledBuffer.setRGB(i, (int) (255 * brightness), 0, 0);
// //         } else if (((i + 2 + move_rainbow_right) % 12) == 0 || ((i + 2 + move_rainbow_right) % 12) == 1) {
// //           // Sets second LED, then sets every 6 after it "orange"
// //           m_ledBuffer.setRGB(i, (int) (255 * brightness), (int) (69 * brightness), 0);
// //         } else if (((i + 4 + move_rainbow_right) % 12) == 0 || ((i + 4 + move_rainbow_right) % 12) == 1) {
// //           // Sets third LED, then sets every 6 after it "yellow"
// //           m_ledBuffer.setRGB(i, (int) (252 * brightness), (int) (227 * brightness), 0);
// //         } else if (((i + 6 + move_rainbow_right) % 12) == 0 || ((i + 6 + move_rainbow_right) % 12) == 1) {
// //           // Sets fourth LED, then sets every 6 after it "green"
// //           m_ledBuffer.setRGB(i, 0, (int) (255 * brightness), 0);
// //         } else if (((i + 8 + move_rainbow_right) % 12) == 0 || ((i + 8 + move_rainbow_right) % 12) == 1) {
// //           // Sets fifth LED, then sets every 6 after it "blue"
// //           m_ledBuffer.setRGB(i, 0, 0, (int) (255 * brightness));
// //         } else if (((i + 10 + move_rainbow_right) % 12) == 0 || ((i + 10 + move_rainbow_right) % 12) == 1) {
// //           // Sets sixth LED, then sets every 6 after it "purple"
// //           m_ledBuffer.setRGB(i, (int) (128 * brightness), 0, (int) (128 * brightness));
// //         }
// // =======
// //     for (int i = startLEDs; i < startLEDs + numLEDs; i++) {

// //       if (((i + move_rainbow) % 12) == 0 || ((i + move_rainbow) % 12) == 1) {
// //         // Sets first LED, then sets every 6 after it "red"
// //         m_ledBuffer.setRGB(i, (int) (255 * brightness), 0, 0);
// //       } else if (((i + 2 + move_rainbow) % 12) == 0 || ((i + 2 + move_rainbow) % 12) == 1) {
// //         // Sets second LED, then sets every 6 after it "orange"
// //         m_ledBuffer.setRGB(i, (int) (255 * brightness), (int) (69 * brightness), 0);
// //       } else if (((i + 4 + move_rainbow) % 12) == 0 || ((i + 4 + move_rainbow) % 12) == 1) {
// //         // Sets third LED, then sets every 6 after it "yellow"
// //         m_ledBuffer.setRGB(i, (int) (252 * brightness), (int) (227 * brightness), 0);
// //       } else if (((i + 6 + move_rainbow) % 12) == 0 || ((i + 6 + move_rainbow) % 12) == 1) {
// //         // Sets fourth LED, then sets every 6 after it "green"
// //         m_ledBuffer.setRGB(i, 0, (int) (255 * brightness), 0);
// //       } else if (((i + 8 + move_rainbow) % 12) == 0 || ((i + 8 + move_rainbow) % 12) == 1) {
// //         // Sets fifth LED, then sets every 6 after it "blue"
// //         m_ledBuffer.setRGB(i, 0, 0, (int) (255 * brightness));
// //       } else if (((i + 10 + move_rainbow) % 12) == 0 || ((i + 10 + move_rainbow) % 12) == 1) {
// //         // Sets sixth LED, then sets every 6 after it "purple"
// //         m_ledBuffer.setRGB(i, (int) (128 * brightness), 0, (int) (128 * brightness));
// // >>>>>>> main
// //       }
// //     } else {
// //       counter_rainbow_right++;
// //     }
// //       // m_led.setData(m_ledBuffer);
// //   }

// // <<<<<<< HEAD
// //   public void LEDRainbowReverse(int startLEDs, int numLEDs, int time) {

// //     if (counter_rainbow_left < time) {
// //       move_rainbow_left++;
// //       counter_rainbow_left = 0;

// //       // for (int i = startLEDs; i < (startLEDs + numLEDs); i++) {
// //       //   m_ledBuffer.setRGB(i, 0, 0, 0);
// //       // }

// //       for (int i = startLEDs; i < (startLEDs + numLEDs); i++) {

// //         if (((i - move_rainbow_left) % 12) == 0 || ((i - move_rainbow_left) % 12) == 1) {
// //           // Sets first LED, then sets every 6 after it "red"
// //           m_ledBuffer.setRGB(i, (int) (255 * brightness), 0, 0);
// //         } else if (((i - 2 - move_rainbow_left) % 12) == 0 || ((i - 2 - move_rainbow_left) % 12) == 1) {
// //           // Sets second LED, then sets every 6 after it "orange"
// //           m_ledBuffer.setRGB(i, (int) (255 * brightness), (int) (69 * brightness), 0);
// //         } else if (((i - 4 - move_rainbow_left) % 12) == 0 || ((i - 4 - move_rainbow_left) % 12) == 1) {
// //           // Sets third LED, then sets every 6 after it "yellow"
// //           m_ledBuffer.setRGB(i, (int) (252 * brightness), (int) (227 * brightness), 0);
// //         } else if (((i - 6 - move_rainbow_left) % 12) == 0 || ((i - 6 - move_rainbow_left) % 12) == 1) {
// //           // Sets fourth LED, then sets every 6 after it "green"
// //           m_ledBuffer.setRGB(i, 0, (int) (255 * brightness), 0);
// //         } else if (((i - 8 - move_rainbow_left) % 12) == 0 || ((i - 8 - move_rainbow_left) % 12) == 1) {
// //           // Sets fifth LED, then sets every 6 after it "blue"
// //           m_ledBuffer.setRGB(i, 0, 0, (int) (255 * brightness));
// //         } else if (((i - 10 - move_rainbow_left) % 12) == 0 || ((i - 10 - move_rainbow_left) % 12) == 1) {
// //           // Sets sixth LED, then sets every 6 after it "purple"
// //           m_ledBuffer.setRGB(i, (int) (128 * brightness), 0, (int) (128 * brightness));
// //         }
// // =======
// //       if (counter_rainbow < time) {
// //         counter_rainbow++;
// //       } else {
// //         move_rainbow++;
// //         counter_rainbow = 0;
// // >>>>>>> main
// //       }
// //     } else {
// //       counter_rainbow_left++;
// //     }
// //       // m_led.setData(m_ledBuffer);
// //   }

  // burst() lights LEDs from the middle out
  public void burst(int length, int r, int g, int b, boolean init) {
    // Calculates the middle led(s) of the led string
    int middle1 = (int) (Math.floor((length / 2)));
    int middle2 = (int) (Math.ceil((length / 2)));

    if ((leds_from_middle + middle2) < (length - 1) && time_burst < 15) {
      // If there are still more LEDs to change and it is not yet time to change
      // Wait until the 2000th time
      time_burst = time_burst + 1;
    } else if ((leds_from_middle + middle2) < (length - 1)) {
      // If it is time to change and there are still more LEDs to change
      // Reset the time
      time_burst = 0;
      // Moves the LEDs out from the center by one light
      leds_from_middle = leds_from_middle + 1;
      // Sets the LEDs
      setColorRGBAll(0, 0, 0);
      setLEDs2(middle1 - leds_from_middle, middle2 + leds_from_middle, r, g, b);
    } else {
      // Resets the time and says that the burst is finished
      time_burst = 0;
      setColorRGBAll(0, 0, 0);
      if (init) {
        burst_done = 1;
      }
    }
  }

  // blinkyLightsTwoColors() switches the lights between two colors for all LEDs
  public void blinkyLightsTwoColors(int h, int s, int v, int r, int g, int b) {
    if (time < 50) {
      // Sets the LEDs to the first color
      setPatternHSVAll(h, s, v);
      time = time + 1;
    } else if (time < 100) {
      // Sets the LEDs to the second color
      setColorRGBAll(r, g, b);
      time = time + 1;
    } else {
      // Resets the time
      time = 0;
    }
  }

  public void dashing(int min, int num, int r, int g, int b) {
    if (dash_time >= 50) {
      if (dash_num != 0) {
        setLED(min + ((dash_num - 1) % num), 0, 0, 0);
      }
      setLED(min + (dash_num % num), r, g, b);
      dash_num += 1;
    }
    dash_time += 1;
  }

  // blinkyLights() flashes lights on and off in one color for a range of LEDs
  public void blinkyLights(int minLEDsBlink, int numberLEDsBlink, int r, int g, int b, boolean gameData) {
    if (time_blinkyLEDs < 30) {
      // Sets the LEDs to the specified color
      rangeRGB(minLEDsBlink, numberLEDsBlink, r, g, b);
      time_blinkyLEDs = time_blinkyLEDs + 1;
    } else if (time_blinkyLEDs < 60) {
      // Turns the LEDs off
      time_blinkyLEDs = time_blinkyLEDs + 1;
      rangeRGB(minLEDsBlink, numberLEDsBlink, 0, 0, 0);
    } else {
      // Resets the time counter
      time_blinkyLEDs = 0;
      if (gameData) {
        gameDataBlinkCount = gameDataBlinkCount + 1;
      }
    }
  }

  // batteryBling() sets the LED color and number depending on the battery voltage
  public void batteryBling(int minLEDsVolts, int numberLEDsVolts, double min_volts, double max_volts) {
    for (int i = minLEDsVolts; i < (minLEDsVolts + numberLEDsVolts); i++) {
      m_ledBuffer.setRGB(i, 0, 0, 0);
    }
    
    double volts = RobotController.getBatteryVoltage();

    // First, it calculates the percentage of leds that will turn on.
    // amount above the minimum voltage / range of volts
    // the -1 and +1 account for the one that is always on.
    int num = (int) (Math.round(((volts - min_volts) / (max_volts - min_volts)) * (numberLEDsVolts - 1)) + 1);

    // If less than 1/3 of the leds are lit up, the light is red.
    // If between 1/3 and 2/3 of the leds are lit up, the light is yellow.
    // If more than 2/3 of the leds are lit up, the light is green.
    if (num <= (numberLEDsVolts / 3)) {
      rangeRGB(minLEDsVolts, num, 255, 0, 0);
    } else if (num > (numberLEDsVolts / 3) && num <= (2 * (numberLEDsVolts / 3))) {
      rangeRGB(minLEDsVolts, num, 255, 255, 0);
    } else if (num > (2 * (numberLEDsVolts / 3))) {
      rangeRGB(minLEDsVolts, num, 0, 255, 0);
    }
  }

  // movingLEDs() lights a single LED that moves up the range and then restarts
  public void movingLEDs(int minLEDsMove, int numberLEDsMove) {
    if (time < 50) {
      // Waits until the 50th time
      time = time + 1;
    } else {
      if (move < numberLEDsMove - 1) {
        // Changes the LED that is lit
        move = move + 1;
      } else {
        move = 0;
      }
      // Sets the LED that is lit
      int set = minLEDsMove + move;
      rangeRGB(minLEDsMove, numberLEDsMove, 0, 0, 0);
      setLED(set, 255, 0, 0);
    }
  }

  // driverControlledLEDs() allows the driver to control the LEDs using start, B,
  // X, and Y
  public void driverControlledLEDs(int minLEDsDriver, int numberLEDsDriver) {
    if (OI.driverController.getStartButtonPressed()) {
      // If start was pressed
      // set color orange
      rangeRGB(minLEDsDriver, numberLEDsDriver, 255, 42, 0);
    } else if (OI.driverController.getBButtonPressed()) {
      // If b was pressed
      // set colors to have alternating orange and blue
      alternateRGB(minLEDsDriver, numberLEDsDriver, 255, 42, 0, 0, 0, 255);
    } else if (OI.driverController.getXButtonPressed()) {
      // If x was pressed
      // set color blue
      rangeRGB(minLEDsDriver, numberLEDsDriver, 0, 0, 255);
    } else if (OI.driverController.getYButtonPressed()) {
      // If y was pressed
      // turn the light off
      rangeRGB(minLEDsDriver, numberLEDsDriver, 0, 0, 0);
    }
  }

  public void magazineBallCountBling(int min_LEDs, int r, int g, int b) {
    // if (magazine != null) {
    // int ballCount = magazine.getCellCount();
    // if (ballCount == 0) {
    // rangeRGB(min_LEDs, 5, 0, 0, 0);
    // } else if (ballCount > 5){
    // rangeRGB(min_LEDs, 5, 0, 0, 0);
    // } else {
    // rangeRGB(min_LEDs, ballCount, r, g, b);
    // }
    // }
  }

  public void powerPortTrackingBling(int minLEDs, int numLEDs, double min_meters, double max_meters, int r, int g,
      int b) {
    // if (portTracker != null) {
    // if (portTracker.getAdvancedTargets()[0].quality > 0) {
    // int num = (int) (Math.round(((portTracker.getAdvancedTargets()[0].range -
    // min_meters) /
    // (max_meters - min_meters)) * (numLEDs - 1)) + 1);
    // rangeRGB(minLEDs, num, 0, 0, 0);
    // rangeRGB(minLEDs, num, r, g, b);
    // }
    // }
  }

  public void movingPowerPortTrackingBling(int min_LED, int num_LEDs, int r, int g, int b) {
    // if (portTracker != null) {
    // if (portTracker.getAdvancedTargets()[0].quality > 0) {
    // int min_LED_move = min_LED + 1;
    // int num_LEDs_move = num_LEDs - 2;

    // int x_value = portTracker.getAdvancedTargets()[0].cx;
    // int moving_LED = ((x_value / 320) * num_LEDs_move) + min_LED_move;

    // rangeRGB((moving_LED - 1), 3, r, g, b);
    // }
    // }
  }

  public void winchVSdrivetrain(int min_LEDs, int num_LEDs) {
    // The first two LEDs turn white if the winch is engaged
    // if (winch.isWinchEngaged()) {
    // rangeRGB(min_LEDs, num_LEDs, 255, 255, 255);
    // } else {
    // rangeRGB(min_LEDs, num_LEDs, 0, 0, 0);
    // }
  }
}
