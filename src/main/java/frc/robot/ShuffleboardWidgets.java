package frc.robot;

import java.util.Map;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShuffleboardWidgets extends SubsystemBase {
    private ShuffleboardTab tab;
    private NetworkTableEntry chooseAuto;
    int auto = 0;

    public ShuffleboardWidgets() {
        tab = Shuffleboard.getTab("Robot2021");
        chooseAuto = tab.add("P", 1).withWidget(BuiltInWidgets.kNumberSlider).withSize(10, 1).withPosition(0, 1)
                .withProperties(Map.of("min", 0, "max", 10)).getEntry();
    }

    @Override
    public void periodic() {
    }
}
