package frc.robot;

import java.util.Map;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class ShuffleboardWidgets extends SubsystemBase {
    private ShuffleboardTab tab;
    private NetworkTableEntry chooseAuto;
    public static int auto = 0;

    private Drivetrain drivetrain;
    private Collector collector;
    private Magazine magazine;
    private Turret turret;
    private Shooter shooter;
    private PowerCellTracker cellTracker;
    private PowerPortTracker portTracker;

    double leftEncoderValue = 0.0;
    double rightEncoderValue = 0.0;
    double drivetrainSpeed = 0.0;
    double gyroAngleDegrees = 0.0;
    double robotX = 0.0;
    double robotY = 0.0;
    double robotRotation = 0.0;
    double turretAngle = 0.0;
    double turretVelocity = 0.0;
    double flywheelVelocity = 0.0;
    double[] flywheelTemperature = new double[2];
    double hoodAngle = 0.0;
    double hoodVelocity = 0.0;
    int cellCount = 0;
    boolean isBrakeset = false;
    boolean isLiftFullyExtended = false;
    boolean isLiftFullyRetracted = false;
    double liftExtension = 0.0;
    boolean isPinned = false;
    boolean isWinchEngaged = false;
    double batteryVoltage = 0.0;

    public ShuffleboardWidgets(Drivetrain drivetrain, Collector collector, Magazine magazine, Turret turret,
            Shooter shooter, PowerCellTracker cellTracker, PowerPortTracker portTracker) {
        this.drivetrain = drivetrain;
        this.collector = collector;
        this.magazine = magazine;
        this.turret = turret;
        this.shooter = shooter;
        this.cellTracker = cellTracker;
        this.portTracker = portTracker;

        tab = Shuffleboard.getTab("Robot2021");
        chooseAuto = tab.add("chooseAuto", 0).withWidget(BuiltInWidgets.kNumberSlider).withSize(5, 1).withPosition(0, 0)
                .withProperties(Map.of("min", 0, "max", 10)).getEntry();

        createWidgets();
    }

    @Override
    public void periodic() {
        updateWidgets();
        Shuffleboard.update();
        auto = (int) chooseAuto.getDouble(0.0);
    }

    private void createWidgets() {
        NetworkTableEntry leftEncoderEntry = tab.add("leftEncoder", leftEncoderValue).withPosition(0, 0).withSize(2, 1)
                .getEntry();
        NetworkTableEntry rightEncoderEntry = tab.add("rightEncoder", rightEncoderValue).withPosition(2, 0)
                .withSize(2, 1).getEntry();
        NetworkTableEntry drivetrainSpeedEntry = tab.add("drivetrainVelocity", drivetrainSpeed).withPosition(0, 6)
                .withSize(2, 1).getEntry();

        NetworkTableEntry gyroAngleEntry = tab.add("gyroAngle", gyroAngleDegrees).withPosition(2, 3).withSize(2, 1)
                .getEntry();
        NetworkTableEntry xcoordinateEntry = tab.add("x-coordinate", robotX).withPosition(4, 0).withSize(2, 1)
                .getEntry();
        NetworkTableEntry ycoordinateEntry = tab.add("y-coordinate", robotY).withPosition(6, 0).withSize(2, 1)
                .getEntry();
        NetworkTableEntry rotationEntry = tab.add("rotation", robotRotation).withWidget(BuiltInWidgets.kDial)
                .withProperties(Map.of("min", -180, "max", 180)).withPosition(4, 3).withSize(2, 2).getEntry();

        NetworkTableEntry turretAngleEntry = tab.add("turretDegrees", turretAngle).withWidget(BuiltInWidgets.kDial)
                .withProperties(Map.of("min", 0, "max", 360)).withPosition(6, 3).withSize(2, 2).getEntry();
        NetworkTableEntry turretVelocityEntry = tab.add("turretVelocity", turretVelocity).withPosition(8, 0)
                .withSize(2, 1).getEntry();

        NetworkTableEntry flywheelVelocityEntry = tab.add("flywheelVelocity", flywheelVelocity).withPosition(0, 1)
                .withSize(2, 1).getEntry();
        NetworkTableEntry flywheelTemperature1Entry = tab
                .add("flywheelTemperature1", flywheelTemperature[0] * (9 / 5) + 32).withPosition(2, 1).withSize(2, 1)
                .getEntry();
        NetworkTableEntry flywheelTemperature2Entry = tab
                .add("flywheelTemperature2", flywheelTemperature[1] * (9 / 5) + 32).withPosition(4, 1).withSize(2, 1)
                .getEntry();
        NetworkTableEntry hoodAngleEntry = tab.add("hoodDegrees", hoodAngle).withWidget(BuiltInWidgets.kDial)
                .withProperties(Map.of("min", 0, "max", 180)).withPosition(8, 3).withSize(2, 2).getEntry();
        NetworkTableEntry hoodVelocityEntry = tab.add("hoodVelocity", hoodVelocity).withPosition(6, 1).withSize(2, 1)
                .getEntry();

        NetworkTableEntry cellCountEntry = tab.add("cellCount", cellCount).withPosition(8, 1).withSize(2, 1).getEntry();

        NetworkTableEntry isBrakesetEntry = tab.add("isBrakeset", isBrakeset).withWidget(BuiltInWidgets.kBooleanBox)
                .withPosition(0, 2).withSize(2, 1).getEntry();
        NetworkTableEntry isLiftFullyExtendedEntry = tab.add("isLiftFullyExtended", isLiftFullyExtended)
                .withWidget(BuiltInWidgets.kBooleanBox).withPosition(2, 2).withSize(2, 1).getEntry();
        NetworkTableEntry isLiftFullyRetractedEntry = tab.add("isLiftFullyRetracted", isLiftFullyRetracted)
                .withWidget(BuiltInWidgets.kBooleanBox).withPosition(4, 2).withSize(2, 1).getEntry();
        NetworkTableEntry liftExtensionEntry = tab.add("liftExtension", liftExtension).withPosition(6, 2).withSize(2, 1)
                .getEntry();
        NetworkTableEntry isPinnedEntry = tab.add("isPinned", isPinned).withWidget(BuiltInWidgets.kBooleanBox)
                .withPosition(8, 2).withSize(2, 1).getEntry();

        NetworkTableEntry isWinchEngagedEntry = tab.add("isWinchEngaged", isWinchEngaged)
                .withWidget(BuiltInWidgets.kBooleanBox).withPosition(0, 3).withSize(2, 1).getEntry();

        NetworkTableEntry batteryVoltageEntry = tab.add("batteryVoltage", batteryVoltage)
                .withWidget(BuiltInWidgets.kDial).withProperties(Map.of("min", 0, "max", 14)).withPosition(0, 4)
                .withSize(2, 2).getEntry();
    }

    private void updateWidgets() {
        drivetrainSpeed = Math.sqrt(Math.pow(drivetrain.getDrivetrainVelocity().vxMetersPerSecond, 2)
                + Math.pow(drivetrain.getDrivetrainVelocity().vyMetersPerSecond, 2));
        Pose2d pose = drivetrain.getRobotPose();
        robotX = pose.getX();
        robotY = pose.getY();
        robotRotation = pose.getRotation().getRadians();
        turretAngle = turret.getPosition();
        turretVelocity = turret.getVelocity();
        // flywheelVelocity = shooter.get;
        // flywheelTemperature = shooter.getInternalTemperature();
        hoodAngle = shooter.getHoodAngle();
        // hoodVelocity = shooter.getHoodVelocity();
        // cellCount = magazine.getCellCount();
        // liftExtension = lift.liftExtension();
        // isWinchEngaged = winch.isWinchEngaged();
        // batteryVoltage = RobotController.getBatteryVoltage();
    }
}
