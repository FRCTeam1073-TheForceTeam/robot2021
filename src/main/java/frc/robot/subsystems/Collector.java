package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Collector extends SubsystemBase {

    public Collector() {

    }

    /// Is the collector motor stalled?
    public boolean isStalled() {
      // TODO:
      return true;
    }

    /// Set the collectiton pwm/power level.
    public void setCollect(double power) {
      // TODO:
    }

    /// Reurn the collector motor power.
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
      // TODO:
      return true;
    }

    /// Deploy/retract the collector.
    public void setDeploy(boolean deployed) {
      // TODO:
    }
    
    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
}
