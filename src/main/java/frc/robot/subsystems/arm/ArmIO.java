package frc.robot.subsystems.arm;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Rotation2d;

// TODO: NOTE: to log the IO classes that impl an interface, you need to @Logged each of them
@Logged
public interface ArmIO {
    default void updateInputs(ArmInputs inputs) {}

    default void setVoltage(double volts) {}
}
