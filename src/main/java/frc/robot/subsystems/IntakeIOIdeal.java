package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import frc.robot.subsystems.intake.IntakeIO;

/**
 * This IO implementation facilitates disabling a subsystem on the robot by not actuating any hardware
 * However, it does produce inputs that match end conditions so Intake commands do not stall
 */
public class IntakeIOIdeal implements IntakeIO {

    public IntakeIOIdeal() {}

    @Override
    public void updateInputs(IntakeIO.Inputs inputs) {
        inputs.velocity = MetersPerSecond.of(0);
        inputs.hasNote = true;
    }
}
