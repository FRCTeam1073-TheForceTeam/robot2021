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
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Collector;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.PowerCellTracker;
import frc.robot.subsystems.PowerPortTracker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;

public class ShuffleboardWidgets extends SubsystemBase {
        private static ShuffleboardTab tab;

        private static ShuffleboardLayout testPanel;

        private Drivetrain drivetrain;
        private Collector collector;
        private Magazine magazine;
        private Turret turret;
        private Shooter shooter;
        private PowerCellTracker cellTracker;
        private PowerPortTracker portTracker;

        private NetworkTableEntry robotVelocityDisplay;
        private double robotVelocity = 0;

        private NetworkTableEntry isCollectorDeployedDisplay;
        private boolean isCollectorDeployed = false;

        private NetworkTableEntry flywheelVelocityGraph;
        private double flywheelVelocity = 0.0;

        private NetworkTableEntry yourDisplay;
        private double myNumber = 0;

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

                testPanel = tab.getLayout("Test Panel", BuiltInLayouts.kList).withSize(2, 4).withPosition(0, 0);

                createWidgets();
        }

        @Override
        public void periodic() {
                updateWidgets();
                Shuffleboard.update();
        }

        private void createWidgets() {

                //Robot velocity in m/s: number bar display from -4 m/s to +4 m/s
                robotVelocityDisplay = testPanel.add("Drivetrain velocity (meters per second)", robotVelocity).withWidget(BuiltInWidgets.kNumberBar)
                                .withProperties(Map.of("min", -4, "max", 4)).getEntry();

                //True/false value for if the collector is deployed (gray when off, green when on).
                isCollectorDeployedDisplay = testPanel.add("Is collector deployed?", isCollectorDeployed).withWidget(BuiltInWidgets.kBooleanBox)
                                .withProperties(Map.of("min", "#050505", "max", "#b0ff00")).getEntry();

                //Graph of the flywheel speed in radians/s.
                flywheelVelocityGraph = testPanel.add("Flywheel velocity (radians per second)", flywheelVelocity).withWidget(BuiltInWidgets.kGraph)
                                .getEntry();

                yourDisplay = testPanel.add("Test Value",0)
                        .withWidget(BuiltInWidgets.kGraph)
                        .withProperties(Map.of(
                                "Lower bound", 0,
                                 "Upper bound", 100000.0))
                        .getEntry();
        }

        private void updateWidgets() {
                robotVelocity = drivetrain.getDrivetrainVelocity().vxMetersPerSecond;
                robotVelocityDisplay.setNumber(robotVelocity);

                isCollectorDeployed = collector.isDeployed();
                isCollectorDeployedDisplay.setBoolean(isCollectorDeployed);

                flywheelVelocity = shooter.getFlywheelVelocity();
                flywheelVelocityGraph.setNumber(flywheelVelocity);

                myNumber = myNumber + 1;
                yourDisplay.setNumber(myNumber);
        }
}
