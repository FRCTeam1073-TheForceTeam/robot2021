package frc.robot;

import java.util.Map;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
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
import frc.robot.subsystems.PowerCellTracker.PowerCellData;
import frc.robot.subsystems.PowerPortTracker.PowerPortData;

public class ShuffleboardWidgets extends SubsystemBase {
        private static ShuffleboardTab tab;
        private static NetworkTableEntry chooseAuto;
        public static int auto = 0;

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
        //
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

                tab = Shuffleboard.getTab("Robot2021");
                chooseAuto = tab.add("chooseAuto", 0).withWidget(BuiltInWidgets.kNumberSlider).withSize(5, 1)
                                .withPosition(0, 0).withProperties(Map.of("min", 0, "max", 10)).getEntry();
                createWidgets();

                hoodMax = shooter.maxHoodPosition;
                hoodMin = shooter.minHoodPosition;
        }

        @Override
        public void periodic() {
                updateWidgets();
                Shuffleboard.update();
                auto = (int) chooseAuto.getDouble(0.0);
        }

        private void createWidgets() {
                drivetrainSpeedE = tab.add("drivetrainVelocity", drivetrainSpeed).withPosition(0, 6).withSize(2, 1)
                                .getEntry();
                robotXE = tab.add("x-coordinate", robotX).withPosition(4, 0).withSize(2, 1).getEntry();
                robotYE = tab.add("y-coordinate", robotY).withPosition(6, 0).withSize(2, 1).getEntry();
                robotRotationE = tab.add("rotation", robotRotation).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", -180, "max", 180)).withPosition(4, 3).withSize(2, 2)
                                .getEntry();

                turretAngleE = tab.add("turretDegrees", turretAngle).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", 0, "max", 360)).withPosition(6, 3).withSize(2, 2)
                                .getEntry();
                turretVelocityE = tab.add("turretVelocity", turretVelocity).withPosition(8, 0).withSize(2, 1)
                                .getEntry();

                hoodAngleE = tab.add("hoodDegrees", hoodAngle).withWidget(BuiltInWidgets.kDial)
                                .withProperties(Map.of("min", 0, "max", 180)).withPosition(8, 3).withSize(2, 2)
                                .getEntry();

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

                collectorCurrent = collector.getfilteredCurrent();

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
        }
}
