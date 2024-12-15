package frc.robot.subsystems.intake;

import edu.wpi.first.epilogue.Logged;

/**
 * This IO implementation facilitates disabling a subsystem on the robot by not actuating any hardware
 * However, it does produce inputs that match end conditions so Intake commands do not stall
 */
public class IntakeIOIdeal implements IntakeIO {
    
    public IntakeIOIdeal() {}

    @Override
    public void updateInputs(IntakeInputs inputs) {
        inputs.velocityMetersPerSecond = 0;
        inputs.hasNote = true;
    }
}
