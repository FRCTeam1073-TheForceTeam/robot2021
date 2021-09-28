# robot2021
2021 Competition Robot

### Controls:

Note: Several of the subsystems listed here (especially the shooter and turret) will be mostly or entirely autonomous,
so the teleop controls for many of the mechanisms may be more high-level than those from previous seasons.

**Drivetrain:**
(Driver controller)
  - **Left stick Y axis**: 
  
    Move forward/backward (current maximum speed for the controls is 1.5 m/s forward)
  - **Right stick X axis**:
  
    Turn (current maximum rotational speed is 3.0 radians/s)
  - **Right trigger**

    Increases the robot's speed (essentially a throttle).

**Collector:**
(Driver controller)
  -  **Left bumper (held)**: Intake on collector 
  -  **Right bumper (held)**: Expel on collector 
  -  **Back button**: Raise collector
  -  **Start button**: Lower collector

**Magazine:**
(Operator controller)
  -  **Start button**: Move the magazine upwards by one power cell diameter.
  -  **Back button**: Move the magazine downwards by one power cell diameter.


**Firing controls (Shooter/Turret):**
(Operator controller)
  
  The shooter and turret are mostly controlled through automated commands triggered by the main buttons on the robot. There __are__ controls for manually adjusting the shooter and turret to allow for minor adjustments, but for the most part these commands should be enough:
  - **A button**: Re-aligns turret to zero.
  - **B button**: Stops the shooter and re-aligns the turret.

  - **X button**: Aims the turret to align to the power port and automatically sets the flywheel speed and hood angle to fire a power cell. Unlike `AutomaticFireCommand`, this does not actually fire, as firing and powering the shooter down are separate commands.

  - **Y button**: Advances the magazine by 4 power cells. This is intended as the 'fire' command, where operators press X to aim, Y to fire, and B to stop the shooter after firing or to cancel.

  -  **Left bumper**: Cancels the auto-firing command when pressed. Often necessary when trying to restart the shooter quickly.

  The manual controls for the shooter and turret use the D-Pad and the right stick:
  - **Right stick X**: Moves the turret
  - **D-Pad up**: Raises the hood
  - **D-Pad down**: Lowers the hood
  - **D-Pad left**: Increases the flywheel speed
  - **D-Pad right**: Decreases the flywheel speed
