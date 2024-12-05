package frc.robot.subsystems.arm;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Rotation2d;


public interface ArmIO {
    default void updateInputs(ArmInputs inputs) {}

    default void setVoltage(double volts) {}
}
