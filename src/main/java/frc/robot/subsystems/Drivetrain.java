package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;

public class Drivetrain extends SubsystemBase {
    private static WPI_TalonFX leftMotorLeader;
    private static WPI_TalonFX rightMotorLeader;
    private static WPI_TalonFX leftMotorFollower;
    private static WPI_TalonFX rightMotorFollower;
    private PigeonIMU gyro;
    private DifferentialDriveOdometry odometry;
    private DifferentialDriveKinematics kinematics;
    private DifferentialDriveWheelSpeeds diffSpeeds;
    private boolean isDebugOutputActive;

    private Solenoid winch = new Solenoid(1, 1);
    private Solenoid drivetrain = new Solenoid(1, 7);

    private double kP = 0.007;
    private double kI = 0.002;
    private double kD = 0;
    private double kF = 0.05;

    private double ticksPerMeter = (
        (double) (
            // Raw Encoder Ticks
            /* Left Right */
            119300 + 119130 + 
            118144 + 117828 + 
            114665 + 119048 + 
            120111 + 119625 + 
            119420 + 119625
            ) / 10.0
        ) / Units.feetToMeters(10.0);

    private double robotWidth = 0.5969;
    private final double wheelDiameter = Units.inchesToMeters(5.9);
    private Pose2d robotPose = new Pose2d();
    private double[] orientation;
    private double gyroAngle = 0;
    private double rawGyroAngle;
    private boolean hasRobotStoppedTurning = false;
    private double lastGyroValue = 0;
    private double totalGyroDrift = 0;

    private double leftVelocity = 0;
    private double rightVelocity = 0;

    public boolean isDebugOutputActive() {
        return isDebugOutputActive;
    }

    public Drivetrain() {
        // Setting up motors
        leftMotorLeader = new WPI_TalonFX(12);
        leftMotorFollower = new WPI_TalonFX(14);
        rightMotorLeader = new WPI_TalonFX(13);
        rightMotorFollower = new WPI_TalonFX(15);

        isDebugOutputActive = true;

        // Gyro
        gyro = new PigeonIMU(6);
        gyro.configFactoryDefault();
        gyro.setYaw(0);
        gyro.setFusedHeading(0);

        // Odometry
        odometry = new DifferentialDriveOdometry(getRotation());

        // Kinematics
        kinematics = new DifferentialDriveKinematics(robotWidth);

        engageDrivetrain();
    }

    /**
     * Returns the gyro feedback in degrees.
     */
    public double getAngleDegrees() {
        return gyroAngle;
    }

    /**
     * Returns the Rotation2d of the robot.
     */
    public Rotation2d getRotation() {
        return Rotation2d.fromDegrees(gyroAngle);
    }

    // This method will be called once per sceduler run
    @Override
    public void periodic() {
        rawGyroAngle = getOrientation()[0];

        gyroAngle = rawGyroAngle - totalGyroDrift;

        robotPose = odometry.update(getRotation(), leftMotorLeader.getSelectedSensorPosition() / ticksPerMeter,
                rightMotorLeader.getSelectedSensorPosition() / ticksPerMeter);

        if (isDebugOutputActive) {
            SmartDashboard.putNumber("[Drivetrain] Left Velocity (raw)", leftMotorLeader.getSelectedSensorVelocity());
            SmartDashboard.putNumber("[Drivetrain] Right Velocity (raw)", rightMotorLeader.getSelectedSensorVelocity());

            SmartDashboard.putNumber("[Drivetrain] Left Target Velocity (raw)", leftVelocity);
            SmartDashboard.putNumber("[Drivetrain] Right Target Velocity (raw)", rightVelocity);

            SmartDashboard.putNumber("[Drivetrain] Left Velocity Difference (m/s)",
                    (leftMotorLeader.getSelectedSensorVelocity() - leftVelocity) * 10.0 / ticksPerMeter);
            SmartDashboard.putNumber("[Drivetrain] Right Velocity Difference (m/s)",
                    (rightMotorLeader.getSelectedSensorVelocity() - rightVelocity) * 10.0 / ticksPerMeter);

            SmartDashboard.putNumber("[Drivetrain] Left Error (raw)", leftMotorLeader.getClosedLoopError());
            SmartDashboard.putNumber("[Drivetrain] Right Error (raw)", rightMotorLeader.getClosedLoopError());

            double[] xyz_dps = new double[3];
            gyro.getRawGyro(xyz_dps);
            SmartDashboard.putNumber("[IMU] Actual Rotational Speed (radians/s)", xyz_dps[0] * Math.PI / 180.0);

            SmartDashboard.putNumber("[Drivetrain] Left Output Power", leftMotorLeader.getMotorOutputPercent());
            SmartDashboard.putNumber("[Drivetrain] Right Output Power", rightMotorLeader.getMotorOutputPercent());

            SmartDashboard.putNumber("[Drivetrain] Raw Left Position (ticks)",
                    leftMotorLeader.getSelectedSensorPosition());
            SmartDashboard.putNumber("[Drivetrain] Raw Right Position (ticks)",
                    rightMotorLeader.getSelectedSensorPosition());

            SmartDashboard.putNumber("[Drivetrain] Left Wheel Position (meters)",
                    leftMotorLeader.getSelectedSensorPosition() / ticksPerMeter);
            SmartDashboard.putNumber("[Drivetrain] Right Wheel Position (meters)",
                    rightMotorLeader.getSelectedSensorPosition() / ticksPerMeter);

            SmartDashboard.putString("[Drivetrain] Odometry coordinates",
                    "(" + Math.round((getRobotPose().getTranslation().getX()) * 1000.0) * 0.001 + ","
                            + Math.round(getRobotPose().getTranslation().getY() * 1000.0) * 0.001 + ") @ "
                            + Math.round(getRobotPose().getRotation().getRadians() * 1000.0) * 0.001 + " radians");
        }
    }

