# robot2021
2021 Competition Robot

### Controls:

Note: Several of the subsystems listed here (especially the shooter and turret) will be mostly or entirely autonomous,
so the teleop controls for many of the mechanisms may be more high-level than those from previous seasons.

#### Driver controller:
  - **Left stick Y axis**:

    Drive forward/backward (current maximum speed for the controls is 1.5 m/s forward).
  - **Right stick X axis**:

    Turns the robot (current maximum rotational speed is 3.0 radians/s).
  - **Right trigger**

    Increases the driving and turning speed (essentially a throttle).
  - **Left trigger**

     Activates an automatic power cell-collecting command, which cancels when the trigger is released.
  -  **Left bumper (held)**: Intakes collector.
  -  **Right bumper (held)**: Expels collector.
  -  **Back button**: Raises collector.
  -  **Start button**: Lowers collector.
  -  **Y button**: Moves magazine up.
  -  **A button**: Moves magazine down (less than it moves up to avoid damaging the electronics.

**Operator controller:**
  -  **Left stick X axis**: Moves turret (limited to a safe range of angles so long as the turret starts in the right place).
  -  **Start button**: Move the magazine downwards by one power cell diameter.
  -  **Back button**: Move the magazine upwards by one power cell diameter.
  -  **D-Pad up**: Raises the hood.
  -  **D-Pad down**: Lowers the hood.
  -  **D-Pad left**: Increases the flywheel speed.
  -  **D-Pad right**: Decreases the flywheel speed.
  -  **Y button**:

      Runs an auto-firing routine which centers the turret, spins up the flywheel and moves the hood, and runs the magazine until a power cell is fired.
  -  **B button**":

      Spins down the flywheel and hood and recenters the turret.
  -  **A button**: 

      Recenters the turret without stopping the flywheel.
  -  **Left bumper**:

      Cancels auto-firing (does not stop the shooter or recenter the turret, just disables the command). Press this button if the shooter is not responding (which is usually the result of the command continuing to run after another command has control of the mechanisms).
