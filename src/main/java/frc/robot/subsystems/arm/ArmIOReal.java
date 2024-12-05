package frc.robot.subsystems.arm;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.arm.Arm.ArmConfig;

public class ArmIOReal implements ArmIO {
    public static final ArmConfig config = new ArmConfig(0.05, 0, 0, 0);

    private SparkMax leftPivot = new SparkMax(0, MotorType.kBrushless); 
    private SparkMax rightPivot = new SparkMax(0, MotorType.kBrushless); 
    private AbsoluteEncoder armAbsoluteEncoder = leftPivot.getAbsoluteEncoder(); 


    public ArmIOReal() {
        setupMotors();
        setupEncoders();
    }

    private void setupMotors() {

        // TODO: where to put constants? Does the Constants file work for this type of structure?

        leftPivot.configure(
            new SparkMaxConfig()
            .inverted(false)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40), 
            ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); 


            SparkBaseConfig config = new SparkMaxConfig()
            .inverted(false)
            .idleMode(IdleMode.kBrake)
            .smartCurrentLimit(40)
            .follow(leftPivot); 
            
            config.absoluteEncoder
                .positionConversionFactor(360)
                .velocityConversionFactor(360 / 60.0); 

            rightPivot.configure(
                config, 
                ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); 
    }

    public void setupEncoders() {
        // conversion factors
    }


    @Override
    public void updateInputs(ArmInputs inputs) {
        inputs.absoluteAngle = Rotation2d.fromDegrees(armAbsoluteEncoder.getPosition());
        inputs.velocityMetersPerSecond = armAbsoluteEncoder.getVelocity(); 
    }

    @Override
    public void setVoltage(double volts) {
        leftPivot.setVoltage(volts);
        rightPivot.setVoltage(volts);
    }
}