    /**
     * Returns a Pose2d object containing the translation and rotation components of
     * the robot's position.
     */
    public Pose2d getRobotPose() {
        return new Pose2d(robotPose.getTranslation().getX(), robotPose.getTranslation().getY(),
                new Rotation2d(robotPose.getRotation().getRadians()));
    }

    /**
     * Resets the odometry so that the current position is the origin and the
     * current angle is 0 radians.
     */
    public void resetRobotOdometry() {
        robotPose = new Pose2d();
        odometry.resetPosition(robotPose, robotPose.getRotation());
        leftMotorLeader.setSelectedSensorPosition(0);
        rightMotorLeader.setSelectedSensorPosition(0);
        gyro.setYaw(0);
        gyroAngle = 0;
        totalGyroDrift = 0;
        lastGyroValue = 0;
    }

    /**
     * Sets motor rotational speeds in radians/second.
     * 
     * @param left  The left motor speed in radians/second.
     * @param right The right motor speed in radians/second.
     */
    public void setRotationalVelocity(double left, double right) {
        leftMotorLeader.set(ControlMode.Velocity, left * 2048.0 * 0.1 / (2.0 * Math.PI));
        rightMotorLeader.set(ControlMode.Velocity, right * 2048.0 * 0.1 / (2.0 * Math.PI));

        if (left != right) {
            hasRobotStoppedTurning = true;
            lastGyroValue = rawGyroAngle;
        } else if (hasRobotStoppedTurning) {
            hasRobotStoppedTurning = false;
            totalGyroDrift += rawGyroAngle - lastGyroValue;
        }
    }

    /**
     * Sets the robot velocity.
     * 
     * @param forward  The forward speed in meters/second
     * @param rotation The robot's rotational speed in radians/second
     */
    public void setVelocity(double forward, double rotation) {
        diffSpeeds = kinematics.toWheelSpeeds(new ChassisSpeeds(forward, 0, rotation));
        leftVelocity = diffSpeeds.leftMetersPerSecond * ticksPerMeter * 0.1;
        rightVelocity = diffSpeeds.rightMetersPerSecond * ticksPerMeter * 0.1;
        if (isDebugOutputActive) {
            SmartDashboard.putNumber("SetVelocity Forward", forward);
            SmartDashboard.putNumber("diffSpeeds.left", diffSpeeds.leftMetersPerSecond);
            SmartDashboard.putNumber("diffSpeeds.right", diffSpeeds.rightMetersPerSecond);
            SmartDashboard.putNumber("leftRotationalSpeed", leftVelocity);
            SmartDashboard.putNumber("rightRotationalSpeed", rightVelocity);
            SmartDashboard.putNumber("wheelDiameter", wheelDiameter);
        }

        leftMotorLeader.set(ControlMode.Velocity, leftVelocity);
        rightMotorLeader.set(ControlMode.Velocity, rightVelocity);

        if (leftVelocity != rightVelocity) {
            hasRobotStoppedTurning = true;
        } else if (hasRobotStoppedTurning) {
            hasRobotStoppedTurning = false;
            totalGyroDrift += rawGyroAngle - lastGyroValue;
        }
    }

