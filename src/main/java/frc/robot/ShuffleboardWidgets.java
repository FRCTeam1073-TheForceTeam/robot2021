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
    static int auto = 0;

    public ShuffleboardWidgets() {
        tab = Shuffleboard.getTab("Robot2021");
        chooseAuto = tab.add("chooseAuto", 0).withWidget(BuiltInWidgets.kNumberSlider).withSize(5, 1).withPosition(0, 0)
                .withProperties(Map.of("min", 0, "max", 10)).getEntry();
    }

    @Override
    public void periodic() {
        Shuffleboard.update();
        auto = (int) chooseAuto.getDouble(0.0);
        System.out.println("Shuffleboard auto: " + auto);
    }
}
