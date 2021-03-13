package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Collector extends SubsystemBase {
  private boolean isDeployed = false;
  private boolean isRelaxed = false;
  private WPI_TalonSRX collectorMotor;
  private Solenoid collectorWithdrawPneumatic, collectorDeployPneumatic;
  private final DigitalInput collectorSensor = new DigitalInput(1);
  private LinearFilter filter;
  private double power;
  private double rawCurrent;
  private double filteredCurrent;
  private boolean isIntaking;

  public Collector() {
    this.collectorWithdrawPneumatic = new Solenoid(1, 6);
    this.collectorDeployPneumatic = new Solenoid(1, 0);
    this.collectorMotor = new WPI_TalonSRX(27);
    this.collectorMotor.configFactoryDefault();
    this.collectorMotor.setNeutralMode(NeutralMode.Coast);

    this.collectorMotor.enableCurrentLimit(true);
    this.collectorMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 15.0, 30.0, 0.25), 500);
    this.collectorMotor.configPeakCurrentLimit(28, 500);
    this.collectorMotor.configPeakCurrentDuration(750, 500);

    this.filter = LinearFilter.singlePoleIIR(0.75, 0.02);
  }

  // Is the collector motor stalled?
  public boolean isStalled() {
    return 27.85 < Math.abs(getfilteredCurrent());
  }

  public boolean getSensor() {
    return isIntaking;
  }

  /**
   * Sets the motor for the collector to turn in a direction (+ or -) and to a
   * prcentage of its Output
   * 
   * @param power - (from -1 to 1) determines the velocity the motor spins the
   *              colector at going from full speed turning outwards to inwards
   */
  public void setCollect(double power) {
    this.power = power;
    collectorMotor.set(ControlMode.PercentOutput, power);

  }

  /**
   * lets the code belive the collector is deployed
   * 
   * @param manipulateTo - the boolean value isDeployed will be manippulated to be
   *                     (for testing purposes only).
   */
  public void manipulateIsDeployed(boolean manipulateTo) {
    isDeployed = manipulateTo;
  }

  // Return the collector motor power.
  public double getPower() {
    return power;
  }

  // Return the collector load current.
  public double getfilteredCurrent() {
    return filteredCurrent;
  }

  // Return the collector load current.
  public double getRawCurrent() {
    return rawCurrent;
  }

  // Is the collector deployed?
  public boolean isDeployed() {
    return isDeployed;
  }

  /// Is the collector deployed?
  public void raise() {
    collectorDeployPneumatic.set(false);
    collectorWithdrawPneumatic.set(true);
  }

  public void lower() {
    collectorDeployPneumatic.set(true);
    collectorWithdrawPneumatic.set(false);
  }

  /**
   * Are both pneumatics off?
   * 
   * @return boolean isRelaxed
   */
  public boolean isRelaxed() {
    return isRelaxed;
  }

  /**
   * Deploys or withdraws the collector
   * 
   * @param deployed - if input as true will deploy if false will withdraw the
   *                 collector (if it isn't already)
   */
  public void setDeploy(boolean deployed) {
    // TODO: SmartDashboard or Shuffleboard
    if (isDeployed != deployed) {
      if (deployed) {
        collectorWithdrawPneumatic.set(false);
        collectorDeployPneumatic.set(true);
        isDeployed = true;
        isRelaxed = false;
      } else {
        collectorWithdrawPneumatic.set(true);
        collectorDeployPneumatic.set(false);
        isDeployed = false;
        isRelaxed = false;
      }
    } else if (deployed) {
      System.out.println("Collector was already deployed");
    } else {
      System.out.println("Collector was already withdrawn");
    }
  }

  /**
   * Turns both pneumatics off/relaxes the collector
   */
  public void relax() {
    if (!isRelaxed) {
      collectorWithdrawPneumatic.set(false);
      collectorDeployPneumatic.set(false);
      isRelaxed = true;
    } else {
      // TODO: SmartDashboard or Shuffleboard
      System.out.println("Collector was already relaxed");
    }
  }

  @Override
  public void periodic() {
    isIntaking = !collectorSensor.get();
    // This method will be called once per scheduler run
    rawCurrent = collectorMotor.getStatorCurrent();
    filteredCurrent = filter.calculate(rawCurrent);
    SmartDashboard.putNumber("[collector] filteredOutputCurrent", filteredCurrent);
    SmartDashboard.putBoolean("[Collector] Sensor", collectorSensor.get());
  }
}