    public void setPower(double left, double right) {
        leftMotorLeader.set(ControlMode.PercentOutput, left);
        rightMotorLeader.set(ControlMode.PercentOutput, right);
    }

    /**
     * Returns *raw* linear wheel speeds.
     * 
     * @return Speed of left and right sides of the robot in ticks/0.1s.
     */
    public double[] getRawWheelSpeeds() {
        return new double[] { leftMotorLeader.getSelectedSensorVelocity(),
                rightMotorLeader.getSelectedSensorVelocity() };
    }

    /**
     * Returns linear wheel speeds.
     * 
     * @return Speed of left and right sides of the robot in meters per second.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(leftMotorLeader.getSelectedSensorVelocity() * 10.0 / ticksPerMeter,
                rightMotorLeader.getSelectedSensorVelocity() * 10.0 / ticksPerMeter);
    }

    /**
     * Sets PID configurations
     */
    public void setPIDF() {
        leftMotorLeader.config_kP(0, kP);
        leftMotorLeader.config_kI(0, kI);
        leftMotorLeader.config_kD(0, kD);
        leftMotorLeader.config_kF(0, kF);
        rightMotorLeader.config_kP(0, kP);
        rightMotorLeader.config_kI(0, kI);
        rightMotorLeader.config_kD(0, kD);
        leftMotorLeader.config_kF(0, kF);
    }

    public ChassisSpeeds getDrivetrainVelocity() {
        return kinematics.toChassisSpeeds(getWheelSpeeds());
    }

    public void engagePneumatics() {
        drivetrain.set(true);
        winch.set(false);
    }

    public void engageDrivetrain() {

        drivetrain.set(true);
        winch.set(false);

        // Factory default
        leftMotorLeader.configFactoryDefault();
        rightMotorLeader.configFactoryDefault();
        leftMotorFollower.configFactoryDefault();
        rightMotorFollower.configFactoryDefault();

        // Followers
        leftMotorFollower.follow(leftMotorLeader);
        rightMotorFollower.follow(rightMotorLeader);

        // Inversions
        leftMotorLeader.setInverted(true);
        leftMotorFollower.setInverted(true);
        rightMotorLeader.setInverted(false);
        rightMotorFollower.setInverted(false);

        leftMotorLeader.neutralOutput();
        rightMotorLeader.neutralOutput();

        leftMotorLeader.setSafetyEnabled(false);
        rightMotorLeader.setSafetyEnabled(false);

        leftMotorLeader.setNeutralMode(NeutralMode.Brake);
        rightMotorLeader.setNeutralMode(NeutralMode.Brake);
        leftMotorFollower.setNeutralMode(NeutralMode.Brake);
        rightMotorFollower.setNeutralMode(NeutralMode.Brake);

        leftMotorLeader.configPeakOutputForward(1.0);
        leftMotorLeader.configPeakOutputReverse(-1.0);
        rightMotorLeader.configPeakOutputForward(1.0);
        rightMotorLeader.configPeakOutputReverse(-1.0);

        leftMotorLeader.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));
        rightMotorLeader.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));

        leftMotorLeader.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        rightMotorLeader.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        leftMotorLeader.setSensorPhase(true);
        rightMotorLeader.setSensorPhase(true);

        leftMotorLeader.setSelectedSensorPosition(0);
        rightMotorLeader.setSelectedSensorPosition(0);

        leftMotorLeader.config_kP(0, kP);
        rightMotorLeader.config_kP(0, kP);
        leftMotorLeader.config_kI(0, kI);
        rightMotorLeader.config_kI(0, kI);
        leftMotorLeader.config_kD(0, kD);
        rightMotorLeader.config_kD(0, kD);
        leftMotorLeader.config_kF(0, kF);
        rightMotorLeader.config_kF(0, kF);

        leftMotorLeader.configMaxIntegralAccumulator(0, 400);
        rightMotorLeader.configMaxIntegralAccumulator(0, 400);

        leftMotorLeader.setIntegralAccumulator(0);
        rightMotorLeader.setIntegralAccumulator(0);
    }

    public double[] getOrientation() {
        orientation[0] = 0;
        orientation[1] = 0;
        orientation[2] = 0;
        gyro.getYawPitchRoll(orientation);
        return orientation;
    }
}
