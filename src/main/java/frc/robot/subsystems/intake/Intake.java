package frc.robot.subsystems.intake; 

import java.util.function.Supplier;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;


/**
 * Intake functions as an example subsystem implementation.
 * 
 * This code structure is primarily implemented with the following influences:
 * - IO system from Team 6328 Mechanical Advantage
 * - create() and (partly) disable() from Team 1155 SciBorgs
 * - command-based from https://bovlb.github.io/frc-tips/commands/best-practices.html
 */
@Logged
public class Intake extends SubsystemBase {
    public record Config(double kP, double kI, double kD) {}
    
    public static final double kGearing = 1.0;
    public static final double kMomentOfInertia = 0.001;

    // should be a fairly small fraction of robot voltage, < 0.50?
    public static final double kIntakingVoltage = 0.25 * Constants.kRobotInitialVoltage;
    
    private final IntakeIO io;
    private final IntakeInputs inputs;

    private final PIDController controller;

    /**
     * Triggers should be the only way for other subsystems to acquire information about a subsystem, which is the Intake here
     * Commands only need to know if subsystems are in an appropriate state so they can run,
     * not the setpoint or measured velocity specific to that subsystem---too much info
     */

    /*
    * here, note that this is not "touchSensorPressed"---which is how the subsystem knows it has a note, the "solution-space" (don't worry too much about this fancy term though)
    * instead, "hasNote" focuses more on how this info could help with playing the game, the "problem-space"---knowing if we have a note helps with scoring
    */ 
    public final Trigger hasNote;

    public static Intake create() {
        return RobotBase.isReal()
        ? new Intake(new IntakeIOReal(), IntakeIOReal.config)
        : new Intake(new IntakeIOSim(), IntakeIOSim.config);
    }

    /**
     * In the event that the robot physically cannot run this subsystem,
     * this method provides a convenient way to no-op all of the subsystem's commands
     */
    public static Intake disable() {
        return new Intake(new IntakeIOIdeal(), new Intake.Config(0, 0, 0));
    }

    private Intake(IntakeIO io, Config config) {
        this.io = io;
        this.inputs = new IntakeInputs();
        this.controller = new PIDController(config.kP, config.kI, config.kD);

        this.hasNote = new Trigger(() -> inputs.hasNote);
    }

    @Override
    public void periodic() {
        this.io.updateInputs(inputs);
    }

    /**
     * Subsystems like Intake should provide actions they can perform through "Command factories,"
     * methods that return a new Command for the action every time they're called
     * 
     * Commands should generally not end automatically, they should be decorated with end conditions when used
     * e.g. call runRollers().until(intake.hasNote) instead of hiding the intake.hasNote condition into runRollers()
     * 
     * Again, name these methods in terms of how they help play the game, not specifics to the subsystem's sensors/hardware
     */
    public Command runRollers() {
        return runVoltage(() -> kIntakingVoltage);
    }

    /**
     * This is common to any Command for running the Intake,
     * but it's private since it deals with the specifics of the subsystem's hardware---it asks what voltage to run at,
     * which doesn't matter in playing the game
     * @param voltage the desired voltage to run the rollers at (this is a supplier, which looks like `() -> Volts.of(voltage)`)
     * @return a command that runs forever and sets the target voltage of the rollers to the desired voltage
     * */ 
    private Command runVoltage(Supplier<Double> voltage) {
        return run(() -> io.setVoltage(voltage.get()));
    }

    /**
     * 
     * @param velocity the deseired velocity to run the rollers at
     * @return a command that runs forever and uses a PID to reach the desired velocity
     */
    private Command runVelocity(double velocity) {
        controller.setSetpoint(velocity);
        return runVoltage(() -> controller.calculate(this.inputs.velocityMetersPerSecond));
    }

    /**
     * Commands that stop the subsystem (i.e. stop running motors here) are the exception to making Commands never end without a condition
     * They can end immediately after stopping the subsystem---note that for default commands, these "instant" commands are still valid
     * since they'll be repeatedly scheduled anyway if there's no other Command assigned to the subsystem
     */
    public Command stop() {
        return runOnce(() -> io.setVoltage(0));
    }
}
