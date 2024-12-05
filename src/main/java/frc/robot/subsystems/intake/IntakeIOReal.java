package frc.robot.subsystems.intake;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystems.intake.Intake.Config;

public class IntakeIOReal implements IntakeIO {
    public static final Intake.Config config = new Intake.Config(0, 0, 0);

    private SparkMax rollers = new SparkMax(0, MotorType.kBrushless); 

    private DigitalInput beamBreak = new DigitalInput(0); 

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
