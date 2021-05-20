package frc.robot;

import java.util.Map;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.WidgetType;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.PowerCellTracker.PowerCellData;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class ShuffleboardWidgets extends SubsystemBase {
        private static ShuffleboardTab tab;
        private static NetworkTableEntry chooseAuto;
        public static int auto = 100;

        private static ShuffleboardLayout driving;
        private static ShuffleboardLayout collecting;
        private static ShuffleboardLayout magazining;
        private static ShuffleboardLayout turreting;
        private static ShuffleboardLayout shooting;
        private static ShuffleboardLayout cellTracking;
        private static ShuffleboardLayout portTracking;
        private static ShuffleboardLayout shootingReadout;

        private Drivetrain drivetrain;
        private Collector collector;
        private Magazine magazine;
        private Turret turret;
        private Shooter shooter;
        private PowerCellTracker cellTracker;
        private PowerPortTracker portTracker;

        private ChassisSpeeds chassis = new ChassisSpeeds();
        private Pose2d pose = new Pose2d();
        private double robotAngle = 0.0;
        private double robotAng = 0.0;
        private double robotX = 0.0;
        private double robotY = 0.0;
        private double drivetrainSpeed = 0.0;
        private double rotationalSpeed = 0.0;

        private double collectorCurrent = 0.0;

        private boolean magazineSensor = false;
        private double magazinePosition = 0.0;
        private int magazineCount = 0;

        private double turretAngle = 0.0;
        private double turretAng = 0.0;
        private double turretVelocity = 0.0;

        private double flywheelVelocity = 0.0;
        private double hoodAngle = 0.0;
        private double hoodPosition = 0.0;
        private double hoodMin = 0.0;
        private double hoodMax = 0.0;

        private PowerCellData cellData = new PowerCellData();
        private int cellX = 0;
        private int cellY = 0;
        private int cellArea = 0;

        private PowerPortData portData = new PowerPortData();
        private int portX = 0;
        private int portY = 0;

        private NetworkTableEntry robotAngE;
        private NetworkTableEntry robotAngleE;
        private NetworkTableEntry robotXE;
        private NetworkTableEntry robotYE;
        private NetworkTableEntry drivetrainSpeedE;
        private NetworkTableEntry rotationalSpeedE;

        private NetworkTableEntry collectorCurrentE;

        private NetworkTableEntry magazineSensorE;
        private NetworkTableEntry magazinePositionE;
        private NetworkTableEntry magazineCountE;

        private NetworkTableEntry turretAngE;
        private NetworkTableEntry turretAngleE;
        private NetworkTableEntry turretVelocityE;

        private NetworkTableEntry flywheelVelocityE;
        private NetworkTableEntry hoodAngleE;
        private NetworkTableEntry hoodPositionE;
        private NetworkTableEntry hoodMinE;
        private NetworkTableEntry hoodMaxE;

        private NetworkTableEntry cellXE;
        private NetworkTableEntry cellYE;
        private NetworkTableEntry cellAreaE;

        private NetworkTableEntry portXE;
        private NetworkTableEntry portYE;

        private NetworkTableEntry shooterFlywheelSpeed;
        private NetworkTableEntry shooterHoodAngle;
        private NetworkTableEntry sensorRange;
        private NetworkTableEntry portTrackerHasData;

        public ShuffleboardWidgets(Drivetrain drivetrain, Collector collector, Magazine magazine, Turret turret,
                        Shooter shooter, PowerCellTracker cellTracker, PowerPortTracker portTracker) {
                this.drivetrain = drivetrain;
                this.collector = collector;
                this.magazine = magazine;
                this.turret = turret;
                this.shooter = shooter;
                this.cellTracker = cellTracker;
                this.portTracker = portTracker;

        }

        public void initialize() {
                tab = Shuffleboard.getTab("Robot 2021");

                chooseAuto = tab.add("Choose Auto", 0).withWidget(BuiltInWidgets.kTextView).withSize(1, 2)
                                .withPosition(0, 0).getEntry();

                driving = tab.getLayout("Drivetrain", BuiltInLayouts.kList).withSize(2, 3).withPosition(0, 2);
                collecting = tab.getLayout("Collector", BuiltInLayouts.kList).withSize(1, 2).withPosition(1, 0);
                magazining = tab.getLayout("Magazine", BuiltInLayouts.kList).withSize(1, 2).withPosition(2, 0);
                turreting = tab.getLayout("Turret", BuiltInLayouts.kList).withSize(1, 3).withPosition(2, 2);
                shooting = tab.getLayout("Shooter", BuiltInLayouts.kList).withSize(1, 3).withPosition(3, 2);
                cellTracking = tab.getLayout("CellTracker", BuiltInLayouts.kList).withSize(1, 2).withPosition(3, 0);
                portTracking = tab.getLayout("PortTracker", BuiltInLayouts.kList).withSize(1, 2).withPosition(4, 0);
                shootingReadout = tab.getLayout("Shooter readouts", BuiltInLayouts.kList).withSize(2, 2).withPosition(5, 0);

                hoodMax = shooter.maxHoodPosition;
                hoodMin = shooter.minHoodPosition;

                createWidgets();
        }

        @Override
        public void periodic() {
                updateWidgets();
                Shuffleboard.update();
                auto = (int) chooseAuto.getDouble(100.0);
        }

        private void createWidgets() {
                robotAngE = driving.add("Angle mirrored", robotAng).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", -90, "max", 90)).getEntry();
                robotAngleE = driving.add("Angle", robotAngle).getEntry();
                robotXE = driving.add("X", robotX).getEntry();
                robotYE = driving.add("Y", robotY).getEntry();
                drivetrainSpeedE = driving.add("Speed", drivetrainSpeed).getEntry();
                rotationalSpeedE = driving.add("Rotational Speed", rotationalSpeed).getEntry();

                collectorCurrentE = collecting.add("Current", collectorCurrent).withWidget(BuiltInWidgets.kNumberBar)
                                .withProperties(Map.of("min", 0.0, "max", 40.0)).getEntry();

                magazineSensorE = magazining.add("Sensor", magazineSensor).withWidget(BuiltInWidgets.kBooleanBox)
                                .getEntry();
                magazinePositionE = magazining.add("Position", magazinePosition).getEntry();
                magazineCountE = magazining.add("Count", magazineCount).getEntry();

                turretAngE = turreting.add("Angle mirrored", turretAng).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", -90, "max", 90)).getEntry();
                turretAngleE = turreting.add("Angle", turretAngle).getEntry();
                turretVelocityE = turreting.add("Velocity", turretVelocity).getEntry();

                flywheelVelocityE = shooting.add("Velocity", flywheelVelocity).getEntry();
                hoodAngleE = shooting.add("Angle", hoodAngle).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", 0, "max", 180)).getEntry();
                hoodPositionE = shooting.add("Position", hoodPosition).getEntry();
                hoodMinE = shooting.add("Min P", hoodMin).getEntry();
                hoodMaxE = shooting.add("Max P", hoodMax).getEntry();

                cellXE = cellTracking.add("X", cellX).getEntry();
                cellYE = cellTracking.add("Y", cellY).getEntry();
                cellAreaE = cellTracking.add("Area", cellArea).getEntry();

                portXE = portTracking.add("X", portX).getEntry();
                portYE = portTracking.add("Y", portY).getEntry();

                hoodMinE.setDouble(hoodMin);
                hoodMaxE.setDouble(hoodMax);

                portTrackerHasData = shootingReadout.add("Can see port?", portTracker.getPortData(new PowerPortData())).withWidget(BuiltInWidgets.kBooleanBox).getEntry();
                sensorRange = shootingReadout.add("Range sensor distance (meters)", portTracker.getRange()).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", 0, "max", Constants.MAXIMUM_DETECTABLE_RANGE)).getEntry();
                shooterHoodAngle = shootingReadout.add("Hood angle (radians)", hoodAngle).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", shooter.hoodAngleLow*180.0/Math.PI, "max", shooter.hoodAngleHigh*180.0/Math.PI)).getEntry();
                shooterFlywheelSpeed = shootingReadout.add("Flywheel speed (radians/s)", flywheelVelocity).withWidget(BuiltInWidgets.kNumberBar).withProperties(Map.of("min", 0, "max", Constants.MAX_FLYWHEEL_SPEED)).getEntry();
        }

        private void updateWidgets() {
                chassis = drivetrain.getDrivetrainVelocity();
                pose = drivetrain.getRobotPose();
                robotAngle = pose.getRotation().getDegrees();
                robotAng = mirrorAngle(robotAngle);
                robotX = pose.getX();
                robotY = pose.getY();
                drivetrainSpeed = Math
                                .sqrt(Math.pow(chassis.vxMetersPerSecond, 2) + Math.pow(chassis.vyMetersPerSecond, 2));
                rotationalSpeed = chassis.omegaRadiansPerSecond;

                collectorCurrent = Math.abs(collector.getfilteredCurrent());

                magazineSensor = magazine.getSensor();
                magazinePosition = magazine.getPosition();
                magazineCount = magazine.getPowerCellCount();

                turretAngle = Units.radiansToDegrees(turret.getPosition());
                turretAng = mirrorAngle(turretAngle);
                turretVelocity = turret.getVelocity();

                flywheelVelocity = shooter.getFlywheelVelocity();
                hoodAngle = Units.radiansToDegrees(shooter.getHoodAngle());
                hoodPosition = shooter.getHoodPosition();

                cellTracker.getCellData(cellData);
                cellX = cellData.cx;
                cellY = cellData.cy;
                cellArea = cellData.area;

                portTracker.getPortData(portData);
                portX = portData.cx;
                portY = portData.cy;

                robotAngE.setDouble(robotAng);
                robotAngleE.setDouble(robotAngle);
                robotXE.setDouble(robotX);
                robotYE.setDouble(robotY);
                drivetrainSpeedE.setDouble(drivetrainSpeed);
                rotationalSpeedE.setDouble(rotationalSpeed);

                collectorCurrentE.setDouble(collectorCurrent);

                magazineSensorE.setBoolean(magazineSensor);
                magazinePositionE.setDouble(magazinePosition);
                magazineCountE.setNumber(magazineCount);

                turretAngE.setDouble(turretAng);
                turretAngleE.setDouble(turretAngle);
                turretVelocityE.setDouble(turretVelocity);

                flywheelVelocityE.setDouble(flywheelVelocity);
                hoodAngleE.setDouble(hoodAngle);
                hoodPositionE.setDouble(hoodPosition);

                cellXE.setNumber(cellX);
                cellYE.setNumber(cellY);
                cellAreaE.setNumber(cellArea);

                portXE.setNumber(portX);
                portYE.setNumber(portY);

                portTrackerHasData.setBoolean(portTracker.getPortData(new PowerPortData()));
                sensorRange.setDouble(portTracker.getRange());
                shooterHoodAngle.setDouble(hoodAngle);
                shooterFlywheelSpeed.setDouble(flywheelVelocity);

        }

        
        boolean a = false;
        private double mirrorAngle(double raw) {
                raw *= -1;
                if (raw < -90) {
                        return -180 - raw;
                } else if (robotAngle > 90) {
                        return 180 - raw;
                } else {
                        return raw;
                }
        }
}
