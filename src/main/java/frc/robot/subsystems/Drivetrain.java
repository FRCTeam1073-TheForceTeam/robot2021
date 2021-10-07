package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.MathUtil;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.sensors.PigeonIMU;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

/*

    Left:
        0.10 1360
        0.25 4759
        0.50 10250
        0.75 
        0.85

    Right:
        0.10 1425
        0.25 4844
        0.50 10603
        0.75 
        0.85

*/

public class Drivetrain extends SubsystemBase  {
    private static WPI_TalonFX leftMotorLeader;
    private static WPI_TalonFX rightMotorLeader;
    private static WPI_TalonFX leftMotorFollower;
    private static WPI_TalonFX rightMotorFollower;
    private PigeonIMU gyro;
    private DifferentialDriveOdometry odometry;
    private DifferentialDriveKinematics kinematics;
    private boolean isDebugOutputActive;

    Solenoid winch = new Solenoid(1, 1);
    Solenoid drivetrain = new Solenoid(1, 7);

    double kP = 0.007;//1.1e-1;
    double kI = 0.002;//1e-2;
    double kD = 0;
    double kF = 0.05;

    // double Fconstant = (
    //     (0.10 / 1336.0) + (0.25 / 4746.0) + (0.50 / 10139.0) + (0.75/15255.0) +
    //     (0.10 / 1313.0) + (0.25 / 4754.0) + (0.50 / 10187.0) + (0.75/15349)
    // ) / 8.0;


    //private double wheelDiameter = 0.15;
    // private double ticksPerWheelRotation =
    // ((52352+56574+54036+56452+53588+57594)/6.0)*0.1;//7942.8;
    private double ticksPerMeter = (
    (double) (
            //Raw Encoder Ticks
            /*Left    Right*/
            119300 + 119130 + 118144 + 117828 + 114665 + 119048 + 120111 + 119625 + 119420 + 119625
        ) / 10.0
    ) / Units.feetToMeters(10.0);
    private double robotWidth = 0.5969;
    private final double wheelDiameter = Units.inchesToMeters(5.9);
    private Pose2d robotPose = new Pose2d();
    private double gyroAngle = 0;

    private boolean hasRobotStopped = false;
    private double gyroDriftValue = 0;
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
        odometry = new DifferentialDriveOdometry(getAngleRadians());

        // Kinematics
        kinematics = new DifferentialDriveKinematics(robotWidth);

