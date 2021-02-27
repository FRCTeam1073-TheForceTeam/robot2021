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
        public static int auto = 0;

        private static ShuffleboardLayout driving;
        private static ShuffleboardLayout collecting;
        private static ShuffleboardLayout magazining;
        private static ShuffleboardLayout turreting;
        private static ShuffleboardLayout shooting;
        private static ShuffleboardLayout cellTracking;
        private static ShuffleboardLayout portTracking;

        private Drivetrain drivetrain;
        private Collector collector;
        private Magazine magazine;
        private Turret turret;
        private Shooter shooter;
        private PowerCellTracker cellTracker;
        private PowerPortTracker portTracker;

        private ChassisSpeeds chassis = new ChassisSpeeds();
        private Pose2d pose = new Pose2d();
        private double robotX = 0.0;
        private double robotY = 0.0;
        private double robotRotation = 0.0;
        private double drivetrainSpeed = 0.0;
        private double rotationalSpeed = 0.0;

        private double collectorCurrent = 0.0;

        private double magazinePosition = 0.0;
        private int magazineCount = 0;
        private boolean magazineSensor = false;

        private double turretAngle = 0.0;
        private double turretVelocity = 0.0;

        private double hoodAngle = 0.0;
        private double hoodPosition = 0.0;
        private double hoodMin = 0.0;
        private double hoodMax = 0.0;

        private PowerCellData cellData = new PowerCellData();
        private int cellArea = 0;
        private int cellX = 0;
        private int cellY = 0;

        private PowerPortData portData = new PowerPortData();
        private int portX = 0;
        private int portY = 0;

        private NetworkTableEntry robotXE;
        private NetworkTableEntry robotYE;
        private NetworkTableEntry robotRotationE;
        private NetworkTableEntry drivetrainSpeedE;
        private NetworkTableEntry rotationalSpeedE;

        private NetworkTableEntry collectorCurrentE;

        private NetworkTableEntry magazinePositionE;
        private NetworkTableEntry magazineCountE;
        private NetworkTableEntry magazineSensorE;

        private NetworkTableEntry turretAngleE;
        private NetworkTableEntry turretVelocityE;

        private NetworkTableEntry hoodAngleE;
        private NetworkTableEntry hoodPositionE;
        private NetworkTableEntry hoodMinE;
        private NetworkTableEntry hoodMaxE;

        private NetworkTableEntry cellAreaE;
        private NetworkTableEntry cellXE;
        private NetworkTableEntry cellYE;

        private NetworkTableEntry portXE;
        private NetworkTableEntry portYE;

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
                tab = Shuffleboard.getTab("Robot2021");

                chooseAuto = tab.add("chooseAuto", 0).withWidget(BuiltInWidgets.kTextView).withSize(1, 2)
                                .withPosition(0, 0).getEntry();

                driving = tab.getLayout("Drivetrain", BuiltInLayouts.kList).withSize(2, 3).withPosition(0, 2);
                collecting = tab.getLayout("Collector", BuiltInLayouts.kList).withSize(1, 2).withPosition(1, 0);
                magazining = tab.getLayout("Magazine", BuiltInLayouts.kList).withSize(1, 2).withPosition(2, 0);
                turreting = tab.getLayout("Turret", BuiltInLayouts.kList).withSize(1, 3).withPosition(2, 2);
                shooting = tab.getLayout("Shooter", BuiltInLayouts.kList).withSize(1, 3).withPosition(3, 0);
                cellTracking = tab.getLayout("CellTracker", BuiltInLayouts.kList).withSize(1, 2).withPosition(3, 3);
                portTracking = tab.getLayout("PortTracker", BuiltInLayouts.kList).withSize(1, 2).withPosition(4, 0);

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
                robotRotationE = driving.add("Angle", robotRotation).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", -Math.PI, "max", Math.PI)).getEntry();
                robotXE = driving.add("X", robotX).getEntry();
                robotYE = driving.add("Y", robotY).getEntry();
                drivetrainSpeedE = driving.add("Speed", drivetrainSpeed).getEntry();
                rotationalSpeedE = driving.add("Rotational Speed", rotationalSpeed).getEntry();

                collectorCurrentE = collecting.add("Current", collectorCurrent).withWidget(BuiltInWidgets.kNumberBar)
                                .withProperties(Map.of("min", 0.0, "max", 50.0)).getEntry();

                magazineSensorE = magazining.add("Sensor", magazineSensor).withWidget(BuiltInWidgets.kBooleanBox)
                                .getEntry();
                magazinePositionE = magazining.add("Position", magazinePosition).getEntry();
                magazineCountE = magazining.add("Count", magazineCount).getEntry();

                turretAngleE = turreting.add("Angle", turretAngle).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", -Math.PI, "max", Math.PI)).getEntry();
                turretVelocityE = turreting.add("Velocity", turretVelocity).getEntry();

                hoodAngleE = shooting.add("Angle", hoodAngle).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", 0, "max", Math.PI)).getEntry();
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
        }

        private void updateWidgets() {
                chassis = drivetrain.getDrivetrainVelocity();
                pose = drivetrain.getRobotPose();
                robotX = pose.getX();
                robotY = pose.getY();
                robotRotation = pose.getRotation().getRadians();
                drivetrainSpeed = Math
                                .sqrt(Math.pow(chassis.vxMetersPerSecond, 2) + Math.pow(chassis.vyMetersPerSecond, 2));
                rotationalSpeed = chassis.omegaRadiansPerSecond;

                collectorCurrent = Math.abs(collector.getfilteredCurrent());

                magazinePosition = magazine.getPosition();
                magazineCount = magazine.getPowerCellCount();
                magazineSensor = magazine.getSensor();

                turretAngle = turret.getPosition();
                turretVelocity = turret.getVelocity();

                hoodAngle = shooter.getHoodAngle();
                hoodPosition = shooter.getHoodPosition();

                cellTracker.getCellData(cellData);
                cellArea = cellData.area;
                cellX = cellData.cx;
                cellY = cellData.cy;

                portTracker.getPortData(portData);
                portX = portData.cx;
                portY = portData.cy;

                robotXE.setDouble(robotX);
                robotYE.setDouble(robotY);
                robotRotationE.setDouble(robotRotation);
                drivetrainSpeedE.setDouble(drivetrainSpeed);
                rotationalSpeedE.setDouble(rotationalSpeed);

                collectorCurrentE.setDouble(collectorCurrent);

                magazinePositionE.setDouble(magazinePosition);
                magazineCountE.setNumber(magazineCount);
                magazineSensorE.setBoolean(magazineSensor);

                turretAngleE.setDouble(turretAngle);
                turretVelocityE.setDouble(turretVelocity);

                hoodAngleE.setDouble(hoodAngle);
                hoodPositionE.setDouble(hoodPosition);

                cellAreaE.setNumber(cellArea);
                cellXE.setNumber(cellX);
                cellYE.setNumber(cellY);

                portXE.setNumber(portX);
                portYE.setNumber(portY);
        }
}
