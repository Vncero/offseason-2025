package frc.robot.subsystems.intake;

import edu.wpi.first.epilogue.Logged;

@Logged
public interface IntakeIO {
    /**
     * In the IO system model, inputs are the data that is read from the robot's sensors at every iteration
     * outputs then are the computed power/voltage values for hardware to run based on robot code
     * The IO system facilitates simulation by offering an extra layer to swap out hardware for simulation systems
     */

    /*
     * (optional): WPILib has Alerts, which may be nice for interfacing robot state with driver dashboards so both drivers and code know about issues
     * --- there may be a way to disable subsystems mid-match? error handling (along with LED signals) are still to be developed
     */

    /*
     * This is where inputs would be updated for the next robot loop
     */
    default void updateInputs(IntakeInputs inputs) {}

    default void setVoltage(double volts) {}
}
