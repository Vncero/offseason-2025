package frc.robot.subsystems.arm;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.arm.Arm.ArmConfig;

public class ArmIOIdeal implements ArmIO {

    public static final ArmConfig config = new ArmConfig(0, 0, 0, 0);

    public ArmIOIdeal() {}

    @Override
    public void updateInputs(ArmInputs inputs) {
        inputs.velocityMetersPerSecond = 0; 
        inputs.absoluteAngle = Rotation2d.fromDegrees(0); 
    }

    @Override
    public void setVoltage(double power) {

    }
}
