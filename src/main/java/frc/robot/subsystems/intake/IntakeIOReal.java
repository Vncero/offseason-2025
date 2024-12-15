package frc.robot.subsystems.intake;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake.IntakeConfig;

@Logged
public class IntakeIOReal implements IntakeIO {
    public static final IntakeConfig config = new IntakeConfig(0, 0, 0);

    private SparkMax rollers = new SparkMax(0, MotorType.kBrushless); 

    private DigitalInput beamBreak = new DigitalInput(0); 

    public IntakeIOReal() {
        configMotor();
    }

    private void configMotor() {
        rollers.configure(
            new SparkMaxConfig()
                .inverted(false)
                .idleMode(IdleMode.kBrake)
                .smartCurrentLimit(20) // amps
                .voltageCompensation(Constants.kRobotInitialVoltage)
                .apply(
                    new EncoderConfig() // native RPM (rot / min) * (circumference m / rot) * (min / 60 s) = m / s
                        .velocityConversionFactor(2 * Math.PI * Intake.kWheelRadiusMeters / 60.0)), 
                ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void updateInputs(IntakeInputs inputs) {
        inputs.velocityMetersPerSecond = rollers.getEncoder().getVelocity(); 
        inputs.hasNote = beamBreak.get();
    }

    @Override
    public void setVoltage(double voltage) {
        rollers.setVoltage(voltage);
    }
}
