package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Collector extends SubsystemBase {
  private boolean isDeployed = false;
  private boolean isRelaxed = false;
  private WPI_TalonSRX collectorMotor;
  private Solenoid collectorWithdrawPneumatic, collectorDeployPneumatic;

  public Collector() {
    this.collectorWithdrawPneumatic = new Solenoid(1, 6);
    this.collectorDeployPneumatic = new Solenoid(1, 0);
    this.collectorMotor = new WPI_TalonSRX(27);
    this.collectorMotor.configFactoryDefault();
    this.collectorMotor.setNeutralMode(NeutralMode.Brake);

    this.collectorMotor.enableCurrentLimit(true);
    // The TalonSRX is said to have a hardware limit of 100 amps and can only stay
    // there for 2 seconds
    // And a normal current of 60 amps so I put the limit at 75 and it can stay
    // there for half a second
    this.collectorMotor.configPeakCurrentLimit(75, 0);
    this.collectorMotor.configPeakCurrentDuration(500, 0);
  }

  /// Is the collector motor stalled?
  public boolean isStalled() {
    // TODO: make this actually do stuff
    return false;
  }

  /**
   * Sets the motor for the collector to turn in a direction (+ or -) and to a
   * prcentage of its Output
   * 
   * @param power - (from -1 to 1) determines the velocity the motor spins the
   *              colector at going from full speed turning outwards to inwards
   */
  public void setCollect(double power) {
    // TODO: SmartDashboard or ShuffleBoard
    if (!(isStalled()) && isDeployed) {
      collectorMotor.set(ControlMode.PercentOutput, power);
    } else if (!(isDeployed)) {
      System.out.println("Collector is NOT deployed");
    } else {
      System.out.println("Motor is stalled");
    }
  }

  /// Return the collector motor power.
  public double getPower() {
    // TODO:
    return 0.0;
  }

  /// Return the collector load current.
  public double getLoad() {
    // TODO:
    return 0.0;
  }

  /// Is the collector deployed?
  public boolean isDeployed() {
    return isDeployed;
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
    // This method will be called once per scheduler run
  }
}
