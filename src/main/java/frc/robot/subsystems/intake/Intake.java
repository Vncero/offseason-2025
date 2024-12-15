package frc.robot.subsystems.intake; 

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.util.Tuning;


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
    public record IntakeConfig(double kP, double kI, double kD) {}
    
    public static final double kWheelRadiusMeters = 0.025;

    public static final double kGearing = 1.0;
    public static final double kMomentOfInertiaKgMetersSquared = 0.001;

    // should be a fairly small fraction of robot voltage, < 0.50?
    public static final double kIntakingVoltage = 0.25 * Constants.kRobotInitialVoltage;
    

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


    private final IntakeIO io;
    private final IntakeInputs inputs;

    private final PIDController controller;


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
        return new Intake(new IntakeIOIdeal(), new Intake.IntakeConfig(0, 0, 0));
    }

    private Intake(IntakeIO io, IntakeConfig config) {
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
    private Command runVelocity(DoubleSupplier velocity) {
        return 
            runOnce(() -> controller.setSetpoint(velocity.getAsDouble()))
                .andThen(runVoltage(() -> controller.calculate(this.inputs.velocityMetersPerSecond)));
    }

    /**
     * Commands that stop the subsystem (i.e. stop running motors here) are the exception to making Commands never end without a condition
     * They can end immediately after stopping the subsystem---note that for default commands, these "instant" commands are still valid
     * since they'll be repeatedly scheduled anyway if there's no other Command assigned to the subsystem
     */
    public Command stop() {
        return runOnce(() -> io.setVoltage(0));
    }

    /**
     * Tuning controllers is a key part of coding a working subsystem
     * The idea with specific commands for tuning is to:
     * 1. Update the PID constants and testing setpoint from the driver dashboard 
     * 2. Test the subsystem moving to the setpoint with the new PID constants and see how well it does---command ends when the subsystem roughly reaches the setpoint
     * 3. Record working PID constants
     * 
     * These commands should be bound to a controller trigger/button so there's an implied end condition of letting the trigger go
     * ---stopping the test if the subsystem goes insane
     */
    public Command tune() {
        var kP = Tuning.entry("kP", controller.getP());
        var kI = Tuning.entry("kI", controller.getI());
        var kD = Tuning.entry("kD", controller.getD());
        var setpointMetersPerSecond = Tuning.entry("target velocity (m per s)", 0);
        return tune(
            () -> kP.get(),
            () -> kI.get(),
            () -> kD.get(),
            () -> setpointMetersPerSecond.get()
        );
    }

    // these remain as suppliers just in case other ways of reading setpoints and constants are appropriate
    private Command tune(
        DoubleSupplier kP, DoubleSupplier kI, DoubleSupplier kD, DoubleSupplier targetVelocity
    ) {
        return 
            runOnce(() -> controller.setPID(kP.getAsDouble(), kI.getAsDouble(), kD.getAsDouble()))
            .andThen(runVelocity(targetVelocity)
                .until(hasNote));
    }
}
