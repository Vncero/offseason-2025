package frc.robot.subsystems.intake;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;
import frc.robot.Robot;
import frc.robot.subsystems.intake.Intake.IntakeConfig;

@Logged
public class IntakeIOSim implements IntakeIO {
    public static final IntakeConfig config = new Intake.IntakeConfig(0, 0, 0);
    
    private DCMotorSim rollers;

    public IntakeIOSim() {
        this.rollers = new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getNEO(1), Intake.kMomentOfInertiaKgMetersSquared, Intake.kGearing),
            DCMotor.getNEO(1));
    }

    @Override
    public void updateInputs(IntakeInputs inputs) {
        rollers.update(Robot.kDefaultPeriod);

        // arc length S = r (radius) * theta (in rad)
        inputs.velocityMetersPerSecond = rollers.getAngularVelocityRadPerSec() * Intake.kWheelRadiusMeters;
        inputs.hasNote = true; // unfortunately few means exist to simulate a beam break properly
    }

    @Override
    public void setVoltage(double volts) {
        rollers.setInputVoltage(volts);
    }
}
