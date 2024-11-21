package frc.robot.subsystems.intake;

import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Voltage;

public class IntakeIOReal implements IntakeIO {
    public static final Intake.Config config = new Intake.Config(0, 0, 0);

    @Override
    public void updateInputs(IntakeIO.Inputs inputs) {
        
    }

    @Override
    public void setVoltage(Measure<Voltage> voltage) {}
}
