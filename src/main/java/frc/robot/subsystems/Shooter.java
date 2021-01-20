package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    public Shooter() {

    }

    /// Monitor current level of flywheel for 'spking' to:
    public double getFlywheelCurrent() {
      // TODO:
      return 0.0;
    }

    public void setFlywheelVelocity(double velocity) {
      // TODO:
    }

    public double getFlywheelVelocity() {
      // TODO:
      return 0.0;
    }

    public double getHoodAngle() {
      // TODO:
      return 0.0;
    }

    // Set hood angle
    public void setHoodAngle(double angle) {
      // TODO:

    }

    // Return maximum hood angle.
    public double getMaxHoodAngle() {
      // TODO:
      return 0.0;
    }

    // Return minimum hood angle.
    public double getMinHoodAngle() {
      // TODO:
      return 0.0;
    }

    // Return deadzone turret
    public double getDeadzoneCurrent() {
      // TODO:
      return 0.0;
    }

    public void setDeadzoneVelocity(double velocity) {
      // TODO:
    }

    public double getDeadzoneVelocity() {
      // TODO:
      return 0.0;
    }
    

    @Override
    public void periodic() {
      // This method will be called once per scheduler run
    }
    
}
