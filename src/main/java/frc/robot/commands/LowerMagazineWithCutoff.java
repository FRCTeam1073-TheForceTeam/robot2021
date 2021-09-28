// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.subsystems.Bling;
import frc.robot.subsystems.Magazine;

public class LowerMagazineWithCutoff extends ParallelDeadlineGroup {
  /** Creates a new LowerMagazineWithCutoff. */
  double powerCellDiameters;
  double magazineVelocity;

  public LowerMagazineWithCutoff(Magazine magazine, Bling bling) {
    this(magazine, bling, 0.5, 0.03);
  }

  public LowerMagazineWithCutoff(Magazine magazine, Bling bling, double powerCellDiameters) {
    this(magazine, bling, powerCellDiameters, 0.03);
  }

  public LowerMagazineWithCutoff(Magazine magazine, Bling bling, double powerCellDiameters, double magazineVelocity) {
    super(
      new MagazineSensorCutoff(magazine),
      new AdvanceMagazineCommand(magazine, bling, magazineVelocity, -powerCellDiameters, 0)
    );
  }
}
