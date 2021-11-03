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

public class ShuffleboardWidgets extends SubsystemBase 
{
        private static ShuffleboardTab tab;

        private static ShuffleboardLayout testPanel;


        public ShuffleboardWidgets() {
        }

        public void initialize() 
        {
                tab = Shuffleboard.getTab("Robot 2021");

                testPanel = tab.getLayout("Test Panel", BuiltInLayouts.kList).withSize(2, 4).withPosition(0, 0);

                createWidgets();
        }

        @Override
        public void periodic() 
        {
                updateWidgets();
                Shuffleboard.update();
        }

        private void createWidgets() {
        }

        private void updateWidgets() {
        }
}