        engageDrivetrain();
    }

    /**
     * Returns the gyro feedback in degrees instead of radians so that humans
     * reading SmartDashboard feel at ease
     */
    public double getAngleDegrees() {
        // Rotrwation?
        return Units.radiansToDegrees(MathUtil.angleModulus(Units.degreesToRadians(gyroAngle)));
    }

    /**
     * Returns angle in radians, as is proper.
     */
    public Rotation2d getAngleRadians() {
        // Rotrwation?
        return new Rotation2d(MathUtil.angleModulus(Units.degreesToRadians(gyroAngle)));
    }

    @Override
    public void periodic() {
        // This method will be called once per sceduler run
        // new DifferentialDriveWheelSpeeds()
        double rawGyroAngle = getOrientation()[0];

        gyroAngle = rawGyroAngle;// - gyroDriftValue - totalGyroDrift;

//        DifferentialDriveWheelSpeeds wheelSpeeds = getWheelSpeeds();

        robotPose = odometry.update(getAngleRadians(), leftMotorLeader.getSelectedSensorPosition() / ticksPerMeter,
        rightMotorLeader.getSelectedSensorPosition() / ticksPerMeter);        
    }

    /**
     * Returns a Pose2d object containing the translation and rotation components of the robot's position.
     */
    public Pose2d getRobotPose() {
        return new Pose2d(
            robotPose.getTranslation().getX(), 
            robotPose.getTranslation().getY(),
            new Rotation2d(robotPose.getRotation().getRadians())
        );
    }

    /**
     * Resets the odometry so that the current position is the origin and the current angle is 0 radians.
     */
    public void resetRobotOdometry() {
        robotPose = new Pose2d();
        odometry.resetPosition(robotPose,robotPose.getRotation());//robotPose.getRotation());
        leftMotorLeader.setSelectedSensorPosition(0);
        rightMotorLeader.setSelectedSensorPosition(0);
        gyro.setYaw(0);
        gyroAngle = 0;
        gyroDriftValue = 0;
        totalGyroDrift = 0;
        lastGyroValue = 0;
    }

    public void resetRobotOdometry(Pose2d newPose) {
        robotPose = new Pose2d(newPose.getTranslation(), newPose.getRotation());
        odometry.resetPosition(robotPose,robotPose.getRotation());//robotPose.getRotation());
        leftMotorLeader.setSelectedSensorPosition(0);
        rightMotorLeader.setSelectedSensorPosition(0);
        gyro.setYaw(newPose.getRotation().getDegrees());
        gyroAngle = newPose.getRotation().getRadians();
        gyroDriftValue = 0;
        totalGyroDrift = 0;
        lastGyroValue = gyroAngle;
    }

    /**
     * Sets motor rotational speeds in radians/second.
     * @param left The left motor speed in radians/second.
     * @param right The right motor speed in radians/second.
     */
    public void setRotationalVelocity(double left, double right) {
        leftMotorLeader.set(ControlMode.Velocity, left * 2048.0 * 0.1 / (2.0 * Math.PI));
        rightMotorLeader.set(ControlMode.Velocity, right * 2048.0 * 0.1 / (2.0 * Math.PI));
    }

    /**
     * Sets the robot velocity.
     * @param forward The forward speed in meters/second
     * @param rotation The robot's rotational speed in radians/second
     */
    public void setVelocity(double forward, double rotation) {
        DifferentialDriveWheelSpeeds diffSpeeds = kinematics.toWheelSpeeds(new ChassisSpeeds(forward, 0, rotation));
        leftVelocity = diffSpeeds.leftMetersPerSecond * ticksPerMeter * 0.1;
        rightVelocity = diffSpeeds.rightMetersPerSecond * ticksPerMeter * 0.1;

        leftMotorLeader.set(ControlMode.Velocity, leftVelocity);
        rightMotorLeader.set(ControlMode.Velocity, rightVelocity);
        //System.out.println("x");
    }

    public void curvatureDrive(double radius, double velocity) {
        if (Math.abs(radius) < 0.01) {
            // Driving straight
            leftVelocity = rightVelocity = velocity * ticksPerMeter * 0.1;
        } else {
            // Driving on a curve
            leftVelocity = (radius - 0.5 * 0.9) * velocity / radius * ticksPerMeter * 0.1;
            rightVelocity = (radius + 0.5 * 0.9) * velocity / radius * ticksPerMeter * 0.1;
        }
        leftMotorLeader.set(ControlMode.Velocity, leftVelocity);
        rightMotorLeader.set(ControlMode.Velocity, rightVelocity);
    }

    //public void xydv(double x, double y, double degrees, double velocity) {
    //
    //}

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
        return new double[]{
            leftMotorLeader.getSelectedSensorVelocity(),
            rightMotorLeader.getSelectedSensorVelocity()
        };
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

    /**
     * Returns a ChassisSpeeds object for the drivetrain's velocity in m/s.
     * @return
     */
    public ChassisSpeeds getDrivetrainVelocity() {
        return kinematics.toChassisSpeeds(getWheelSpeeds());
    }

    public void engagePneumatics() {
        // drivetrain.set(true);
        // winch.set(false);
    }
    
    public void engageDrivetrain() {

        // drivetrain.set(true);
        // winch.set(false);

        //Factory default
        leftMotorLeader.configFactoryDefault();
        rightMotorLeader.configFactoryDefault();
        leftMotorFollower.configFactoryDefault();
        rightMotorFollower.configFactoryDefault();

        //Followers
        leftMotorFollower.follow(leftMotorLeader);
        rightMotorFollower.follow(rightMotorLeader);
        
        //Inversions
        leftMotorLeader.setInverted(true);
        leftMotorFollower.setInverted(true);
        rightMotorLeader.setInverted(false);
        rightMotorFollower.setInverted(false);
        
        leftMotorLeader.neutralOutput();
        // leftMotorFollower.neutralOutput();
        rightMotorLeader.neutralOutput();
        // rightMotorFollower.neutralOutput();
        
        leftMotorLeader.setSafetyEnabled(false);
        rightMotorLeader.setSafetyEnabled(false);
        // leftMotorFollower.setSafetyEnabled(false);
        // rightMotorFollower.setSafetyEnabled(false);
        
        leftMotorLeader.setNeutralMode(NeutralMode.Brake);
        rightMotorLeader.setNeutralMode(NeutralMode.Brake);
        leftMotorFollower.setNeutralMode(NeutralMode.Brake);
        rightMotorFollower.setNeutralMode(NeutralMode.Brake);
        // leftMotorFollower.setNeutralMode(NeutralMode.Coast);
        // rightMotorFollower.setNeutralMode(NeutralMode.Coast);

        // leftMotorLeader.configClosedloopRamp(0.25);
        // rightMotorLeader.configClosedloopRamp(0.25);
        
        leftMotorLeader.configPeakOutputForward(1.0);
        leftMotorLeader.configPeakOutputReverse(-1.0);
        // leftMotorFollower.configPeakOutputForward(1.0);
        // leftMotorFollower.configPeakOutputReverse(-1.0);
        rightMotorLeader.configPeakOutputForward(1.0);
        rightMotorLeader.configPeakOutputReverse(-1.0);
        // rightMotorFollower.configPeakOutputForward(1.0);
        // rightMotorFollower.configPeakOutputReverse(-1.0);
        
        leftMotorLeader.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));
        rightMotorLeader.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));
        // leftMotorFollower.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));
        // rightMotorFollower.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 28, 33, 0.25));
        
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
        // leftMotorLeader.configVelocityMeasurementWindow(4);
        // leftMotorLeader.configClosedLoopPeriod(0, 5);
        // rightMotorLeader.configVelocityMeasurementWindow(4);
        // rightMotorLeader.configClosedLoopPeriod(0, 5);

        // leftMotorFollower.setInverted(TalonFXInvertType.CounterClockwise);
        // rightMotorFollower.setInverted(TalonFXInvertType.CounterClockwise);
        // leftMotorLeader.setInverted(TalonFXInvertType.CounterClockwise);
        // rightMotorLeader.setInverted(TalonFXInvertType.CounterClockwise);

    }

    public double[] getOrientation() {
        double[] orientation = { 0, 0, 0 };
        gyro.getYawPitchRoll(orientation);
        return orientation;
    }

}
