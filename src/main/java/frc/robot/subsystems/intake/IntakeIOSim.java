package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class IntakeIOSim implements IntakeIO {
    public static final Intake.Config config = new Intake.Config(0, 0, 0);
    
    private DCMotorSim rollers;

    public IntakeIOSim() {
        this.rollers = new DCMotorSim(DCMotor.getNEO(1), Intake.gearing, Intake.momentOfInertia);
    }

    @Override
    public void updateInputs(IntakeIO.Inputs inputs) {
    }

    @Override
    public void setVoltage(Measure<Voltage> voltage) {
        
    }
}
