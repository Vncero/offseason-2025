package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.Volts;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Velocity;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.DigitalInput;

public class IntakeIOReal implements IntakeIO {
    public static final Intake.Config config = new Intake.Config(0, 0, 0);

    private CANSparkMax rollers = new CANSparkMax(0, MotorType.kBrushless); 

    private DigitalInput beamBreak = new DigitalInput(0); 

    @Override
    public void updateInputs(IntakeIO.Inputs inputs) {
        inputs.velocity = MetersPerSecond.of(rollers.getEncoder().getVelocity()); 
        inputs.hasNote = beamBreak.get();

    }

    @Override
    public void setVoltage(Measure<Voltage> voltage) {
        rollers.setVoltage(voltage.in(Volts));
    }
}
